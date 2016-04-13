package com.onetech.intelinfo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Blob;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.onetech.intelinfo.model.Document;

@Controller
public class FileController {
	
	@RequestMapping(value="myfileupload", method=RequestMethod.POST)
	public ModelAndView handleFileUpload(@ModelAttribute Document document,@RequestParam("file") MultipartFile file,
			HttpServletRequest request,HttpServletResponse response){
			
			
			ModelAndView model = new ModelAndView("helloword");
			String myfile = file.getOriginalFilename();
			/*FileInputStream inputStream;
			
			try {
				inputStream = new FileInputStream(myfile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			*/
			String message = "the file is with server"+myfile;
		model.addObject("message",message);
		model.addObject("file", myfile);
		return model;
	}
}
