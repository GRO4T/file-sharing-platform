package com.gro4t.flux;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.gro4t.flux.dto.FileDto;
import com.gro4t.flux.dto.FileUploadResponse;
import com.gro4t.flux.mapper.FileMapper;
import com.gro4t.flux.model.FileMetadata;
import com.gro4t.flux.repository.FileMetadataRepository;
import com.gro4t.flux.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest
public class FileServiceTests {
    @Autowired
    private FileService service;

    @Autowired
    private FileMapper mapper;

    @MockitoBean
    private FileMetadataRepository fileMetadataRepository;

    @MockitoBean
    private Storage blobStorage;

    @Test
    public void testGetFiles() {
        // Given
        when(fileMetadataRepository.findAll()).thenReturn(
                List.of(
                        new FileMetadata(
                                "1", "document.pdf", 1024,
                                "application/pdf", "user123", FileStatus.UPLOADED
                        )
                )
        );

        // When
        var files = service.getFiles();

        // Then
        assertEquals(
                List.of(new FileDto("document.pdf", 1024, "application/pdf", "user123")),
                files
        );
    }

    @Test
    public void testUploadFile() throws Exception {
        // Given
        String fileName = "test-file.txt";
        String expectedSignedUrl = "http://mocked-upload-url.com/test-file.txt";

        // Mock the behaviour of fileMetadataRepository
        when(fileMetadataRepository.findByName(fileName)).thenReturn(Collections.emptyList());

        // Mock the behaviour of blobStorage.signUrl
        when(blobStorage.signUrl(
                any(BlobInfo.class),
                any(Long.class),
                any(TimeUnit.class),
                any(Storage.SignUrlOption.class), // For httpMethod
                any(Storage.SignUrlOption.class), // For withExtHeaders
                any(Storage.SignUrlOption.class)  // For withV4Signature
        )).thenReturn(new URL(expectedSignedUrl));

        // When
        FileUploadResponse response = service.uploadFile(fileName);

        // Then
        assertEquals(expectedSignedUrl, response.getUploadUrl());
    }

    @Test
    public void testUploadFileWhenFileAlreadyExists() {
        // Given
        String fileName = "document.pdf";

        // Mock the behaviour of fileMetadataRepository
        when(fileMetadataRepository.findByName(fileName)).thenReturn(
                List.of(new FileMetadata("1", fileName, 1024, "application/pdf", "user123", FileStatus.UPLOADED))
        );

        // When
        FileUploadResponse response = service.uploadFile(fileName);

        // Then
        assertEquals("File already exists", response.getErrorMessage());
    }
}
