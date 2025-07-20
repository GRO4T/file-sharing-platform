package com.gro4t.flux.files;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.gro4t.flux.SystemConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class FileService {
    private final FileMetadataRepository fileMetadataRepository;
    private final FileMapper fileMapper;
    private final Storage blobStorage;
    private final SystemConfiguration systemConfiguration;

    @Autowired
    public FileService(
            FileMetadataRepository fileMetadataRepository,
            FileMapper fileMapper,
            Storage blobStorage,
            SystemConfiguration systemConfiguration) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.fileMapper = fileMapper;
        this.blobStorage = blobStorage;
        this.systemConfiguration = systemConfiguration;
    }

    public List<FileDto> getFiles() {
        return fileMetadataRepository.findAll().stream().map(fileMapper::fileMetadataToFileDto).toList();
    }

    public FileUploadResponse uploadFile(String objectName) {
        var fileMetadataList = fileMetadataRepository.findByName(objectName);
        // TODO: Try to use MongoDB unique indexing to handle this
        if (!fileMetadataList.isEmpty()) {
            return FileUploadResponse.builder().errorMessage("File already exists").build();
        }

        // Generating signed URL can potentially fail so we do it before saving to DB
        var url = generateSignedUploadUrl(objectName);
        var newFileMetadata = FileMetadata.builder().name(objectName).status(FileMetadata.Status.UPLOADING).build();
        fileMetadataRepository.save(newFileMetadata);
        return FileUploadResponse.builder().uploadUrl(url).build();
    }

    String generateSignedUploadUrl(String objectName) {
        BlobInfo blobInfo =
                BlobInfo.newBuilder(BlobId.of(systemConfiguration.getApplicationProperties().getBucketName(),
                        objectName)).build();
        Map<String, String> extensionHeaders = new HashMap<>();
        extensionHeaders.put("Content-Type", "application/octet-stream");
        URL url = blobStorage.signUrl(blobInfo, 15, TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withExtHeaders(extensionHeaders), Storage.SignUrlOption.withV4Signature());
        return url.toString();
    }
}
