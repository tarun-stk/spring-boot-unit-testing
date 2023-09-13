package com.luv2code.springmvc;


import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;
import java.util.Collections;
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

    @Autowired
    private MathGradeDao mathGradeDao;

    @Autowired
    private ScienceGradesDao scienceGradeDao;

    @Autowired
    private HistoryGradesDao historyGradeDao;

//    @BeforeEach is used to create set up data
    @BeforeEach
    public void setUpDatabase(){
//        inserting new record with id 1.
        jdbc.execute("insert into student(id, firstname, lastname, email_address)" +
                "values (1, 'Tarun', 'Kumar', 'tarun@hotmail.com')");

        jdbc.execute("insert into math_grade(id, student_id, grade) " +
                "values (1, 1, 100.0)");

        jdbc.execute("insert into history_grade(id, student_id, grade) " +
                "values (1, 1, 100.0)");

        jdbc.execute("insert into science_grade(id, student_id, grade) " +
                "values (1, 1, 100.0)");
    }

    @Test
    public void createGradeService(){

//        create the grade
        assertTrue(studentService.createGrade(80.5, 1, "math"));
        assertTrue(studentService.createGrade(80.5, 1, "science"));
        assertTrue(studentService.createGrade(80.5, 1, "history"));

//        get all grades with studetnId
        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(1);

//        verify there are grades
//        casting iterables to collections as to access check size
        assertTrue(((Collection<MathGrade>) mathGrades).size() == 2, "Should have 2 math grades");
        assertTrue(((Collection<ScienceGrade>)scienceGrades).size() == 2);
        assertTrue(((Collection<HistoryGrade>)historyGrades).size() == 2);


    }

    @Test
    public void createGradeServiceReturnFalse(){
//        grad invalid
        assertFalse(studentService.createGrade(105, 1, "math"));
        assertFalse(studentService.createGrade(-54, 1, "math"));
//        student with id not present
        assertFalse(studentService.createGrade(89.5, 2, "math"));
//        subject not present
        assertFalse(studentService.createGrade(90, 1, "literature"));
    }

    @Test
    public void deleteGradeService(){
//        deleting math grade
        assertEquals(1, studentService.deleteGrade(1, "math"),
                "Returns studentId of after delete");
        //        deleting science grade
        assertEquals(1, studentService.deleteGrade(1, "science"),
                "Returns studentId of after delete");
        //        deleting history grade
        assertEquals(1, studentService.deleteGrade(1, "history"),
                "Returns studentId of after delete");

    }

    @Test
    public void deleteGradeServiceReturnIdOfZero(){
        /*grade id of 0 is not present in math grade*/
        assertEquals(0, studentService.deleteGrade(0, "math"),
                "No grades with id 0");
        /*no grade type of literature*/
        assertEquals(0, studentService.deleteGrade(0, "literature"),
                "No grades of type literature");
    }

//    deleting student will also deleted associated grades
    @Test
    public void deleteStudent(){

//        retrive the student
        Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(1);
        /*get grades*/
        Optional<MathGrade> deletedMathGrade = mathGradeDao.findById(1);
        Optional<ScienceGrade> deletedScienceGrade = scienceGradeDao.findById(1);
        Optional<HistoryGrade> deletedHistoryGrade = historyGradeDao.findById(1);

//        assert that the student is present in db
        assertTrue(deletedCollegeStudent.isPresent());
//      assert that grades are present
        assertTrue(deletedMathGrade.isPresent());
        assertTrue(deletedScienceGrade.isPresent());
        assertTrue(deletedHistoryGrade.isPresent());

        studentService.deleteStudentById(1);

        deletedCollegeStudent = studentDao.findById(1);
        deletedMathGrade = mathGradeDao.findById(1);
        deletedHistoryGrade = historyGradeDao.findById(1);
        deletedScienceGrade = scienceGradeDao.findById(1);

//        assert that student is deleted and not present in db
        assertFalse(deletedCollegeStudent.isPresent());
//      assert that grades are not present
        assertFalse(deletedMathGrade.isPresent());
        assertFalse(deletedScienceGrade.isPresent());
        assertFalse(deletedHistoryGrade.isPresent());
    }

    @Test
    public void studentInformation(){

        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformatiom(1);
        assertNotNull(gradebookCollegeStudent);
        assertEquals(1, gradebookCollegeStudent.getId());
        assertEquals("Tarun", gradebookCollegeStudent.getFirstname());
        assertEquals("Kumar", gradebookCollegeStudent.getLastname());
        assertEquals("tarun@hotmail.com", gradebookCollegeStudent.getEmailAddress());
        assertTrue(gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size() == 1);
    }

    @Test
    public void studentInformationServiceReturnNull(){

        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformatiom(0);

        assertNull(gradebookCollegeStudent);
    }

//    AFTerEach is used to destroy set up data
    @AfterEach
    public void setUpAfterTransaction(){
//        deletes all student records.
        jdbc.execute("DELETE FROM student");

        jdbc.execute("DELETE FROM math_grade");
        jdbc.execute("DELETE FROM science_grade");
        jdbc.execute("DELETE FROM history_grade");
    }


}
