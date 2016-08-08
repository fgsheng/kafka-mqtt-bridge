package com.ibm.switchbox;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 
 * Thread pool configuration. Thread pool core size, max thread pool size
 * and queue capacity are defined in this class. 
 *
 */
@Configuration
@EnableAsync
@ConfigurationProperties(prefix = "threadpool")
public class AsyncConfiguration implements AsyncConfigurer{
	@Value("${threadpool.core-pool-size:30}")
	private int corePoolSize;
	@Value("${threadpool.max-pool-size:100}")
    private int maxPoolSize;
    @Value("${threadpool.queue-capacity:30}")
    private int queueCapacity;
    
    /**
     * Create a threadpool task executor, and set the corepool size, max pool size
     * and queue capacity.
     * @return return the created executor instance. 
     */
    @Override
    public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("PredictExecutor-");
		executor.initialize();
		return executor;
	}
    
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    	// TODO Auto-generated method stub
    	return null;
    }
}
