package com.onetech.intelinfo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onetech.intelinfo.service.FileParserService;
@RestController
@CrossOrigin
public class FileController {
	
	
	FileParserService fileParserService = new FileParserService();

	@RequestMapping(value="/uploads.htm", method=RequestMethod.POST)
	public @ResponseBody void handleFileUpload(
			@RequestParam(value="file", required=true) MultipartFile file) throws Exception{

		BufferedOutputStream outStream = null;
		byte[] fileBytes = file.getBytes();
		System.out.println(file.getOriginalFilename());
		String rootpath = System.getProperty("catalina.home");
		File dir = new File(rootpath +File.separator + "tmpFiles");
		if(!dir.exists()){
			dir.mkdirs();
		}
		System.out.println(dir);
		File serrverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
		try {
			outStream = new BufferedOutputStream(new FileOutputStream(serrverFile));
			outStream.write(fileBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		outStream.close();
		System.out.println(serrverFile);
		fileParserService.parseExcelFile(serrverFile);

	}

	@RequestMapping(value="/download.htm", method=RequestMethod.POST)
	public @ResponseBody void handleFileDownload(@RequestParam(value="category") String name,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("In download controller");
		System.out.println(name);
		fileParserService.fromDbTOExcel();
		
		
	}


}
