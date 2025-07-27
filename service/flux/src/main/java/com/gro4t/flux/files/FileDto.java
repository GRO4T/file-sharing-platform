package com.gro4t.flux.files;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDto {
    private String name;
    private int size;
    private String type;
    private String uploadedBy;
    private FileMetadata.Status status;
}
