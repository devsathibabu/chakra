package com.onetech.intelinfo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.onetech.intelinfo.model.Document;

@Controller
public class FileController {
	
	@RequestMapping(value="myfileupload", method=RequestMethod.POST)
	public ModelAndView handleFileUpload(@ModelAttribute Document document,@RequestParam("file") MultipartFile file,
			HttpServletRequest request,HttpServletResponse response){
			
			
			ModelAndView model = new ModelAndView("helloword");
			String myfile = file.getOriginalFilename();
			
			String message = "the file is with server "+myfile;
		model.addObject("message",message);
		model.addObject("file", myfile);
		return model;
	}
}
