package com.onetech.intelinfo.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
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

		//BufferedOutputStream outStream = null;
		//byte[] fileBytes = file.getBytes();
		System.out.println(file.getOriginalFilename());
		/*String rootpath = System.getProperty("catalina.home");
		File dir = new File(rootpath +File.separator + "tmpFiles");
		if(!dir.exists()){
			dir.mkdirs();
		}
		System.out.println(dir);
		File serrverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename().trim());
		try {
			outStream = new BufferedOutputStream(new FileOutputStream(serrverFile));
			outStream.write(fileBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		outStream.close();
		System.out.println(serrverFile);*/
		fileParserService.parseExcelFile(file);

	}

	@RequestMapping(value="/download.htm", method=RequestMethod.GET)
	public @ResponseBody void handleFileDownload(@RequestParam(value="category") String name,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("In download controller");
		System.out.println(name);
		File file = null;
		file = fileParserService.fromDbTOExcel(name);
		
		
		if(!file.exists()){
			String errorMessage = "Sorry. The file you are looking for does not exist";
			System.out.println(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}
		
		String mimeType= URLConnection.guessContentTypeFromName(file.getName());
		if(mimeType==null){
			System.out.println("mimetype is not detectable, will take default");
			mimeType = "application/octet-stream";
		}
		
		System.out.println("mimetype : "+mimeType);
		
        response.setContentType(mimeType);
        
        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() +"\""));

        
        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        
        response.setContentLength((int)file.length());

		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
		
		
	}


}
