package pe.ffernacu.blobStorage.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class BlobStorageConfig {

    @Value("${aadi.cloud.api.storage.endpoint}")
    private String uriStorage;
    @Value("${aadi.cloud.api.storage.container.name}")
    private String containerName;
    @Value("${aadi.cloud.api.storage.endpoint}/"+"${aadi.cloud.api.storage.container.name}/")
    private String uriFile;
    @Value("${aadi.cloud.api.storage.connection-string}")
    private String myConnectionString;
}
