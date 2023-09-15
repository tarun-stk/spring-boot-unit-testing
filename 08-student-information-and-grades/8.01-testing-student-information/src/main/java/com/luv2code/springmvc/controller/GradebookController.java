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

		GradebookCollegeStudent studentEntity = collegeStudentAndGradeService.studentInformatiom(id);
		m.addAttribute("student", studentEntity);

		if(studentEntity.getStudentGrades().getMathGradeResults().size() > 0){
			m.addAttribute("mathAverage", studentEntity.getStudentGrades().findGradePointAverage(
					studentEntity.getStudentGrades().getMathGradeResults()
			));
		}
		else{
			m.addAttribute("mathAverage", "N/A");
		}

		if(studentEntity.getStudentGrades().getHistoryGradeResults().size() > 0){
			m.addAttribute("historyAverage", studentEntity.getStudentGrades().findGradePointAverage(
					studentEntity.getStudentGrades().getHistoryGradeResults()
			));
		}
		else{
			m.addAttribute("historyAverage", "N/A");
		}

		if(studentEntity.getStudentGrades().getScienceGradeResults().size() > 0){
			m.addAttribute("scienceAverage", studentEntity.getStudentGrades().findGradePointAverage(
					studentEntity.getStudentGrades().getScienceGradeResults()
			));
		}
		else{
			m.addAttribute("scienceAverage", "N/A");
		}


		return "studentInformation";
	}

}


















