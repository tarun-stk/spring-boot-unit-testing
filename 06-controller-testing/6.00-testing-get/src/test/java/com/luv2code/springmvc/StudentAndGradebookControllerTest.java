package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentAndGradebookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentAndGradeService studentService;

    @Autowired
    JdbcTemplate jdbc;

//    @BeforeEach is used to create set up data
    @BeforeEach
    public void setUpDatabase(){
//        inserting new record with id 1.
        jdbc.execute("insert into student(id, firstname, lastname, email_address)" +
                " values (1, 'Tarun', 'Kumar', 'tarun@hotmail.com')");
    }

    @Test
    public void getStudentsHttpRequest() throws Exception{

//        set up
        CollegeStudent studentOne = new CollegeStudent("Tarun", "Kumar", "tarun.kumar@hotmail.com");
        CollegeStudent studentTwo = new CollegeStudent("Varun", "Chakravarthy", "varun.chaks@yahoo.com");

        List<CollegeStudent> studentList = new ArrayList<>(Arrays.asList(studentTwo, studentOne));

        when(studentService.getGradebook()).thenReturn(studentList);

//        assert & execute
//        asserting for students list from iterable.
        assertIterableEquals(studentList, studentService.getGradebook());

//        MockMvc is a spring provided bean to perform rest api calls, (internally it won't start running
//        server, but creates container on its own)
//        we use mockMvc.perform(MockMvcRequestBuilders.get("/")) to call the rest api call based on url
//        & .andExpect(status().isOk()).andReturn(); this is setting expectation
//        in this case we're expecting status to OK.
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();

//        getting back the ModelAndView (mav) from the operation performed above
        ModelAndView mav = mvcResult.getModelAndView();

//        asserting for viewName by using ModelAndViewAssert.assertViewName(actual, expected);
        ModelAndViewAssert.assertViewName(mav, "index");

    }



//    AFTerEach is used to destroy set up data
    @AfterEach
    public void setUpAfterTransaction(){
//        deletes all student records.
        jdbc.execute("DELETE FROM student");
    }

}















