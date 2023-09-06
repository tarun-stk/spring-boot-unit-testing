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
import static org.mockito.Mockito.*;

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

    @DisplayName("Throwing an exception")
    @Test
    public void throwingAnException(){
//        set up
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");
        doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);

//        assert and execute
        assertThrows(RuntimeException.class, () -> applicationService.checkNull(nullStudent));

//        verify
        verify(applicationDao, times(1)).checkNull(nullStudent);

    }

    @DisplayName("Multiple Stubbing")
    @Test
    public void multipleStubbing(){
//        set up
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");
//        when called for the first time throws an exception
//        when called consecutively then returns a message
//        i.e, throws an exception only for the first call
        when(applicationDao.checkNull(nullStudent))
                .thenThrow(new RuntimeException())
                .thenReturn("Do not throw exception second time");

//        assert and execute
        assertThrows(RuntimeException.class, () -> applicationService.checkNull(nullStudent));
        assertEquals("Do not throw exception second time", applicationService.checkNull(nullStudent));

//        verify
        verify(applicationDao, times(2)).checkNull(nullStudent);

    }
}




















