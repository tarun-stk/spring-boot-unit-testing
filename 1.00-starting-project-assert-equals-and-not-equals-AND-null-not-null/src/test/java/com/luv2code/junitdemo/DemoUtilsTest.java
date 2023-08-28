package com.luv2code.junitdemo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//test classes may not be public so can omit
class DemoUtilsTest {

//    Testing assert equals and not equals
    @Test
    public void testEqualsNotEquals(){
//        set up
        DemoUtils demoUtils = new DemoUtils();

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
        //        set up
        DemoUtils demoUtils = new DemoUtils();

//        execute and assert
        String str1 = null;
        String str2 = "stk";
        assertNull(demoUtils.checkNull(str1), "Object must be null");
        assertNotNull(demoUtils.checkNull(str2), "Object must not be null");
    }


}
