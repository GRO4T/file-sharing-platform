package com.gro4t.flux;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ApplicationProperties {
    @Value("${flux.blob_storage.project_id}")
    private String projectId;

    @Value("${flux.blob_storage.bucket}")
    private String bucketName;
}
