package com.javasampleapproach.uploadfiles.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.javasampleapproach.uploadfiles.storage.StorageService;

@Controller
public class UploadController {

	@Autowired
	StorageService storageService;

	List<String> files = new ArrayList<String>();

	@CrossOrigin(origins = "*")
	@PostMapping("/post")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.store(file);
			files.add(file.getOriginalFilename());

			message = "You successfully uploaded " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/upload")
	public ResponseEntity<String> handleFileUpload2(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.store(file);
			files.add(file.getOriginalFilename());
			message = "You successfully uploaded " + file.getOriginalFilename() + "!";
			System.out.println("Correcto");
		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			System.out.println("error->" + e.getMessage());
			return new ResponseEntity<String>(message, HttpStatus.EXPECTATION_FAILED);
		}
		System.out.println(message);
		return new ResponseEntity<String>(message, HttpStatus.OK);

		// return message;
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/getAll")
	public ResponseEntity<List<String>> getAll() {
		List<String> fileNames = null;
		try {
			fileNames = storageService.getAllFile();
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<List<String>>(fileNames, HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<List<String>>(fileNames, HttpStatus.OK);
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/getallfiles")
	public ResponseEntity<List<String>> getListFiles() {

		List<String> fileNames = storageService.getAllFile();
		fileNames = fileNames
				.stream().map(fileName -> MvcUriComponentsBuilder
						.fromMethodName(UploadController.class, "getFile", fileName).build().toString())
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(fileNames);
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		System.out.println(filename);
		Resource file = storageService.loadFile(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
}
