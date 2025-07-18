package com.gro4t.flux;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlobStorageConfig {
    @Bean
    public Storage googleCloudStorage() {
        return StorageOptions.newBuilder().setProjectId("flux-test-465915").build().getService();
    }

}
