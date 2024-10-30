package com.xzr.socket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xzr
 */
@Configuration
public class ThreadPoolConfig {


    /**
     * 配置固定大小的线程池
     * 核心池大小和最大池大小都设置为固定值，队列大小可调
     * 适用于执行长时间的任务，需要限制线程数量的场景
     */
    @Bean(name = "socketThreadPool")
    public ThreadPoolTaskExecutor fixedThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2; // 核心线程数根据 CPU 核心数调整
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(200); // 保留最大值为200
        executor.setQueueCapacity(500); // 根据实际场景缩小队列大小
        executor.setKeepAliveSeconds(180); // 线程空闲时间调整为180秒
        executor.setThreadNamePrefix("SkThreadPool-");

        executor.setThreadFactory(new CustomThreadFactory());

        // 拒绝策略保持为CallerRunsPolicy
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
            try {
                if (!executor.getThreadPoolExecutor().awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.getThreadPoolExecutor().shutdownNow();
                }
            } catch (InterruptedException ex) {
                executor.getThreadPoolExecutor().shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));

        return executor;
    }


    // 自定义线程工厂，为线程命名，便于问题排查
    private static class CustomThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final ThreadGroup group;

        public CustomThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, "SkThreadPool-" + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
