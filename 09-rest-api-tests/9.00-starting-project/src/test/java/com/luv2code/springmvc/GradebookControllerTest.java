package com.luv2code.springmvc;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GradebookControllerTest {

    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Mock
    StudentAndGradeService studentCreateServiceMock;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradesDao mathGradeDao;

    @Autowired
    private ScienceGradesDao scienceGradeDao;

    @Autowired
    private HistoryGradesDao historyGradeDao;

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CollegeStudent student;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${sql.script.create.student}")
    private String sqlAddStudent;

    @Value("${sql.script.create.math.grade}")
    private String sqlAddMathGrade;

    @Value("${sql.script.create.science.grade}")
    private String sqlAddScienceGrade;

    @Value("${sql.script.create.history.grade}")
    private String sqlAddHistoryGrade;

    @Value("${sql.script.delete.student}")
    private String sqlDeleteStudent;

    @Value("${sql.script.delete.math.grade}")
    private String sqlDeleteMathGrade;

    @Value("${sql.script.delete.science.grade}")
    private String sqlDeleteScienceGrade;

    @Value("${sql.script.delete.history.grade}")
    private String sqlDeleteHistoryGrade;

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    public void setUp() {

        request = new MockHttpServletRequest();

        request.setParameter("firstname", "Tarun");

        request.setParameter("lastname", "Soodula");

        request.setParameter("emailAddress", "tarun.soodula@hotmail.com");

    }

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddStudent);
        jdbc.execute(sqlAddMathGrade);
        jdbc.execute(sqlAddScienceGrade);
        jdbc.execute(sqlAddHistoryGrade);
    }


    @Test
    public void getStudentHttpRequest() throws Exception {

//        creating one student
        student.setFirstname("Tarun");
        student.setLastname("Kumar");
        student.setEmailAddress("tarun@hotmail.com");
//        saving the student into db
        entityManager.persist(student);
        entityManager.flush();

//        performing api call
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
//                expecting status to be OK -> 200
                .andExpect(status().isOk())
//                expecting response to be JSON content type
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
//        verifying the response body
//        jsonPath() is an open source project availbale at github.com/json-path/jsonPath
//        when dealing with spring boot starter test, it auto includes jsonPath
//        $ is the root in jsonPath
//        for example: json response be :
//        [
//            {
//                "id": 1,
//                "firstname:" "Tarun",
//                "subjects":{
//                         "Math",
//                        "Science"
//                }
//            }
//        ]

//        In the above response you can access firstname using $.firstname, similarly subjects using $.subjects
//        also access the size of thee output array using hasSize method.
                .andExpect(jsonPath("$", hasSize(2)));


    }

    @Test
    public void createStudentHttpRequest() throws Exception {

//       creating a student record
        student.setFirstname("Chad");
        student.setLastname("Darby");
        student.setEmailAddress("chad@luv2code.com");

//        perform post api call
        mockMvc.perform(MockMvcRequestBuilders.post("/")
                                .contentType(MediaType.APPLICATION_JSON)
//                        objectMapper.writeValueAsString(student) provided by Jackson API
//                        used to convert java objects to JSON
                                .content(objectMapper.writeValueAsString(student))
                )
//                expecting status to be ok
                .andExpect(status().isOk())
//                checking the size of response body
//                2 because, one student added in beforeeach and other using post call
                .andExpect(jsonPath("$", hasSize(2)));

//        reverify that student is actually added to db
        CollegeStudent verifyStudent = studentDao.findByEmailAddress("chad@luv2code.com");
        assertNotNull(verifyStudent);

    }

    @Test
    public void deleteStudentHttpRequest() throws Exception {

//        make sure you have student in db
        assertTrue(studentDao.findById(1).isPresent());

//        perform delete api call
        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType((APPLICATION_JSON_UTF8)))
//                JSON response with array size 0
                .andExpect(jsonPath("$", hasSize(0)));


//        verify whethere is actually deleted or not
        assertFalse(studentDao.findById(1).isPresent());

    }

    @Test
    public void deleteStudentHttpRequestReturnsError() throws Exception {

//        make sure you dont have student in db
        assertFalse(studentDao.findById(0).isPresent());

//        perform delete api call
        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}", 0))
//                expect a status in 400 series
                .andExpect(status().is4xxClientError())
//                expect status of 404 in response body
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")));


    }

    @Test
    public void studentInformationHttpRequest() throws Exception {

//        retrieve CollegeStudent from dao
        Optional<CollegeStudent> student = studentDao.findById(1);
//      make sure it is there in db
        assertTrue(student.isPresent());

//        perform api call to retrieve response
        mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{1}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
//                checking json response
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("Eric")))
                .andExpect(jsonPath("$.lastname", is("Roby")))
                .andExpect(jsonPath("$.emailAddress", is("eric.roby@luv2code_school.com")));

    }

    @Test
    public void studentInformationHttpRequestEmptyResponse() throws Exception {

//        retrieve CollegeStudent from dao
        Optional<CollegeStudent> student = studentDao.findById(0);
//      make sure it is not there in db
        assertFalse(student.isPresent());

//        perform api call to retrieve response
        mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{1}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")))
                .andExpect(jsonPath("$.status", is(404)));

    }

    @Test
    public void createAValidGradeHttpRequest() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                        .contentType(APPLICATION_JSON_UTF8)
//                adding second math grade
                        .param("grade", "85.00")
                        .param("gradeType", "math")
                        .param("studentId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("Eric")))
                .andExpect(jsonPath("$.lastname", is("Roby")))
                .andExpect(jsonPath("$.emailAddress", is("eric.roby@luv2code_school.com")))
                .andExpect(jsonPath("$.studentGrades.mathGradeResults", hasSize(2)));
    }

    @Test
    public void createAValidGradeHttpRequestStudentIdDoesNotExistEmptyResponse() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                        .contentType(APPLICATION_JSON_UTF8)
//                adding second math grade
                        .param("grade", "85.00")
                        .param("gradeType", "math")
                        .param("studentId", "0"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")))
                .andExpect(jsonPath("$.status", is(404)));

    }

    @Test
    public void createAValidGradeHttpRequestGradeTypeDoesNotExistEmptyResponse() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                        .contentType(APPLICATION_JSON_UTF8)
//                adding second math grade
                        .param("grade", "85.00")
                        .param("gradeType", "literature")
                        .param("studentId", "1"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")))
                .andExpect(jsonPath("$.status", is(404)));

    }

    @Test
    public void deleteAValidGradeHttpRequest() throws Exception {

        Optional<MathGrade> mathGrade = mathGradeDao.findById(1);

//        assert that it is present
        assertTrue(mathGrade.isPresent());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{id}/{gradeType}", 1, "math"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("Eric")))
                .andExpect(jsonPath("$.lastname", is("Roby")))
                .andExpect(jsonPath("$.emailAddress", is("eric.roby@luv2code_school.com")))
//                size 0 as we've just deleted the grade
                .andExpect(jsonPath("$.studentGrades.mathGradeResults", hasSize(0)));

    }

    @Test
    public void deleteAValidGradeHttpRequestInvalidGradeId() throws Exception {

        Optional<MathGrade> mathGrade = mathGradeDao.findById(0);

//        assert that it is not present
        assertFalse(mathGrade.isPresent());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{id}/{gradeType}", 0, "math"))
//  `               returns error response
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")))
                .andExpect(jsonPath("$.status", is(404)));

    }

    @Test
    public void deleteAValidGradeHttpRequestInvalidGradeType() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{id}/{gradeType}", 1, "literature"))
//  `               returns error response
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Student or Grade was not found")))
                .andExpect(jsonPath("$.status", is(404)));

    }


    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteStudent);
        jdbc.execute(sqlDeleteMathGrade);
        jdbc.execute(sqlDeleteScienceGrade);
        jdbc.execute(sqlDeleteHistoryGrade);
    }


}
