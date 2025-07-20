package com.gro4t.flux;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.gro4t.flux.files.FileDto;
import com.gro4t.flux.files.FileMetadata;
import com.gro4t.flux.files.FileService;
import com.gro4t.flux.files.FileUploadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
class FileServiceTests {
    @Autowired
    private ApplicationProperties applicationProperties;

    @MockitoBean
    private Storage blobStorage;

    private InMemoryFileMetadataRepository fileMetadataRepository;
    private FileService fileService;

    @BeforeEach
    void setUp() {
        fileMetadataRepository = new InMemoryFileMetadataRepository();
        fileService = new SystemConfiguration(applicationProperties).fileService(fileMetadataRepository, blobStorage);
    }

    @Test
    public void testGetFiles() {
        // Given
        fileMetadataRepository.save(new FileMetadata("test_file.txt", 1024, "text/plain", "user123",
                FileMetadata.Status.UPLOADED));
        fileMetadataRepository.save(new FileMetadata("test_file2.txt", 1024, "text/plain", "user123",
                FileMetadata.Status.UPLOADED));

        // When
        var files = fileService.getFiles();

        // Then
        assertEquals(2, files.size());
        assertTrue(files.contains(new FileDto("test_file.txt", 1024, "text/plain", "user123")));
        assertTrue(files.contains(new FileDto("test_file2.txt", 1024, "text/plain", "user123")));
    }

    @Test
    public void testUploadFile() throws Exception {
        // Given
        String fileName = "test-file.txt";
        String expectedSignedUrl = "http://mocked-upload-url.com/test-file.txt";

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
        FileUploadResponse response = fileService.uploadFile(fileName);

        // Then
        assertEquals(expectedSignedUrl, response.getUploadUrl());
    }

    @Test
    public void testUploadFileWhenFileAlreadyExists() {
        // Given
        String fileName = "document.pdf";
        fileMetadataRepository.save(new FileMetadata(fileName, 1024, "application/pdf", "user123",
                FileMetadata.Status.UPLOADED));

        // When
        FileUploadResponse response = fileService.uploadFile(fileName);

        // Then
        assertEquals("File already exists", response.getErrorMessage());
    }
}
