package com.gro4t.flux.files;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Mapping(source = "mimeType", target = "type")
    FileDto fileMetadataToFileDto(FileMetadata fileMetadata);
}
