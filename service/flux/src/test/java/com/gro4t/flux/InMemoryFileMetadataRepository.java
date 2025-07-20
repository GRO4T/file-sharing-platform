package com.gro4t.flux;

import com.gro4t.flux.files.FileMetadata;
import com.gro4t.flux.files.FileMetadataRepository;

import java.util.List;

import static java.util.Objects.requireNonNull;

class InMemoryFileMetadataRepository extends InMemoryRepository<FileMetadata> implements FileMetadataRepository {
    public FileMetadata save(FileMetadata fileMetadata) {
        requireNonNull(fileMetadata);
        map.put(fileMetadata.getName(), fileMetadata);
        return fileMetadata;
    }

    public List<FileMetadata> findByName(String name) {
        if (!map.containsKey(name)) {
            return List.of();
        }
        return List.of(map.get(name));
    }

    public void delete(String name) {
        map.remove(name);
    }

    public List<FileMetadata> findAll() {
        return map.values().stream().toList();
    }
}
