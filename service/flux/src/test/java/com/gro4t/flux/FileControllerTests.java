package com.gro4t.flux;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gro4t.flux.files.*;
import com.gro4t.flux.files.exception.FluxFileAlreadyExistsException;
import com.gro4t.flux.files.exception.FluxFileNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
class FileControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FileService service;

    @Test
    public void testGetFilesShouldAlwaysSucceed() throws Exception {
        when(service.getFiles())
                .thenReturn(List.of(new FileDto("document.pdf", 1024, "application/pdf", "user123", FileMetadata.Status.UPLOADED)));

        this.mockMvc.perform(get("/files"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("document.pdf")))
                .andExpect(jsonPath("$[0].size", is(1024)))
                .andExpect(jsonPath("$[0].type", is("application/pdf")))
                .andExpect(jsonPath("$[0].uploadedBy", is("user123")))
                .andExpect(jsonPath("$[0].status", is("UPLOADED")));
    }

    @Test
    public void testAddFileWhenFileNotExistShouldReturnAddUrl() throws Exception {
        FileUploadRequest request = new FileUploadRequest("document.pdf");
        String requestSerialized = objectMapper.writeValueAsString(request);
        String mockUploadUrl = "https://google.cloud.storage.com/ABC123";

        when(service.addFile("document.pdf")).thenReturn(mockUploadUrl);

        this.mockMvc.perform(post("/files")
                        .contentType("application/json")
                        .content(requestSerialized))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uploadUrl", is(mockUploadUrl)));
    }

    @Test
    public void testAddFileWhenFileExistShouldReturnBadRequest() throws Exception {
        FileUploadRequest request = new FileUploadRequest("document.pdf");
        String requestSerialized = objectMapper.writeValueAsString(request);

        when(service.addFile("document.pdf")).thenThrow(new FluxFileAlreadyExistsException());

        this.mockMvc.perform(post("/files")
                        .contentType("application/json")
                        .content(requestSerialized))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage", is("File already exists")));
    }

    @Test
    public void testRegisterFileUploaded() throws Exception {
        String fileId = "08321080fdsa";

        when(service.registerFileUploaded(fileId))
                .thenReturn(new FileDto("document.pdf", 1024, "application/pdf", "user123", FileMetadata.Status.UPLOADED));

        this.mockMvc.perform(post("/files/" + fileId + "/upload"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UPLOADED")));
    }

    @Test
    public void testRegisterFileUploadedWhenFileNotFound() throws Exception {
        String fileId = "08321080fdsa";

        when(service.registerFileUploaded(fileId)).thenThrow(new FluxFileNotFoundException());

        this.mockMvc.perform(post("/files/" + fileId + "/upload")).andExpect(status().isNotFound());
    }
}
