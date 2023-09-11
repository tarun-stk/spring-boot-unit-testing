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

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

//  @BeforeAll ;must be static - stated by Junit documentation.
    @BeforeAll
    public static void beforeAll(){
        request = new MockHttpServletRequest();
        request.setParameter("firstname", "Tarun");
        request.setParameter("lastname", "Kumar");
        request.setParameter("emailAddress", "tarun@hotmail.com");
    }

    @Test
    public void createStudentHttpRequest() throws Exception {

//        performing mock post
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstname", request.getParameter("firstname"))
                .param("lastname", request.getParameter("lastname"))
                .param("emailAddress", request.getParameter("emailAddress")))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");

//        now verify whether the student is inserted into db or not.
        CollegeStudent verifyStudent = studentDao.findByEmailAddress("tarun@hotmail.com");

//        Assert for not null
        assertNotNull(verifyStudent, "Should not be null");


    }

}















