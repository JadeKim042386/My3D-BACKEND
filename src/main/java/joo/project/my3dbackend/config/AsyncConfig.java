package joo.project.my3dbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("MY3D-ASYNC-");
        executor.initialize();
        return executor;
    }
}
