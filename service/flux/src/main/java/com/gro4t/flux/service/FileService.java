package com.gro4t.flux.service;

import com.gro4t.flux.dto.FileDto;
import com.gro4t.flux.mapper.FileMapper;
import com.gro4t.flux.model.FileMetadata;
import com.gro4t.flux.repository.FileMetadataRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FileService {
    @Autowired
    private final FileMetadataRepository fileMetadataRepository;
    @Autowired
    private final FileMapper fileMapper;

    public List<FileDto> getFiles() {
        var newFileMetadata = FileMetadata.builder()
                .name("test_file")
                .size(123)
                .mimeType("application/json")
                .url("http://example.com").build();
        fileMetadataRepository.save(newFileMetadata);
        return fileMetadataRepository.findAll()
                .stream()
                .map(fileMapper::fileMetadataToFileDto)
                .toList();
    }
}
