package com.gro4t.flux.files;

import com.gro4t.flux.files.dto.FileDto;
import com.gro4t.flux.files.dto.FileUploadRequest;
import com.gro4t.flux.files.dto.FileUploadResponse;
import com.gro4t.flux.files.exception.FluxFileAlreadyExistsException;
import com.gro4t.flux.files.exception.FluxFileNotFoundException;
import com.gro4t.flux.files.exception.FluxFileNotUploadedException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "http://localhost:5173")
class FileController {
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
  public ResponseEntity<FileUploadResponse> addFile(
      @RequestBody FileUploadRequest fileUploadRequest) {
    try {
      var response = fileService.addFile(fileUploadRequest.getName());
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (FluxFileAlreadyExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @PostMapping("/{id}/upload")
  public ResponseEntity<FileDto> notifyFileUploaded(@PathVariable("id") String fileId) {
    try {
      var response = fileService.notifyFileUploaded(fileId);
      return ResponseEntity.ok(response);
    } catch (FluxFileNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<String> getDownloadUrl(@PathVariable("id") String fileId) {
    try {
      var downloadUrl = fileService.getDownloadUrl(fileId);
      return ResponseEntity.status(HttpStatus.OK).body(downloadUrl);
    } catch (FluxFileNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (FluxFileNotUploadedException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }
}
