package de.exb.platform.cloud.fileservice.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import de.exb.platform.cloud.fileservice.dao.FileMetadataDao;
import de.exb.platform.cloud.fileservice.exception.FileStorageException;
import de.exb.platform.cloud.fileservice.exception.NoDataFound;
import de.exb.platform.cloud.fileservice.model.FileMetadata;
import de.exb.platform.cloud.fileservice.property.FileStorageProperties;

@Component
public class FileController {

	private final Path fileStorageLocation;

	@Autowired
	private FileMetadataDao fileMetadataDao;

	@Autowired
	public FileController(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	public ResponseEntity<Resource> downloadFile(UUID fileId, HttpServletRequest request) {

		Resource resource = loadFileAsResource(fileId.toString());
		String contentType = getFileContentType(request, resource);
		String contentDispositionValue = "attachment; filename=\"" + resource.getFilename() + "\"";

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDispositionValue).body(resource);
	}

	private String getFileContentType(HttpServletRequest request, Resource resource) {
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			// logger.info("Could not determine file type.");
		}

		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return contentType;
	}

	private Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new NoDataFound("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new NoDataFound("File not found " + fileName, ex);
		}
	}

	public List<FileMetadata> uploadMultipleFiles(MultipartFile[] fileArray) {
		return Arrays.asList(fileArray).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
	}

	public FileMetadata uploadFile(MultipartFile file) {

		FileMetadata fileMetadata = createAndSaveFileMetadata(file);

		try {
			Files.copy(file.getInputStream(), fileMetadata.getPath(), StandardCopyOption.REPLACE_EXISTING);
			return fileMetadata;
		} catch (IOException ex) {
			throw new FileStorageException(
					"Could not store file " + fileMetadata.getId().toString() + ". Please try again!", ex);
		}
	}

	private FileMetadata createAndSaveFileMetadata(MultipartFile file) {
		FileMetadata fileMetadata = new FileMetadata();

		String extension = getFileExtension(file);
		fileMetadata.setExtension(extension);

		fileMetadata.setName(file.getOriginalFilename());
		String uniqueFileName = fileMetadata.getId().toString();
		String fileName = StringUtils.cleanPath(uniqueFileName);
		Path targetLocation = this.fileStorageLocation.resolve(fileName);
		fileMetadata.setPath(targetLocation);

		return fileMetadataDao.save(fileMetadata);
	}

	private String getFileExtension(MultipartFile file) {

		String fileName = file.getOriginalFilename();
		int i = fileName.lastIndexOf('.');

		if (i > 0) {
			return fileName.substring(i + 1);
		}
		return "";
	}

	public void deleteFile(UUID id) {

		FileMetadata fileMetadata = fileMetadataDao.findOne(id);
		fileMetadataDao.delete(fileMetadata);

		try {
			Files.delete(fileMetadata.getPath());
		} catch (IOException ex) {
			fileMetadataDao.save(fileMetadata);
			throw new FileStorageException(
					"Could not store file " + fileMetadata.getId().toString() + ". Please try again!", ex);
		}
	}
}
