package com.gro4t.flux.controller;

import com.gro4t.flux.dto.FileDto;
import com.gro4t.flux.dto.FileUploadRequest;
import com.gro4t.flux.dto.FileUploadResponse;
import com.gro4t.flux.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class FileController {
    @Autowired
    private final FileService fileService;

    @GetMapping
    public ResponseEntity<List<FileDto>> getFiles() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileService.getFiles());
    }

    @PostMapping
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestBody FileUploadRequest fileUploadRequest) {
        var response = fileService.uploadFile(fileUploadRequest.getName());

        if (response.getErrorMessage() != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
