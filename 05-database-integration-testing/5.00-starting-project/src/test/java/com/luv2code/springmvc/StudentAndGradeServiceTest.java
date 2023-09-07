package com.luv2code.springmvc;


import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application.properties")
public class StudentAndGradeServiceTest {

//    Done using tdd

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private StudentDao studentDao;

//    JdbcTemplate provided by spring framework to execute jdbc queries.
    @Autowired
    JdbcTemplate jdbc;

//    @BeforeEach is used to create set up data
    @BeforeEach
    public void setUpDatabase(){
//        inserting new record with id 1.
        jdbc.execute("insert into student(id, firstname, lastname, email_address)" +
                "values (1, 'Tarun', 'Kumar', 'tarun@hotmail.com')");
    }

//    test to check whether student was created or not
    @Test
    public void createStudentService(){
        studentService.createStudent("Tarun", "Kumar", "tarun.kumar@google.com");
        CollegeStudent student = studentDao.findByEmailAddress("tarun.kumar@google.com");
        assertEquals("tarun.kumar@google.com", student.getEmailAddress(), "Find by email");
    }

//    test to check if there is a student with given id in database.
    @Test
    public void isStudentNullCheck(){
        assertTrue(studentService.checkIfStudentIsNull(1)); //pass
        assertFalse(studentService.checkIfStudentIsNull(0)); //fail
    }

//    test to check delete a student
    @Test
    public void deleteStudent(){
        Optional<CollegeStudent> student = studentDao.findById(1);
//        check if you've that studnet in db
        assertTrue(student.isPresent());
//        call for delete method
        studentService.deleteStudentById(1);
//        check if student is there in db
        assertFalse(studentService.checkIfStudentIsNull(1));
    }

//    Notes on @Sql
//    -this anno loads props from mentioned files for this test method
//    -this anno will run after @BeforeEach is completed
//    so first one student will be inserted using @BeforeEach method
//    then four more will be isnerted using sql file.

    @Sql("/insertData.sql")
    @Test
    public void getGradeBookService(){
        Iterable<CollegeStudent> iterableCollegeStudent = studentService.getGradebook();

        List<CollegeStudent> collegeStudents = new ArrayList<>();
        for(CollegeStudent collegeStudent: iterableCollegeStudent){
            collegeStudents.add(collegeStudent);
        }

//        check if both sizes are equal
        assertEquals(5, collegeStudents.size());
    }

//    AFTerEach is used to destroy set up data
    @AfterEach
    public void setUpAfterTransaction(){
//        deletes all student records.
        jdbc.execute("DELETE FROM student");
    }


}
