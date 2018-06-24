package de.exb.platform.cloud.fileservice.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
	private String uploadDirectory;

	public String getUploadDir() {
		return uploadDirectory;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDirectory = uploadDir;
	}
}
