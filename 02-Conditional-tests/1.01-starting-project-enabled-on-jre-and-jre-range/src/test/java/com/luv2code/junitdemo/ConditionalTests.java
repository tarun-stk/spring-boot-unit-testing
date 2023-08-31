package com.luv2code.junitdemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

public class ConditionalTests {

//    Will test only if running on jre13
    @Test
    @EnabledOnJre(JRE.JAVA_13)
    void testOnlyOnJava13(){

    }

    @Test
    @EnabledForJreRange(min = JRE.JAVA_11)
    void testOnlyForJreWithMin_11(){

    }

//    Will test only if running on jre17
    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void testOnlyOnJava17(){

    }

//    Will test only if running on in range java11 to java17 inclusive

    @Test
    @EnabledForJreRange(min = JRE.JAVA_11, max = JRE.JAVA_17)
    void testOnlyForJreRange(){

    }
}














