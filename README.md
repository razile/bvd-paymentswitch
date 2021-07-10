# BVD Payment Switch - Application Architecture

## Spring Boot Framework

The BDV payment switch is a Spring Boot application, with its entry point found in the _**com.bvd.paymentswitch.Application**_ class. This class is responsible for defining the spring beans that are injected throughout the remainder of the application, and for starting the payment switch server (in the &quot;main&quot; method). The application implements the following key concepts:

- A **Netty NIO** (non-blocking IO) server. This is the low-level TCP-IP server which directly interacts with the POS systems over the defined port (i.e. 10006 for production).
- An **Authorization Client**. This is a template for a proxying client to the targeted payment processors (such as T-CHEK). It is initialized with a processing provider which defines the custom interactions required to communicate with the payment processor.
- **Spring Boot Profiles**. The application makes use of a profile-specific application resources to configure the runtime for an environment (i.e. production, staging, etc.)
- **Web Services**. The application exposes HTTP endpoints for certain transactional information (such as completions and rejections).
- A light **Data Model**. This model allows for transactional data persistence as well as acting as a record for meta data such as card BIN&#39;s, merchant codes, and payment processors.

Data modelling and persistence is implemented using the spring data API, backed by Hibernate and a mySQL database. Logging is handled by Logback, backed by logSlf4j.

## Netty NIO Server

There are three primary classes comprising the NIO server. The are described as follows:

 _**com.bvd.paymentswitch.server.PaymentSwitch**_

This class bootstraps the Netty NIO server, binding the application to a TCP port (as defined in the configuration for the applicable spring-boot profile)

 _**com.bvd.paymentswitch.server.PaymentSwitchInitializer**_

This class defines the channel pipeline and handlers for each request that the server accepts. Specifically, it adds the ssl context (if configured), the decoder and encoder (as per the Kardall Protocol), and the custom handler (PaymentSwitchHandler) for processing the inbound request.

 _**com.bvd.paymentswitch.server.PaymentSwitchHandler**_

This class contains the core logic for processing an inbound request to the switch. It is responsible for proxying the inbound POS request to the processing provider via its authorization client (_ **com.bvd.processing.client.AuthorizationClient** _). In addition, it will lazily load the specific processing provider for the incoming card BIN, which is then injected into the authorization client. Finally, this handler uses a non-blocking completable future to listen for the response coming back from the authorization client, upon which it will respond to the POS client.

## The Authorization Client

The authorization client is where the specific processor processing logic is implemented. It is also a Netty NIO application, with a similar structure to the core server described above. However, it is more appropriately described as a client &quot;template&quot;. At runtime, a concrete payment processing provider is injected into the authorization client. This provider contains the custom logic associated with processing requests and responses for a specific payment processor. The required functions of a processing provider are defined in the interface _ **com.bvd.paymentswitch.processing.provider.ProcessingProvider** _. In the same package, there exists several abstract and concrete implementations:

_**AbstractProcessingProvider**_

This is direct (but abstract) implementation of the ProcessingProvider interface. It will likely be the top-most parent of a concrete implementation class. It implements shared functionality across all providers.

_**PriorPostAbstractProcessingProvider**_

This abstract class, which extends the AbstractProcessingProvider, is the parent of Prior-Post protocol providers (such as EFS and TCHEK). It implements shared functionality for providers that use this protocol.

_**EFSProcessingProvider**_

A concrete provider implementation for EFS, extending the PriorPostAbstractProcessingProvider. It implements any remaining custom functionality for EFS card processing.

_**TChekProcessingProvider**_

A concrete provider implementation for TCHEK, extending the PriorPostAbstractProcessingProvider. It implements any remaining custom functionality for TCHEK card processing

_**ComdataProcessingProvider**_

A concrete provider implementation to COMDATA. As COMDATA uses its own protocol, this class directly extends the AbstractProcessingProvider.

As noted, a concrete provider is injected into the authorization client at runtime. The determination of which provider to use is based on the incoming card&#39;s BIN number. Within the mysql database, there are two tables which are used for this purpose:

_**payment\_processor**_

This table contains the configuration data for a payment processor (such as host, port, provider class, etc).

_**bin\_payment\_processor**_

This table maps a six-digit BIN to a payment processor.

The payment processor is retrieved based on the incoming BIN. The provider class for that processor is then dynamically loaded and injected into the authorization client (note that this process uses caching so that for each bin, the DB data is only fetched one time, and the provider class is only dynamically loaded once).

## Spring Boot Profiles (Application Configuration)

To manage environment-specific configuration, the application makes use of Spring Boot profiles. The properties files can be found under src/main/resources. Global parameters (common across all profiles) are found in &quot; **application.properties**&quot;. Here you will find properties for timeouts, character encoding, and db drivers. Generally, these will not change.

Environment-specific parameters are found in &quot;**application-[environment].properties**&quot;. Currently, there are profiles for production, staging, and development. The following are key profile-specific attributes to note:

| **Property** | **Description** |
| --- | --- |
| spring.jpa.hibernate.ddl-auto | Specifies the hibernate ddl strategy. Keep this on update. |
| spring.jpa.show-sql | Specifies logging of SQL queries. Do not set this to &quot;true&quot; on production. It will slow everything down. |
| spring.datasource.url | Database JDBC url |
| spring.datasource.username | Database username |
| spring.datasource.password | Database password |
| tcp.port | Netty **NIO SERVER** port to bind |
| server.port | **WEB SERVICES** port to bind |
| boss.thread.count | For use with Netty – do not modify |
| worker.thread.count | For use with Netty – do not modify |
| data.seed | Specifies whether to seed the database. This should only be set to true if you are installing the application against a completely EMPTY database. |
| All others: (efs,tchek,comdata hosts) | Used only when data.seed is set to true. |

## Web Services

To support various integrations with other systems, the application exposes RESTful web service endpoints. The controllers for these endpoints can be found in the package _**com.bvd.paymentswitch.web.service**_. Currently, the following GET endpoints are exposed:

_**PosAuthorizationsController (/authorizations/pos/[authId])**_

For a given authorization id, this endpoint will return the associated fuel code for the transaction. The fuel code returned will be a KARDALL fuel code.

_**CompletedAuthorizationsController (/transactions/fuelcard)**_

Returns a list of completed authorizations. Query parameters allow for limiting the list by start and end dates.

_**RejectedAuthorizationsController (/transactions/rejected)**_

Returns a list of rejected authorizations. Query parameters allow for limiting the list by start and end dates.

_**IncompleteAuthorizationsController (/transactions/incomplete)**_

Returns a list of incomplete authorizations. These are authorizations for which a pre-authorization exists without a corresponding post-authorization. Query parameters allow for limiting the list by start and end dates.

## Data Model

To support the above concepts, and for the purpose of data persistence, there is a small data model running on MySQL server.

The following tables exist within the model (and are reflected in the java classes in the **com.bvd.paymentswitch.models** package):

| **Database Table** | **Description** |
| --- | --- |
| **payment\_processor** | This table contains the configuration data for a payment processor (such as host, port, provider class, etc). The authorization client will instantiate a processing provider based on information from this table. Each payment processor supported by the application must have an entry in this table. |
| **bin\_payment\_processor** | This table maps a six-digit BIN to a payment processor. |
| **merchant\_code** | For each payment processor supported by the application (ie. Must have a record in the payment\_processor table), this table links a Kardall site id to the processor&#39;s merchant code for the site. |
| **fuel\_code** | This table contains a mapping between between Kardall fuel codes, efs/tchek codes, and comdata codes. |
| **pos\_authorization** | This table contains a record for each POS request received by the application |
| **processor\_authorization** | This table contains a record for each processor response received by the application |

In addition, the database has views from completed, rejected, and incomplete authorizations.

Within the context of the application, data persistence is managed by the Spring data API. There is heavy use of caching (to reduce DB hits), and asynchronous persistence (to decouple DB writes from primary processing). The **com.bvd.paymentswitch.jpa.service** package contains all the classes that comprise the logic of data access and persistence. Functions that use the data model are defined in the **AuthorizationService** interface, and concretely implemented in the corresponding implementation class (AuthorizationServiceImpl).

# Application Logging

The application using logback with spring profiles support. Loggers and profile specific logging parameters (such as paths and log level) are found in the &quot; **src/main/logback-spring.xml**&quot;. During runtime, all log files are generated at the path &quot;**./logs/[profile]**&quot;. Logs roll on a daily schedule, with logs for previous days stored in the **/archive** subfolder for a period of up to 60 days. Once archived, the log files are suffixed with a date stamp (yyyy-MM-dd). All logging is asynchronous and consequently decoupled from the processing of a transaction. As such, logging, or a failure in logging, will not effect the processing of transactions or the response times of the server.

The following log files will be generated during application runtime:

_**application.log**_

This is the primary log file for the application. Info, warning, and error messages will be found here. In addition, if logging is set to debug level, all transactions received and sent to both the POS and payment processors will be found here.

_**request.log**_

This log contains every request received from the POS system. It is a redundant log backing up the pos\_authorization table in the database.

_**response.log**_

This log contains every response received from payment processors. It is a redundant log backing up the processor\_authorization table in the database.

_**netty.log**_

This is a diagnostic log to check low-level connection information as output by Netty and Java NIO classes. It is generally only needed when diagnosing connection issues.

_**request\_persistence\_errors**_

This log contains POS requests which failed to persist into the database pos\_authorization table.

_**response\_persistence\_errors**_

This log contains processor responses which failed to persist into the database processor\_authorization table

# Build and Deployment

## Project Structure

The payment switch is a Java8 maven project named **bvd-paymentswitch**. You must have maven and java8 installed in order to build the application.

Once cloned from the source repository (i.e. git), it will have the following directory structure on your filesystem:

bvd-paymentswitch (project root)

pom.xml

startStaging.sh

|- src

     |-main

          |-java

               [java packages]

     |-resources

          application.properties

          [application-{profile}.properties]

##

On the current server, the source project is located at the following path:

**/home/bvd/bvd-paymentswitch**

Note that there is a linux user (&quot;bvd&quot;) under which the project is found.

## Maven Build

To build the project, run &quot; **mvn clean package&quot;** at the project root. This will create a **&quot;./target&quot;** subfolder which contains the application bundled as an executable jar file ( **bvd-paymentswitch-1.0.jar** ).

The jar file is the application, and can subsequently be run as a standalone java app, or configured to run as a linux service.

## Running as Standalone Application (Staging)

To run the application as a standalone java app, execute the following command:

**java -Djava.security.egd=file:/dev/./urandom -jar -Dspring.profiles.active=staging target/bvd-paymentswitch-1.0.jar**

Within the project, there is a utility shell script called **startStaging.sh**. This script will start up the staging profile of the application as a standalone instance. Note that this script uses **nohup** to run the application in the background. As such, if you need to stop (or restart) the staging instance you will need to terminate its current process id using the kill command. This can be done as follows:

1. run **ps -ef | grep java**
2. identify the process id of the staging instance (it will be running as root)
3. run **kill -9 [pid]**

## Running as a Linux Service (Production)

(For a detailed description on how to run a Spring Boot application as a Linux service, please see [Deploying Spring Boot Applications](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html) from the official Spring documentation)

Ideally, the application should run as Linux service with restricted privileges (i.e. NOT AS ROOT). This is currently the way it is running in production. The service runs under the **bvd** user account, with the executable jar deployed at the following path:

**/var/lib/bvd/paymentswitch**

All folders and files in this directory must be owned by the bvd user. For a deployment, the jar file must be copied to this folder, followed by a service restart. The production logs are also found as a subdirectory at this path. The following commands can be issued to control the service execution:

**service bvd-paymentswitch stop**

**service bvd-paymentswitch start**

**service bvd-paymentswitch restart**

The service is configured using the **bvd-paymentswitch-1.0.conf** file. The current conf file sets the profile to production, and increases the min and max heap sizes for java.

# How To&#39;s

## Deploying an Update to Production

1. Log into the server, and **cd** to **/home/bvd/bvd-paymentswitch**
2. Run &quot; **git pull**&quot; to update the project
3. Run &quot; **mvn clean package**&quot;
4. Copy (and overwrite) &quot; **target/bvd-paymentswitch-1.0.jar**&quot; to **&quot;/var/lib/bvd/paymentswitch/bvd-paymentswitch-1.0.jar**&quot;
5. Run &quot; **service bvd-paymentswitch restart**&quot; to restart the service

## Adding a new BIN mapping for a Payment Processor

1. Insert a record into the table **bin\_paymentprocessor**

## Add a new POS site

1. For each payment processor that the site will be using, insert a record into the table **merchant\_code**
2. Due to caching in the application, the new code will be active within 12 hours. However, if you need it sooner, restart the application.

## Add a new Payment Processor

1. Create a concrete class which implements the **com.bvd.paymentswitch.processing.provider.ProcessingProvider** interface (if possible, you should extend one of the existing abstract classes rather than attempt a full implementation of all the methods)
2. If necessary, create the encoder and decoder for the processor&#39;s protocol (in **com.bvd.paymentswitch.protocol** ). Reuse existing encoders/decoders if possible.
3. Add a record to **payment\_processor** table, describing the necessary meta data for the processor as well as the implementation class (full package name required) for the provider.
4. For each POS site, add the processor&#39;s merchant codes to the **merchant\_code** table
5. Add BIN&#39;s for the payment processor to the **bin\_payment\_processor table**
6. The new provider will be active within 12 hours (due to caching). Alternatively, restart the application.
