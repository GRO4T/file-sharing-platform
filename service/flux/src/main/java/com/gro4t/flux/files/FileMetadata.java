package com.gro4t.flux.files;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileMetadata {
    @Indexed(unique = true)
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @PositiveOrZero(message = "Size cannot be negative")
    private int size;
    // TODO: Create custom validator
    private String mimeType;
    private String uploadedBy;
    private Status status;

    public enum Status {
        UPLOADING,
        UPLOADED
    }
}

