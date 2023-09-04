package com.luv2code.junitdemo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//test classes may not be public so can omit
class DemoUtilsTest {

    static DemoUtils theDemoUtils;

    @BeforeAll
    public static void setUp() {
        theDemoUtils = new DemoUtils();
    }

    @Test
    @DisplayName("Throws and Does Not Throw")
    public void testThrowsAndDoesNotThrow(){
        assertThrows(Exception.class, () -> { theDemoUtils.throwException(-1); }, "Should throw an exception");
        assertDoesNotThrow(() -> { theDemoUtils.throwException(1); }, "Should not throw an exception" );
    }

    @Test
    @DisplayName("Timeout")
    public void testTimeout(){
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> { theDemoUtils.checkTimeout(); },
                "Method should complete execution under 3 seconds");
    }

}
