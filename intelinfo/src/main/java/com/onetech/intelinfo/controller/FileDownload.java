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

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

@Controller
public class FileDownload {

	@RequestMapping(value="/template.htm", method=RequestMethod.GET)
	public void handleTemplateDownload(@RequestParam(value="category")final String fileName,HttpServletRequest request, HttpServletResponse response) throws IOException{
		System.out.println("In template controller");
		//System.out.println(name);
		
		System.out.println(fileName);
		
		MongoClient mongoClient = new MongoClient();
		DB mongoDB = mongoClient.getDB("Template");
		GridFS gridfs = new GridFS(mongoDB);
		GridFSDBFile grisFsdownloadFile = null;
		try {
		 grisFsdownloadFile = gridfs.findOne(fileName+".xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + grisFsdownloadFile.getFilename() +"\""));

        String mimeType = "application/octet-stream";
        response.setContentType(mimeType);
        
        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", grisFsdownloadFile.getFilename()));
        
        response.setContentLength((int)grisFsdownloadFile.getLength());

		InputStream inputStream = grisFsdownloadFile.getInputStream();

        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());

	}
}


