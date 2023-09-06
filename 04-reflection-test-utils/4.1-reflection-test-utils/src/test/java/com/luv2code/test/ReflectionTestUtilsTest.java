package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class ReflectionTestUtilsTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent studentOne;

    @Autowired
    StudentGrades studentGrades;

//    Notes on ReflectionTestUtils
//    Using ReflectionTestUtils we can directly access private fields and methods
//    we can set values and get values for private fields
//    ReflectionTestUtils is provided by spring itself
//    normally private methods/fields can't be accessed outside class
//    If you're thinking when we have public getter and setter for private fields, then y do we need ReflectionTestUtils
//    Imagine a situation when we don;t have getter and setter
//    ReflectionTestUtils works irrespective with getter and setter.

    @BeforeEach
    public void beforeEach(){
        studentOne.setFirstname("Tarun");
        studentOne.setLastname("Kumar");
        studentOne.setEmailAddress("tarun.kumar@hotmail.com");

//        Accessing private field usign reflection test utils
//        setField(target object, field, field value);
        ReflectionTestUtils.setField(studentOne, "id", 1);
        ReflectionTestUtils.setField(studentOne, "studentGrades",
                new StudentGrades(new LinkedList<>(Arrays.asList(100.0, 95.0, 89.0))));
    }

    @DisplayName("Accessing private field using ReflectionTestUtils")
    @Test
    public void accessPrivateFieldValue(){

//        Accessing private field without using getter and using getField from ReflectionTestUtils
        assertEquals(1, ReflectionTestUtils.getField(studentOne, "id"));
    }

    @DisplayName("Invoking private method using ReflectionTestUtils")
    @Test
    public void InvokePrivateMethod(){
        assertEquals("Tarun 1",
//                Accessing private method using invokeMethod
                ReflectionTestUtils.invokeMethod(studentOne, "getFirstNameAndId"),
                "Failed to call private method");
    }

}
