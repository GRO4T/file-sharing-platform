package com.gro4t.flux.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "http://localhost:5173")
public class FileController {
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

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
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
