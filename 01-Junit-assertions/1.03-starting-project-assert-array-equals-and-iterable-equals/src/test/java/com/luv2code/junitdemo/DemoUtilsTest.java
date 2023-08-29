package com.luv2code.junitdemo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//test classes may not be public so can omit
class DemoUtilsTest {

    static DemoUtils theDemoUtils;

    @BeforeAll
    public static void setUp(){
        theDemoUtils = new DemoUtils();
    }

    @Test
    @DisplayName("Array Equals")
    public void testArrayEquals(){
        String[] stringArray = {"A", "B", "C"};

        assertArrayEquals(stringArray, theDemoUtils.getFirstThreeLettersOfAlphabet(), "Arrays should be deeply equal");
    }

    @Test
    @DisplayName("Iterable Equals")
    public void testIterableEquals(){
        List<String> theList = List.of("luv", "2", "code");

        assertIterableEquals(theList, theDemoUtils.getAcademyInList(), "Expected list should be same as theList");
    }

    @Test
    @DisplayName("Lines Match")
    public void testLinesMatch(){
        List<String> theList = List.of("luv", "2", "code");

        assertLinesMatch(theList, theDemoUtils.getAcademyInList(), "Lines should match");
    }
}
