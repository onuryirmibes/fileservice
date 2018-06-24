package de.exb.platform.cloud.fileservice.model;

import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FileMetadata {
	
	@Id
	private UUID id;
	
	@JsonIgnore
	private Path path;
	
	private String name;
	
	private String extension;
	
	@Version
	private Long version;
	
	@JsonIgnore
	@CreatedDate
	private Date createDate;

	@JsonIgnore
	@LastModifiedDate
	private Date updateDate;
	
	/**
	 *  createdBy & lastModifiedBy works with keyCloak
	 */
	
//	@JsonIgnore
//	@CreatedBy
//	private String createdBy;
//
//	@JsonIgnore
//	@LastModifiedBy
//	private String lastModifiedBy;
	
	public FileMetadata() {
		this.id = UUID.randomUUID();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	public Path getPath() {
		return path;
	}
	
	public void setPath(Path path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
