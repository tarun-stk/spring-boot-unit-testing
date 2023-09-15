package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
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

    @Mock
    private StudentAndGradeService studentService;

    @Autowired
    JdbcTemplate jdbc;

    @BeforeEach
    public void beforeEach(){
        jdbc.execute("insert into student(id, firstname, lastname, email_address)" +
                " values(1, 'Tarun', 'Soodula', 'tarun.soodula@hotmail.com')");
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


    @AfterEach
    public void afterEach(){
        jdbc.execute("DELETE FROM student");
    }

}















