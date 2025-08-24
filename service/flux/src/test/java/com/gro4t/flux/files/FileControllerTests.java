package com.gro4t.flux.files;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gro4t.flux.files.dto.FileDto;
import com.gro4t.flux.files.dto.FileUploadRequest;
import com.gro4t.flux.files.dto.FileUploadResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FileController.class)
@WithMockUser
class FileControllerTests {
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private FileService service;

  @Test
  public void testGetFiles() throws Exception {
    when(service.getFiles())
        .thenReturn(
            List.of(
                new FileDto(
                    "123",
                    "document.pdf",
                    1024,
                    "application/pdf",
                    "user123",
                    FileMetadata.Status.UPLOADED.toString())));

    this.mockMvc
        .perform(get("/api/v1/files"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is("123")))
        .andExpect(jsonPath("$[0].name", is("document.pdf")))
        .andExpect(jsonPath("$[0].size", is(1024)))
        .andExpect(jsonPath("$[0].type", is("application/pdf")))
        .andExpect(jsonPath("$[0].uploadedBy", is("user123")))
        .andExpect(jsonPath("$[0].status", is("UPLOADED")));
  }

  @Test
  public void testAddFile() throws Exception {
    FileUploadRequest request = new FileUploadRequest("document.pdf");
    String requestSerialized = objectMapper.writeValueAsString(request);
    String mockUploadUrl = "https://google.cloud.storage.com/ABC123";

    when(service.addFile("document.pdf"))
        .thenReturn(FileUploadResponse.builder().id("123").uploadUrl(mockUploadUrl).build());

    this.mockMvc
        .perform(
            post("/api/v1/files")
                .contentType("application/json")
                .content(requestSerialized)
                .with(jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is("123")))
        .andExpect(jsonPath("$.uploadUrl", is(mockUploadUrl)));
  }

  @Test
  public void testNotifyFileUploaded() throws Exception {
    String fileId = "08321080fdsa";

    when(service.notifyFileUploaded(fileId))
        .thenReturn(
            new FileDto(
                fileId,
                "document.pdf",
                1024,
                "application/pdf",
                "user123",
                FileMetadata.Status.UPLOADED.toString()));

    this.mockMvc
        .perform(post("/api/v1/files/" + fileId + "/upload").with(jwt()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", is("UPLOADED")));
  }

  @Test
  public void testGetDownloadUrl() throws Exception {
    String fileId = "021840810";

    when(service.getDownloadUrl(fileId)).thenReturn("https://mock-download-url.com/document.pdf");

    this.mockMvc.perform(get("/api/v1/files/" + fileId + "/download")).andExpect(status().isOk());
  }
}
