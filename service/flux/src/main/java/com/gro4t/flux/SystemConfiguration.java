package com.gro4t.flux;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.gro4t.flux.files.FileMetadataRepository;
import com.gro4t.flux.files.FileService;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SystemConfiguration {
  private final ApplicationProperties applicationProperties;

  public SystemConfiguration(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @Bean
  public FileService fileService(
      FileMetadataRepository fileMetadataRepository, Storage blobStorage) {
    return new FileService(fileMetadataRepository, blobStorage, this);
  }

  @Bean
  public Storage googleCloudStorage() {
    return StorageOptions.newBuilder()
        .setProjectId(applicationProperties.getProjectId())
        .build()
        .getService();
  }
}
