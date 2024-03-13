package joo.project.my3dbackend.config;

import joo.project.my3dbackend.dto.properties.AsyncProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncConfig {
    private final AsyncProperties asyncProperties;

    @Bean
    public ThreadPoolTaskExecutor threadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncProperties.corePoolSize());
        executor.setMaxPoolSize(asyncProperties.maxPoolSize());
        executor.setQueueCapacity(asyncProperties.queueCapacity());
        executor.setThreadNamePrefix(asyncProperties.threadNamePrefix());
        // graceful shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(20);
        executor.initialize();
        return executor;
    }
}
