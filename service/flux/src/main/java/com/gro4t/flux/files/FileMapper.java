package com.gro4t.flux.files;

import com.gro4t.flux.files.dto.FileDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
interface FileMapper {
  FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

  @Mapping(source = "mimeType", target = "type")
  FileDto fileMetadataToFileDto(FileMetadata fileMetadata);
}
