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

	@RequestMapping(value = "/delete/student/{id}", method = RequestMethod.GET)
	public String deleteStudent(@PathVariable("id") int id, Model m) {

		if(!collegeStudentAndGradeService.checkIfStudentIsNull(id)){
			return "error";
		}

		collegeStudentAndGradeService.deleteStudentById(id);
		Iterable<CollegeStudent> collegeStudents = collegeStudentAndGradeService.getGradebook();
		for(CollegeStudent stud: collegeStudents){
			System.out.println(stud);
		}
		m.addAttribute("students", collegeStudents);
		return "index";
	}


	@GetMapping("/studentInformation/{id}")
	public String studentInformation(@PathVariable int id, Model m) {

		if(!collegeStudentAndGradeService.checkIfStudentIsNull(id))
			return "error";

		collegeStudentAndGradeService.configureStudentInformationMode(id, m);
		return "studentInformation";
	}

	@PostMapping("/grades")
	public String createGrade(@RequestParam("grade") double grade,
							  @RequestParam("gradeType") String gradeType,
							  @RequestParam("studentId") int studentId,
							  Model m){

		if(!collegeStudentAndGradeService.checkIfStudentIsNull(studentId))
			return "error";

		boolean success = collegeStudentAndGradeService.createGrade(grade, studentId, gradeType);

		if(!success)
			return "error";

		collegeStudentAndGradeService.configureStudentInformationMode(studentId, m);

		return "studentInformation";

	}

}


















