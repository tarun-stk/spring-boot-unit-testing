package com.luv2code.junitdemo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//test classes may not be public so can omit
class DemoUtilsTest {

    static DemoUtils theDemoUtils;

    @BeforeAll
    public static void setUp(){
        theDemoUtils = new DemoUtils();
    }

    @Test
    @DisplayName("Same and Not Same")
    public void testSameAndNotSame(){
        String str = "Khan Academy";
        assertSame(theDemoUtils.getAcademy(), theDemoUtils.getAcademyDuplicate(), "Objects should refer" +
                "to same object");
        assertNotSame(str, theDemoUtils.getAcademy(), "Objects should not refer" +
                "to same object");
    }

    @Test
    @DisplayName("True & False")
    public void testTrueAndFalse(){
        int gradeOne = 10;
        int gradeTwo = 5;

        assertTrue(theDemoUtils.isGreater(gradeOne, gradeTwo), "This should return true");
        assertFalse(theDemoUtils.isGreater(gradeTwo, gradeOne), "This should return false");
    }


}
