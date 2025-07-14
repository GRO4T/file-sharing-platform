package com.gro4t.flux.mapper;

import com.gro4t.flux.dto.FileDto;
import com.gro4t.flux.model.FileMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Mapping(source = "mimeType", target = "type")
    FileDto fileMetadataToFileDto(FileMetadata fileMetadata);
}
