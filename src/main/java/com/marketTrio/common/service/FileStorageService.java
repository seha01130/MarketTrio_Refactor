package com.marketTrio.common.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

	@Value("${file.upload-dir}")
	private String uploadDir;

	public List<String> storeFiles(MultipartFile[] files) throws IOException {
		List<String> uploadedFileNames = new ArrayList<>();
		if (files == null || files.length == 0) {
			return uploadedFileNames;
		}

		Path directory = Paths.get(uploadDir).toAbsolutePath().normalize();
		Files.createDirectories(directory);

		for (MultipartFile file : files) {
			String originalName = file.getOriginalFilename();
			if (originalName == null || originalName.isBlank()) {
				continue;
			}

			String cleanedName = StringUtils.cleanPath(originalName);
			String storedName = UUID.randomUUID() + "_" + cleanedName;

			Path targetLocation = directory.resolve(storedName);
			file.transferTo(targetLocation.toFile());
			uploadedFileNames.add(storedName);
		}

		return uploadedFileNames;
	}
}

