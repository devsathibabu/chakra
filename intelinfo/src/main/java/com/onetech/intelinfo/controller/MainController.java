package com.onetech.intelinfo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class MainController {

	@RequestMapping("/welcome.htm")
	public String showMessage(@RequestParam(value = "name", 
	required = false, defaultValue = "World") String name) {
		System.out.println("in controller");
		//String message = "Welcome to Spring MVC!";
		//ModelAndView mv = new ModelAndView("welcome");
		//mv.addObject("message", message);
		//mv.addObject("name", name);
		//return mv;
		String name1 =  "Hello World";
		return name1;
	}
	
	/*@RequestMapping(value="/download",method=RequestMethod.POST)
	public ModelAndView postMethod(@RequestParam(value="name") String name){
		ModelAndView model = new ModelAndView("helloword");
		
		model.addObject("message", name);
		return model;
		
	}*/
	
	
}