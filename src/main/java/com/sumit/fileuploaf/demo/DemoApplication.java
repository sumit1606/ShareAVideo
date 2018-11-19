package com.sumit.fileuploaf.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}



	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity uploadFile(@RequestParam("file")MultipartFile receivedFile) throws IOException {
		File savedFile = new File(System.getProperty("user.dir") + receivedFile.getOriginalFilename());
		savedFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(savedFile);
		fout.write(receivedFile.getBytes());
		fout.close();
		return new ResponseEntity("Uploaded Succesfully", HttpStatus.OK);
	}

    @RequestMapping(value = "/getFile", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> uploadFile(@RequestParam("fileName") String fileName) throws IOException {

        File toSendFile = new File(System.getProperty("user.dir") + fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        Path path = Paths.get(toSendFile.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(toSendFile.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }



}
