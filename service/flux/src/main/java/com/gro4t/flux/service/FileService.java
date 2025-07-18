package com.gro4t.flux.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.gro4t.flux.FileStatus;
import com.gro4t.flux.dto.FileDto;
import com.gro4t.flux.dto.FileUploadResponse;
import com.gro4t.flux.mapper.FileMapper;
import com.gro4t.flux.model.FileMetadata;
import com.gro4t.flux.repository.FileMetadataRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class FileService {
    @Autowired
    private final FileMetadataRepository fileMetadataRepository;
    @Autowired
    private final FileMapper fileMapper;
    @Autowired
    private Storage blobStorage;

    public List<FileDto> getFiles() {
        return fileMetadataRepository.findAll()
                .stream()
                .map(fileMapper::fileMetadataToFileDto)
                .toList();
    }

    // TODO: Make this transactional
    // * Add @Transactional
    public FileUploadResponse uploadFile(String name) {
        var fileMetadataList = fileMetadataRepository.findByName(name);
        if (!fileMetadataList.isEmpty()) {
            return FileUploadResponse.builder().errorMessage("File already exists").build();
        }

        var newFileMetadata = FileMetadata.builder()
                .name(name)
                .status(FileStatus.UPLOADING)
                .build();
        fileMetadataRepository.save(newFileMetadata);

        var url = generateSignedUploadUrl(name);
        return FileUploadResponse.builder().uploadUrl(url).build();
    }

    String generateSignedUploadUrl(String name) {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of("flux-test-123808", name)).build();
        Map<String, String> extensionHeaders = new HashMap<>();
        extensionHeaders.put("Content-Type", "application/octet-stream");
        URL url =
                blobStorage.signUrl(
                        blobInfo,
                        15,
                        TimeUnit.MINUTES,
                        Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                        Storage.SignUrlOption.withExtHeaders(extensionHeaders),
                        Storage.SignUrlOption.withV4Signature());
        return url.toString();
    }
}
