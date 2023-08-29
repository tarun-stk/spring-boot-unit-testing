package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

//test classes may not be public so can omit
class DemoUtilsTest {

    DemoUtils demoUtils;

//    defining @BeforeEach
//    This will run before each test method, commonly used to set up test data
    @BeforeEach
    void setUpBeforeEachTest(){
        System.out.println("@BeforeEach running before each test method");
//        set up
        demoUtils = new DemoUtils();
    }

//    defining @AfterEach
//    This will run after each test method, commonly used to  clean up test data
    @AfterEach
    void tearDownAfterEachTest(){
        System.out.println("@AfterEach running before each test method");
    }

//    Defining @BeforeAll
//    This will run once before test methods start executing
//    commonly used to set up db connections, and remote server connections
//    NOTE: this method must be static
    @BeforeAll
    static void setUpBeforeAllTest(){
        System.out.println("@BeforeAll running once before all tests");
    }

//    Defining @AfterAll
//    This will run once after test methods complete executing
//    commonly used to destroy up db connections, and cut off remote server connections
//    NOTE: this method must be static
    @AfterAll
    static void tearDownAfterAllTest(){
        System.out.println("@AfterAll running once after all tests");

    }



//    Testing assert equals and not equals
    @Test
    public void testEqualsNotEquals(){

        System.out.println("Running test: testEqualsNotEquals()");

//        execute and assert
        int expected = 10;
        int unexpected = 12;
        int actual = demoUtils.add(4, 6);
        assertEquals(expected, actual, "4+6 must be 10");
        assertNotEquals(unexpected, actual, "4+6 must not be 12");
    }

//    Testing assert null and not null
    @Test
    public void testNullNotNull(){

        System.out.println("Running test: testNullNotNull()");

//        execute and assert
        String str1 = null;
        String str2 = "stk";
        assertNull(demoUtils.checkNull(str1), "Object must be null");
        assertNotNull(demoUtils.checkNull(str2), "Object must not be null");
    }


}
