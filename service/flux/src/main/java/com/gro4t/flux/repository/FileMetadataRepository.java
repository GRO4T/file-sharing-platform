package com.gro4t.flux.repository;

import com.gro4t.flux.model.FileMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMetadataRepository extends MongoRepository<FileMetadata, String> {
    List<FileMetadata> findByName(String name);
}
