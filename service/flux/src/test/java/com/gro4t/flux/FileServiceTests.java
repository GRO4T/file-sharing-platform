package com.gro4t.flux;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.gro4t.flux.files.FileDto;
import com.gro4t.flux.files.FileMetadata;
import com.gro4t.flux.files.FileService;
import com.gro4t.flux.files.exception.FluxFileAlreadyExistsException;
import com.gro4t.flux.files.exception.FluxFileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
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
        fileMetadataRepository.save(new FileMetadata("1", "test_file.txt", 1024, "text/plain", "user123",
                FileMetadata.Status.UPLOADED));
        fileMetadataRepository.save(new FileMetadata("2", "test_file2.txt", 1024, "text/plain", "user123",
                FileMetadata.Status.UPLOADED));

        // When
        var files = fileService.getFiles();

        // Then
        assertEquals(2, files.size());
        assertTrue(files.contains(new FileDto("test_file.txt", 1024, "text/plain", "user123", FileMetadata.Status.UPLOADED)));
        assertTrue(files.contains(new FileDto("test_file2.txt", 1024, "text/plain", "user123", FileMetadata.Status.UPLOADED)));
    }

    @Test
    public void testAddFile() throws Exception {
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
        var uploadUrl = fileService.addFile(fileName);

        // Then
        assertEquals(expectedSignedUrl, uploadUrl);
    }

    @Test
    public void testAddFileWhenFileAlreadyExists() {
        // Given
        String fileName = "document.pdf";
        fileMetadataRepository.save(new FileMetadata("1", fileName, 1024, "application/pdf", "user123",
                FileMetadata.Status.UPLOADED));

        // When and Then
        assertThrows(FluxFileAlreadyExistsException.class, () -> {
            fileService.addFile(fileName);
        });
    }

    @Test
    public void testRegisterFileUploaded() {
        // Given
        String fileId = "401280f8dsa08";
        String fileName = "document.pdf";
        fileMetadataRepository.save(new FileMetadata(fileId, fileName, 1024, "application/pdf", "user123",
                FileMetadata.Status.UPLOADING));

        // When
        var response = fileService.registerFileUploaded(fileId);

        // Then
        assertEquals(FileMetadata.Status.UPLOADED, response.getStatus());
    }

    @Test
    public void testRegisterFileUploadedWhenFileNotExist() {
        // Given
        String fileId = "401280f8dsa08";

        // When and Then
        assertThrows(FluxFileNotFoundException.class, () -> {
            fileService.registerFileUploaded(fileId);
        });
    }
}
