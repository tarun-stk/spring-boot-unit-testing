package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.Grade;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentAndGradebookControllerTest {

//    static because we're going to acess this in static method
    private static MockHttpServletRequest request;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    private MathGradeDao mathGradeDao;

    @BeforeEach
    public void beforeEach(){
        jdbc.execute("insert into student(id, firstname, lastname, email_address)" +
                " values(1, 'Tarun', 'Soodula', 'tarun.soodula@hotmail.com')");

        jdbc.execute("insert into math_grade(id, student_id, grade) " +
                "values (1, 1, 100.0)");

        jdbc.execute("insert into history_grade(id, student_id, grade) " +
                "values (1, 1, 100.0)");

        jdbc.execute("insert into science_grade(id, student_id, grade) " +
                "values (1, 1, 100.0)");
    }

    @Test
    public void testDeleteStudentHttpRequest() throws Exception {

//        first check if student is there in db
        assertTrue(studentDao.findById(1).isPresent());
//        now delete
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/delete/student/{id}", 1))
                .andExpect(status().isOk()).andReturn();
//        now again check if student is there in db
        assertFalse(studentDao.findById(1).isPresent(), "Should not be there in db");
    }

    @Test
    public void testDeleteStudentHttpRequestGiveError() throws Exception {

//        first check if student is there in db
        assertTrue(studentDao.findById(1).isPresent());
//        now delete
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/delete/student/{id}", 2))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error");

    }

    @Test
    public void testStudentInformationHttpRequest() throws Exception {

//        check if student is there
        assertTrue(studentDao.findById(1).isPresent());
//        perform api request
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 1))
                .andExpect(status().isOk()).andReturn();
//      get the viewnam
        ModelAndView mav = mvcResult.getModelAndView();

//        assert
        ModelAndViewAssert.assertViewName(mav, "studentInformation");

    }

    @Test
    public void testStudentInformationHttpRequestReturnsError() throws Exception {

//        check if student is not there
        assertFalse(studentDao.findById(0).isPresent());
//        perform api request
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 0))
                .andExpect(status().isOk()).andReturn();
//      get the viewnam
        ModelAndView mav = mvcResult.getModelAndView();

//        assert
        ModelAndViewAssert.assertViewName(mav, "error");

    }

    @Test
    public void createValidGradeHttpRequest() throws Exception {

//        asser that studnet is presetn in db
        assertTrue(studentDao.findById(1).isPresent());

//        retrivee the student
        GradebookCollegeStudent student = studentService.studentInformatiom(1);

//        assert for number of math grades
        assertEquals(1, student.getStudentGrades().getMathGradeResults().size());

//        add a new math grade usign post
        MvcResult mvcResult = this.mockMvc.perform(post("/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .param("studentId", "1")
                .param("gradeType", "math")
                .param("grade", "100.0"))
                .andExpect(status().isOk()).andReturn();

//        Get the view
        ModelAndView mav = mvcResult.getModelAndView();

//        assert view names
        ModelAndViewAssert.assertViewName(mav, "studentInformation");

//        retrieve student
        student = studentService.studentInformatiom(1);


//        verify whether new grade was added or not
        assertEquals(2, student.getStudentGrades().getMathGradeResults().size());

    }

    @Test
    public void createAValidGradeForInvalidStudentHttpReturnsError() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .param("studentId", "0")
                .param("grade", "85.00")
                .param("gradeType", "history"))
                .andExpect(status().isOk()).andReturn();

//        gaet the view name
        ModelAndView mav = mvcResult.getModelAndView();

//        assert actual and expected
        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    public void createInvalidGradeTypeHttpReturnsError() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("studentId", "1")
                        .param("grade", "85.00")
                        .param("gradeType", "literature"))
                .andExpect(status().isOk()).andReturn();

//        gaet the view name
        ModelAndView mav = mvcResult.getModelAndView();

//        assert actual and expected
        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    public void deleteAValidGradeHttpRequest() throws Exception {
//        retrieve record
        Optional<MathGrade> mathGrade = mathGradeDao.findById(1);
//        must be present
        assertTrue(mathGrade.isPresent());

//        perform delete
        MvcResult mvcResult = this.mockMvc.perform(
                get("/grades/{id}/{gradeType}", 1, "math"))
                .andExpect(status().isOk()).andReturn();
//        find after delete
        mathGrade = mathGradeDao.findById(1);
        assertFalse(mathGrade.isPresent());
    }

    @Test
    public void deleteAValidGradeHttpWhenAGradeIdIsInvalid() throws Exception{
        //        retrieve record
        Optional<MathGrade> mathGrade = mathGradeDao.findById(0);
//        must be present
        assertFalse(mathGrade.isPresent());

//        perform delete
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/grades/{id}/{gradeType}", 0, "math"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    public void deleteAValidGradeHttpWhenAGradeTypeIsInvalid() throws Exception{

//        perform delete
        MvcResult mvcResult = this.mockMvc.perform(
                        get("/grades/{id}/{gradeType}", 1, "literature"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "error");
    }


    @AfterEach
    public void afterEach(){
        jdbc.execute("DELETE FROM student");
        jdbc.execute("DELETE FROM math_grade");
        jdbc.execute("DELETE FROM science_grade");
        jdbc.execute("DELETE FROM history_grade");

    }

}















