package com.onetech.intelinfo.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class FileDownload {

	@RequestMapping(value="/template.htm", method=RequestMethod.POST)
	public void handleTemplateDownload(@RequestParam(value="category") String name,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		System.out.println("In template controller");
		System.out.println(name);
		
		response.setContentType("application/force-download");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		
//		ServletContext servletContext = request.getServletContext();
		String pathToFile = System.getProperty("catalina.home");
		System.out.println(pathToFile);
		File downloadFile = new File(pathToFile+File.separator+"tmpFiles"+File.separator+"Analytics.xls");
		
		
		InputStream inputStream = new BufferedInputStream(new FileInputStream(downloadFile));
		
		
		System.out.println("File location on server is "+downloadFile.getAbsolutePath());
//		String mimeType = servletContext.getMimeType(downloadFile.getName());
//		if(mimeType!= null){
//			response.setContentType(mimeType);
//		}else{
//			response.setContentType("application/force-download");
//		}
		
//		System.out.println(mimeType);
		response.setContentLength((int)downloadFile.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFile.getName() + "\"");
		
		//ServletOutputStream servletOutputStream = response.getOutputStream();
		/*byte[] fileBytes = new byte[1000000000];
		int read = 0;
		while((read = inputStream.read(fileBytes))!= -1){
			servletOutputStream.write(fileBytes);
		}
		servletOutputStream.flush();
		servletOutputStream.close();
		System.out.println("File Downloaded at Client side");*/
		try {
			FileCopyUtils.copy(inputStream, response.getOutputStream());
			System.out.println("File Downloaded");
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("File Download failed");
		}
	}


}


