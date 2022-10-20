package pe.ffernacu.blobStorage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import java.util.concurrent.Executor;

import static pe.ffernacu.blobStorage.util.ExecutorUtil.getExecutor;

@Configuration
@EnableAsync
public class AsyncExecutorConfig {
    public static final String APP_ASYNC_EXECUTOR = "appAsyncExecutor";
    private final Integer queueCapacity;
    private final Integer asyncCorePoolSize;
    private final Integer asyncMaxPoolSize;

    public AsyncExecutorConfig(
            @Value("${commons.async.thread.queuecapacity}") Integer queueCapacity,
            @Value("${commons.async.thread.corepoolsize}") Integer asyncCorePoolSize,
            @Value("${commons.async.thread.maxPoolSize}") Integer asyncMaxPoolSize) {

        this.queueCapacity = queueCapacity;
        this.asyncCorePoolSize = asyncCorePoolSize;
        this.asyncMaxPoolSize = asyncMaxPoolSize;
    }

    /**
     * Bean donde se establece la configuración para el manejo de hilos
     *
     * @return
     */
    @Bean(name = APP_ASYNC_EXECUTOR)
    public Executor getAppAsyncExecutor() {
        return getExecutor(asyncCorePoolSize, asyncMaxPoolSize, queueCapacity, "AppThread-");
    }
}
