package com.gro4t.flux.files;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.gro4t.flux.SystemConfiguration;
import com.gro4t.flux.files.dto.FileDto;
import com.gro4t.flux.files.dto.FileUploadResponse;
import com.gro4t.flux.files.exception.FluxFileAlreadyExistsException;
import com.gro4t.flux.files.exception.FluxFileNotFoundException;
import com.gro4t.flux.files.exception.FluxFileNotUploadedException;
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
            Storage blobStorage,
            SystemConfiguration systemConfiguration) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.fileMapper = new FileMapperImpl();
        this.blobStorage = blobStorage;
        this.systemConfiguration = systemConfiguration;
    }

    public List<FileDto> getFiles() {
        return fileMetadataRepository.findAll().stream().map(fileMapper::fileMetadataToFileDto).toList();
    }

    /**
     * Register a new file in the metadata database.
     *
     * @param name filename
     * @return When successful returns signed URL used to upload the file to object storage. Otherwise, response contains
     * error message.
     */
    public FileUploadResponse addFile(String name) {
        var fileMetadataList = fileMetadataRepository.findByName(name);
        // TODO: Try to use MongoDB unique indexing to handle this
        if (!fileMetadataList.isEmpty()) {
            throw new FluxFileAlreadyExistsException();
        }

        // Generating signed URL can potentially fail so we do it before saving to DB
        var url = generateSignedUploadUrl(name);
        var newFileMetadata = FileMetadata.builder().name(name).status(FileMetadata.Status.UPLOADING).build();
        fileMetadataRepository.save(newFileMetadata);

        return FileUploadResponse.builder()
                .id(newFileMetadata.getId())
                .uploadUrl(url).build();
    }

    /**
     * Notify file has been uploaded to object storage.
     *
     * @param fileId file ID
     * @return file information
     */
    public FileDto notifyFileUploaded(String fileId) {
        var fileOpt = fileMetadataRepository.findById(fileId);
        if (fileOpt.isEmpty()) {
            throw new FluxFileNotFoundException();
        }
        var file = fileOpt.get();
        file.setStatus(FileMetadata.Status.UPLOADED);
        fileMetadataRepository.save(file);
        return fileMapper.fileMetadataToFileDto(file);
    }

    /**
     * Obtain the link to download the file from object storage.
     *
     * @param fileId file ID
     * @return download URL
     */
    public String getDownloadUrl(String fileId) {
        var fileOpt = fileMetadataRepository.findById(fileId);
        if (fileOpt.isEmpty()) {
            throw new FluxFileNotFoundException();
        }
        var file = fileOpt.get();
        if (file.getStatus() == FileMetadata.Status.UPLOADING) {
            throw new FluxFileNotUploadedException();
        }
        return generateSignedDownloadUrl(file.getName());
    }

    String generateSignedUploadUrl(String objectName) {
        BlobInfo blobInfo = BlobInfo.newBuilder(
                BlobId.of(systemConfiguration.getApplicationProperties().getBucketName(), objectName)).build();
        Map<String, String> extensionHeaders = new HashMap<>();
        extensionHeaders.put("Content-Type", "application/octet-stream");
        URL url = blobStorage.signUrl(blobInfo, 15, TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withExtHeaders(extensionHeaders),
                Storage.SignUrlOption.withV4Signature());
        return url.toString();
    }

    String generateSignedDownloadUrl(String objectName) {
        BlobInfo blobInfo = BlobInfo.newBuilder(
                BlobId.of(systemConfiguration.getApplicationProperties().getBucketName(), objectName)).build();
        URL url = blobStorage.signUrl(blobInfo, 15, TimeUnit.MINUTES,
                Storage.SignUrlOption.withV4Signature());
        return url.toString();
    }
}
