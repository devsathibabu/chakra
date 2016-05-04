package com.onetech.intelinfo.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;


@Controller
public class FileTemplate {
	@RequestMapping(value="/template", method=RequestMethod.GET)
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
