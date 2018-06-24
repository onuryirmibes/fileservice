package de.exb.platform.cloud.fileservice.dao;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import de.exb.platform.cloud.fileservice.exception.NoDataFound;
import de.exb.platform.cloud.fileservice.model.FileMetadata;
import de.exb.platform.cloud.fileservice.repository.FileMetadataRepository;

public class FileMetadataDao {
	
	@Autowired
	private FileMetadataRepository repository;

	public List<FileMetadata> findAll() {
		return repository.findAll();
	}

	public FileMetadata findOne(UUID id) {
		FileMetadata result = repository.findOne(id);
		if(result == null) {
			throw new NoDataFound("No File found given id : " + id.toString());
		}
		return result;
	}
	
	public FileMetadata save(FileMetadata entity) {
		return repository.save(entity);
	}
	
	public void delete(UUID entityId) {
		repository.delete(entityId);
	}
	
	public void delete(FileMetadata entity) {
		repository.delete(entity);
	}

	public List<FileMetadata> filter(FileMetadata fileMetadata) {
		// Filtering logic here:
		return Collections.EMPTY_LIST;
	}
}
