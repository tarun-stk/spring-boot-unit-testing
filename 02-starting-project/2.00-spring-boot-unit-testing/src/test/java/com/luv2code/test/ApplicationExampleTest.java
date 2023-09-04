package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class ApplicationExampleTest {

    private static int count = 0;

    @Value("${info.app.name}")
    private String appInfo;

    @Value("${info.app.description}")
    private String appDescription;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.school.name}")
    private String schoolName;

    @Autowired
    private CollegeStudent student;

    @Autowired
    private StudentGrades studentGrades;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    public void beforeEach(){
        count += 1;
        System.out.println("Testing: " + appInfo + " which is " + appDescription +
                " Version: " + appVersion + " Execution of test Method " + count);
        student.setFirstname("Varun");
        student.setLastname("Chakravarthy");
        student.setEmailAddress("varun.chakravarthy@hotmail.com");
        studentGrades.setMathGradeResults(new ArrayList<>(Arrays.asList(100.0, 88.0, 93.0)));
        student.setStudentGrades(studentGrades);
    }

    @Test
    void testExample(){

    }

    @DisplayName("Add Grade results for student grades")
    @Test
    public void addGradeResultsForStudentGrades(){
        assertEquals(281.0, studentGrades.addGradeResultsForSingleClass(
                student.getStudentGrades().getMathGradeResults()
        ));
    }

    @DisplayName("Add Grade results for student grades not equal")
    @Test
    public void addGradeResultsForStudentGradesAssertNotEquals(){
        assertNotEquals(0, studentGrades.addGradeResultsForSingleClass(
                student.getStudentGrades().getMathGradeResults()
        ));
    }

    @DisplayName("Check if grade is greater")
    @Test
    public void checkIsGradeGreater(){
        assertTrue(studentGrades.isGradeGreater(98, 67), "gradeOne must be greater than gradeTwo");
    }

    @DisplayName("Check if grade is greater False")
    @Test
    public void checkIsGradeGreaterFalse(){
        assertFalse(studentGrades.isGradeGreater(63, 67), "Should return false");
    }

    @DisplayName("Check Null for student grades")
    @Test
    public void checkNullForStudentGrades(){
        assertNotNull(studentGrades.checkNull(student.getStudentGrades().getMathGradeResults()),
                "Object should not be null");
    }

    @DisplayName("Create student without grades in it")
    @Test
    public void createStudentWithoutGradesInit(){
        CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);
        studentTwo.setFirstname("Tarun");
        studentTwo.setLastname("Kumar");
        studentTwo.setEmailAddress("tarun.kumar@yahoo.com");
        assertNotNull(student.getFirstname());
        assertNotNull(student.getLastname());
        assertNotNull(student.getEmailAddress());
        assertNull(studentGrades.checkNull(studentTwo.getStudentGrades()));

    }

    @DisplayName("Check for prototype Scope")
    @Test
    public void verifyStudentsArePrototypes(){
        CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);

        assertNotSame(student, studentTwo);

    }

    @DisplayName("Find Grade Point Average")
    @Test
    public void findGradePointAverage(){
        assertAll("Testing all assertEquals",
                ()-> assertEquals(281.0, studentGrades.addGradeResultsForSingleClass(
                        student.getStudentGrades().getMathGradeResults())),
                ()-> assertEquals(93.67, studentGrades.findGradePointAverage(
                        student.getStudentGrades().getMathGradeResults()))
                );

    }
}


















