package com.gro4t.flux;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlobStorageConfig {
    @Value("${flux.blob_storage.project_id}")
    private String projectId;

    @Bean
    public Storage googleCloudStorage() {
        return StorageOptions.newBuilder().setProjectId(projectId).build().getService();
    }

}
