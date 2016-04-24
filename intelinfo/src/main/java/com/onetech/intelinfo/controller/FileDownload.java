package com.onetech.intelinfo.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;

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

	@RequestMapping(value="/template.htm", method=RequestMethod.GET)
	public void handleTemplateDownload(@RequestParam(value="category")final String fileName,HttpServletRequest request, HttpServletResponse response) throws IOException{
		System.out.println("In template controller");
		//System.out.println(name);
		
		System.out.println(fileName);
		
		String rootDirectory = System.getProperty("catalina.home");
		String pathToFile = rootDirectory+File.separator+"templateFiles";
		
		File directory = new File(pathToFile);
		
		//File file = new File("D:/Biotechnology1.xls");
		//String name = request.getParameter("templateName");
		//System.out.println(name);
		
		FilenameFilter fileNameFilter = new FilenameFilter() {
			
			public boolean accept(File dir, String name) {
				return name.startsWith(fileName);
			}
		};
		
		String[] childern = directory.list(fileNameFilter);
		String realFileName = null;
		
		if(childern == null){
			System.out.println("File Not Found in Directory");
		}else{
			for(int i=0;i < childern.length;i++){
				realFileName = childern[i];
				System.out.println(i);
				System.out.println(realFileName);
			}
		}
		
		File downloadFile = new File(realFileName);
		System.out.println("the file is"+downloadFile.getName());
		System.out.println("the file path is "+downloadFile.getAbsolutePath());
		
		
		boolean isFileInServer = downloadFile.exists();
		System.out.println("Status of file"+isFileInServer);
		
		if(!downloadFile.exists()){
			String errorMessage = "Sorry. The file you are looking for does not exist";
			System.out.println(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}
		
		String mimeType= URLConnection.guessContentTypeFromName(downloadFile.getName());
		if(mimeType==null){
			System.out.println("mimetype is not detectable, will take default");
			mimeType = "application/octet-stream";
		}
		
		System.out.println("mimetype : "+mimeType);
		
        response.setContentType(mimeType);
        
        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + downloadFile.getName() +"\""));

        
        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        
        response.setContentLength((int)downloadFile.length());

		InputStream inputStream = new BufferedInputStream(new FileInputStream(downloadFile));

        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());

	}
}


