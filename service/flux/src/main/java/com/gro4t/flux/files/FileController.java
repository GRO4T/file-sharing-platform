package com.gro4t.flux.files;

import com.gro4t.flux.files.exception.FluxFileAlreadyExistsException;
import com.gro4t.flux.files.exception.FluxFileNotFoundException;
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
    @ResponseStatus(HttpStatus.OK)
    public List<FileDto> getFiles() {
        return fileService.getFiles();
    }

    @PostMapping
    public ResponseEntity<FileUploadResponse> addFile(@RequestBody FileUploadRequest fileUploadRequest) {
        var status = HttpStatus.CREATED;
        var body = FileUploadResponse.builder();

        try {
            var uploadUrl = fileService.addFile(fileUploadRequest.getName());
            body.uploadUrl(uploadUrl);
        } catch (FluxFileAlreadyExistsException e) {
            status = HttpStatus.BAD_REQUEST;
            body.errorMessage(e.getMessage());
        }

        return ResponseEntity
                .status(status)
                .body(body.build());
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<FileDto> registerFileUploaded(@PathVariable("id") String fileId) {
        try {
            var response = fileService.registerFileUploaded(fileId);
            return ResponseEntity.ok(response);
        } catch (FluxFileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
