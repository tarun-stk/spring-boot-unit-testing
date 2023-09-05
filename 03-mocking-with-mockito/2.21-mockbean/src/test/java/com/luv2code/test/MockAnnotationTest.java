package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class MockAnnotationTest {

    @Autowired
    private CollegeStudent studentOne;

    @Autowired
    private StudentGrades studentGrades;

//    Autowiring ApplicationContext to get beans
    @Autowired
    private ApplicationContext context;

//    Creating mock for ApplicationDao.
//    @Mock - provided by Mockito
    @MockBean // provided by spring boot
    private ApplicationDao applicationDao;

//    Usages of @InjectMocks
//    when using this anno, it will inject only dependencies(which are there inside ApplicationService class)
//    of those which are actually annotated with @Mock or @Spy annos,
//    Like above we've annotated @Mock for ApplicationDao, so it will inject that mock.
//    @InjectMocks
    @Autowired // regular injection
    private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach(){
        studentOne.setFirstname("Tarun");
        studentOne.setLastname("Kumar");
        studentOne.setEmailAddress("tarun.kumar@gmail.com");
        studentOne.setStudentGrades(studentGrades);
    }

    @DisplayName("When & Verify")
    @Test
    public void assertEqualsTestAddGrades(){
//        Four stages
//        1. set up expectations with mock responses
        when(applicationDao.addGradeResultsForSingleClass(
                studentOne.getStudentGrades().getMathGradeResults())).thenReturn(100.00);

//        2. execute the method you want to test
//        3. Assert - Check result and verify that it is the expected result.
        assertEquals(100.00,
                applicationService.addGradeResultsForSingleClass(studentOne.getStudentGrades().getMathGradeResults()));

//        4. Optionally, verify calls (how many times called etc)
//        Verifying whether applicationDao.addGradeResultsForSingleClass(studentGrades.getMathGradeResults())
//          was called or not
        verify(applicationDao).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());

//        you can also verify number of times method has been called
//        We're calling only once so times(1)
        verify(applicationDao, times(1)).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
    }

    @DisplayName("Find GPA")
    @Test
    public void assertEqualsOnFindGpa(){
//        set up
        when(applicationDao.findGradePointAverage(studentOne.getStudentGrades().getMathGradeResults()))
                .thenReturn(88.31);

//        execute and perform assert
        assertEquals(88.31,
                applicationService.findGradePointAverage(studentOne.getStudentGrades().getMathGradeResults()));

//        verify
        verify(applicationDao, times(1)).findGradePointAverage(
                studentOne.getStudentGrades().getMathGradeResults());

    }

    @DisplayName("check null")
    @Test
    public void assertCheckNull(){
//        set up
        when(applicationDao.checkNull(studentOne)).thenReturn(true);

//        assert and execute
        assertNotNull(applicationService.checkNull(studentOne));

//        verify
        verify(applicationDao, times(1)).checkNull(studentOne);

    }
}




















