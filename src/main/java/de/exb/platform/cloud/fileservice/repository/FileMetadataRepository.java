package de.exb.platform.cloud.fileservice.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.exb.platform.cloud.fileservice.model.FileMetadata;

public interface FileMetadataRepository extends MongoRepository<FileMetadata, UUID>{

}
