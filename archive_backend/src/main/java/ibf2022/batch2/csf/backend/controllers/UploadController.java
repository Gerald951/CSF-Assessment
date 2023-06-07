package ibf2022.batch2.csf.backend.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.batch2.csf.backend.Utils;
import ibf2022.batch2.csf.backend.repositories.ArchiveRepository;
import ibf2022.batch2.csf.backend.repositories.ImageRepository;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@Controller
public class UploadController {

	@Autowired
	private ImageRepository imageRepo;

	@Autowired
	private ArchiveRepository archiveRepository;

	@PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> upload(@RequestPart MultipartFile archive, @RequestPart String name,
			@RequestPart String title, @RequestPart String comment) throws IOException {

				System.out.println("HELPPPPP");

		try {
			// Creating a File object for the directory path
			File directoryPath = new File(
					"zipfile/temp");

			boolean fileExists = true;

			while (fileExists) {
				if (directoryPath.exists()) {
					System.out.println("DIRECTORY EXISTSSSS");
					fileExists = false;
				} else {
					directoryPath.mkdirs();
					System.out.println("DIRECTORY CREATEDDDDD");
					fileExists = false;
				}
			}

			String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
			String fileName = date + archive.getOriginalFilename();

			// folderPath
			String folderPath = "zipfile/temp";
			String filePath = folderPath + File.separator + fileName;

			// Copies Spring's multipartfile inputStream to /zipfile/temp
			Files.copy(archive.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

			ZipFile zipFile = new ZipFile(filePath);

			ZipInputStream zInput;
			ZipEntry zE;
			zInput = new ZipInputStream(archive.getInputStream());
			List<String> listOfKeys = new LinkedList<>();

			while ((zE = zInput.getNextEntry()) != null) {
				ZipEntry zipEntry = zipFile.getEntry(zE.getName());
				InputStream is = zipFile.getInputStream(zipEntry);
				listOfKeys.add(zE.getName());

				imageRepo.upload(zipEntry, is, name, title, comment);
			}

			zipFile.close();

			List<String> listOfDataURL = new LinkedList<>();
			for (String key : listOfKeys) {
				String dataURL = imageRepo.getDataURL(key);
				listOfDataURL.add(dataURL);
			}

			JsonArrayBuilder built = Json.createArrayBuilder();
			for (int i = 0; i < listOfDataURL.size(); i++) {
				JsonObject jo = Json.createObjectBuilder().add(listOfKeys.get(i), listOfDataURL.get(i)).build();
				built.add(jo);
			}
			JsonArray arrKey = built.build();

			String key = UUID.randomUUID().toString()
					.substring(0, 8);

			archiveRepository.recordBundle(key, date, title, name, comment, arrKey.toString());

			JsonObject json = Json.createObjectBuilder().add("bundleId", key).build();

			return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(json.toString());

		} catch (Exception e) {
			JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();

			return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(err.toString());
		}

		
	}


	@GetMapping(path="/bundle/{bundleId}")
	public ResponseEntity<String> getBundle(@PathVariable(required = true) String bundleId) {
		String result = archiveRepository.getBundleByBundleId(bundleId);

		if (result == null) {
			return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(null);
		} else {
			try {
				JsonObject jo = Utils.stringToJson(result);
				return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(jo.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JsonObject err = Json.createObjectBuilder().add("error", e.getMessage()).build();
				
				return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(err.toString());
			}
		}
		
	}

	@GetMapping(path = "/bundles" , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> retrieveBundles() {
		String retrievedBundles = archiveRepository.getBundles().toString();

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(retrievedBundles);
	}
}
// TODO: Task 2, Task 3, Task 4

// TODO: Task 5

// TODO: Task 6
