package com.gro4t.flux.files;

import com.gro4t.flux.InMemoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

class InMemoryFileMetadataRepository extends InMemoryRepository<FileMetadata> implements FileMetadataRepository {
    public FileMetadata save(FileMetadata fileMetadata) {
        requireNonNull(fileMetadata);
        if (fileMetadata.getId() == null) {
            fileMetadata.setId(UUID.randomUUID().toString());
        }
        map.put(fileMetadata.getId(), fileMetadata);
        return fileMetadata;
    }

    public List<FileMetadata> findByName(String name) {
        var file = map.entrySet().stream().filter(x -> x.getValue().getName().equals(name)).findAny();
        if (file.isEmpty()) {
            return List.of();
        }
        return List.of(file.get().getValue());
    }

    public Optional<FileMetadata> findById(String id) {
        if (!map.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(map.get(id));
    }

    public void delete(String id) {
        map.remove(id);
    }

    public List<FileMetadata> findAll() {
        return map.values().stream().toList();
    }
}
