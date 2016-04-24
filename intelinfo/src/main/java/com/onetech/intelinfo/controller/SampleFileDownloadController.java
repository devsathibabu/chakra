package com.onetech.intelinfo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

public class SampleFileDownloadController {
	
/*
	    @RequestMapping(value = "download.htm", method = RequestMethod.GET)
	    public void download(@RequestParam ("fileName") String name, final HttpServletRequest request, final HttpServletResponse response) throws Exception {

	    	InputStream inputStream = null;
	    	
	    	System.out.println(name);
	        File file = new File ("D:/Biotechnology1.xls");
	        try {
	        	
	        	InputStream fileInputStream = new FileInputStream(file);
                OutputStream output = response.getOutputStream();

	            response.reset();

	            response.setContentType("application/octet-stream");
	            response.setContentLength((int) (file.length()));

	            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

	            IOUtils.copyLarge(fileInputStream, output);
	            output.flush();
	        } catch (IOException e) {
	           e.printStackTrace();
	        }

	    }

*/
}
