package com.gro4t.flux.controller;

import com.gro4t.flux.dto.FileDto;
import com.gro4t.flux.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/files")
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

}
