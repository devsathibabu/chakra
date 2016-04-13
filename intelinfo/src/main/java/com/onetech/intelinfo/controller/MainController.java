package com.onetech.intelinfo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.onetech.intelinfo.model.Employee;
import com.onetech.intelinfo.model.Document;
import com.onetech.intelinfo.service.EmployeeDaoImpl;

@Controller
public class MainController {
	@Autowired
	EmployeeDaoImpl empDaoImpl = new EmployeeDaoImpl();

	@RequestMapping("/welcome")
	public ModelAndView showMessage(@RequestParam(value = "name", 
	required = false, defaultValue = "World") String name) {
		System.out.println("in controller");
		String message = "Welcome to Spring MVC!";
		ModelAndView mv = new ModelAndView("helloword");
		mv.addObject("message", message);
		mv.addObject("name", name);
		return mv;
	}
	
	@RequestMapping(value = "employee",method=RequestMethod.POST )
	public ModelAndView saveEmployee(@ModelAttribute Employee empvo, BindingResult result){
		
		
		String name = empvo.getName();
		String address = empvo.getAddress();
		int age = empvo.getAge();
		Employee emp = new Employee(name,address,age);
		 System.out.println(emp.getName());
		 System.out.println(emp.getAddress());
		 System.out.println(emp.getAge());
		 empDaoImpl.save(emp);
		return new ModelAndView("welcome");
	}
}