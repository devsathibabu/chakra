package com.onetech.intelinfo.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.onetech.intelinfo.model.Document;
import com.onetech.intelinfo.service.FileParserService;
@RestController
@CrossOrigin
public class FileController {

	FileParserService fileParserService = new FileParserService();

	@RequestMapping(value="/uploads.htm", method=RequestMethod.POST)
	public @ResponseBody void handleFileUpload(
			@RequestParam(value="file", required=true) MultipartFile file) throws IOException {
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
	public void handleFileUpload(@RequestParam(value="category") String name,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		System.out.println("In download controller");
		System.out.println(name);

		ServletContext servletContext = request.getServletContext();

		String rootFilePath = System.getProperty("catalina.home");
		File downloadFile = new File(rootFilePath+File.separator+"tmpFiles"+File.separator+"Analytics.xls");
		FileInputStream inputStream = new FileInputStream(downloadFile);

		String mimeType = servletContext.getMimeType(rootFilePath);

		if(mimeType == null ){
			mimeType = "application/octet-stream";
		}

		response.setContentType(mimeType);
		response.setContentLength((int)downloadFile.length());

		OutputStream outputStream = response.getOutputStream();

		byte[] buffer = new byte[1000000000];
		int bytesRead = -1;

		try {
			while ((bytesRead = inputStream.read(buffer)) != -1){
				outputStream.write(buffer,0,bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			inputStream.close();
			outputStream.close();
		}

		
	}



}
