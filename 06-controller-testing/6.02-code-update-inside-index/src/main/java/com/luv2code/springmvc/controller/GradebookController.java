package com.luv2code.springmvc.controller;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradebookController {

	@Autowired
	private Gradebook gradebook;

	@Autowired
	private StudentAndGradeService collegeStudentAndGradeService;


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getStudents(Model m) {
		Iterable<CollegeStudent> iterable = collegeStudentAndGradeService.getGradebook();
		m.addAttribute(iterable);
		return "index";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String createStudent(@ModelAttribute("student") CollegeStudent student, Model m) {
		collegeStudentAndGradeService.createStudent(student.getFirstname(), student.getLastname(),
				student.getEmailAddress());
		Iterable<CollegeStudent> collegeStudents = collegeStudentAndGradeService.getGradebook();
		for(CollegeStudent stud: collegeStudents){
			System.out.println(stud);
		}
		m.addAttribute("students", collegeStudents);
		return "index";
	}


	@GetMapping("/studentInformation/{id}")
		public String studentInformation(@PathVariable int id, Model m) {
		return "studentInformation";
		}

}
