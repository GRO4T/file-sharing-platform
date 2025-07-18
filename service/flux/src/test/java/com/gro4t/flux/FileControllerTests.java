package com.gro4t.flux;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gro4t.flux.controller.FileController;
import com.gro4t.flux.dto.FileDto;
import com.gro4t.flux.dto.FileUploadRequest;
import com.gro4t.flux.dto.FileUploadResponse;
import com.gro4t.flux.service.FileService;
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
        when(service.getFiles()).thenReturn(List.of(new FileDto("document.pdf", 1024, "application/pdf", "user123")));

        this.mockMvc.perform(get("/files"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("document.pdf")))
                .andExpect(jsonPath("$[0].size", is(1024)))
                .andExpect(jsonPath("$[0].type", is("application/pdf")))
                .andExpect(jsonPath("$[0].uploadedBy", is("user123")));
    }

    @Test
    public void testUploadFileWhenFileNotExistShouldReturnUploadUrl() throws Exception {
        FileUploadRequest request = new FileUploadRequest("document.pdf");
        String requestSerialized = objectMapper.writeValueAsString(request);
        String mockUploadUrl = "https://google.cloud.storage.com/ABC123";

        when(service.uploadFile("document.pdf")).thenReturn(FileUploadResponse.builder().uploadUrl(mockUploadUrl).build());

        this.mockMvc.perform(
                        post("/files")
                                .contentType("application/json")
                                .content(requestSerialized)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uploadUrl", is(mockUploadUrl)));
    }

    @Test
    public void testUploadFileWhenFileExistShouldReturnBadRequest() throws Exception {
        FileUploadRequest request = new FileUploadRequest("document.pdf");
        String requestSerialized = objectMapper.writeValueAsString(request);

        when(service.uploadFile("document.pdf")).thenReturn(FileUploadResponse.builder().errorMessage("File already exists").build());

        this.mockMvc.perform(
                        post("/files")
                                .contentType("application/json")
                                .content(requestSerialized)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage", is("File already exists")));
    }
}
