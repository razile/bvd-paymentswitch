package com.bvd.paymentswitch;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.bvd.paymentswitch.server.PaymentSwitch;
import com.bvd.paymentswitch.server.PaymentSwitchInitializer;
import com.bvd.paymentswitch.utils.ProtocolUtils;
import com.google.common.cache.CacheBuilder;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Spring Java Configuration and Bootstrap
 * mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=development"
 * 
*/
@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableSpringConfigured
@EnableScheduling
public class Application  {
   
	static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception{
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        PaymentSwitch server = context.getBean(PaymentSwitch.class);
        server.start();
    }
    
    @Bean(name="threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
    	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    	executor.setCorePoolSize(4);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("SaveAuth-");
        executor.initialize();
        return executor;
    }
    
    
    @Value("${tcp.port}")
    private int tcpPort;

    @Value("${boss.thread.count}")
    private int bossCount;

    @Value("${worker.thread.count}")
    private int workerCount;

    @Value("${so.keepalive}")
    private boolean keepAlive;

    @Value("${so.backlog}")
    private int backlog;
    
    @Value("${so.ssl}")
    private boolean ssl;
    
    @Value("${charset.encoding}")
    private String encoding;
        
    @Autowired
    @Qualifier("paymentSwitchInitializer")
    private PaymentSwitchInitializer paymentSwitchInitializer;
   

    @SuppressWarnings("unchecked")
    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(paymentSwitchInitializer);
        Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
        Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
        for (@SuppressWarnings("rawtypes") ChannelOption option : keySet) {
            b.option(option, tcpChannelOptions.get(option));
        }
        return b;
    }

    @Bean(name = "tcpChannelOptions")
    public Map<ChannelOption<?>, Object> tcpChannelOptions() {
        Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
        options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
        options.put(ChannelOption.SO_BACKLOG, backlog);
        return options;
    }

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerCount);
    }

    @Bean(name = "tcpSocketAddress")
    public InetSocketAddress tcpPort() {
        return new InetSocketAddress(tcpPort);
    }
    
    @Bean(name = "sslContext")
    public SslContext sslContext() {
    	SslContext sslCtx = null;
    	try {
			if (ssl) {
				SelfSignedCertificate ssc = new SelfSignedCertificate();
				sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			} 
		} catch (Exception e) {
    		e.printStackTrace();
		}
    	
    	return sslCtx;
    }
    
    @Bean(name = "charset") 
    public Charset charset() {
    	Charset c = null;
    	try {
    		c = Charset.forName(encoding);
    	} catch (Exception e) {
    		c = StandardCharsets.UTF_8;
    	}
    	logger.info("Application Encoding set to: " + c.displayName());
    	ProtocolUtils.APP_CHARSET = c;
    	return c;
    }

    
    @Bean(name = "sslClientContext")
    public SslContext sslClientContext() {
    	SslContext sslCtx = null;
    	try {
    		sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return sslCtx;
    }
    
    @Bean
    public CacheManager cacheManager() {
    	SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
    	
    	GuavaCache merchants = new GuavaCache("merchants", CacheBuilder.newBuilder()
    									.expireAfterWrite(12, TimeUnit.HOURS)
    									.build());
    	
    	GuavaCache fuelCodes = new GuavaCache("fuelcodes", CacheBuilder.newBuilder().build());
    	
    	GuavaCache providers = new GuavaCache("providers", CacheBuilder.newBuilder().build());
    									
    	
    	simpleCacheManager.setCaches(Arrays.asList(merchants,fuelCodes,providers));
    	
    	return simpleCacheManager;
    }
    
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
    

}