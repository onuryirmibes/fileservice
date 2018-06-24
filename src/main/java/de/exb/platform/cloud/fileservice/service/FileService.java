package de.exb.platform.cloud.fileservice.service;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.exb.platform.cloud.fileservice.controller.FileController;
import de.exb.platform.cloud.fileservice.model.FileMetadata;


@CrossOrigin
@RestController
@EnableAutoConfiguration
@RequestMapping("server/v1/files")
public class FileService {
	
	private static final Logger LOGGER = LogManager.getLogger(FileService.class);
	
	@Autowired
	private FileController controller;
		
    @PostMapping("/uploadFile")
    public FileMetadata uploadFile(@Valid @RequestParam("file") MultipartFile file) {
    	LOGGER.info("Upload file Initiated. File Name : " + file.getOriginalFilename());
        return controller.uploadFile(file);
    }

    @PostMapping("/uploadMultipleFiles")
    public List<FileMetadata> uploadMultipleFiles(@Valid @RequestParam("files") MultipartFile[] files) {
    	LOGGER.info("Upload Multiple Files Initiated");
    	return controller.uploadMultipleFiles(files);
    }
    
    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID fileId, HttpServletRequest request) {
    	LOGGER.info("Download File Initiated. File Id : " + fileId);
    	return controller.downloadFile(fileId, request);
    }
    
    @DeleteMapping("/deleteFile/{id}")
    public void deleteFile(@PathVariable UUID id, HttpServletRequest request) {
    	LOGGER.info("Delete File Initiated. File Id : " + id.toString());
    	controller.deleteFile(id);
    }
}
