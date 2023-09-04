package com.luv2code.junitdemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class ConditionalTests {

//    Will be tested only after removing Disabled annotation
    @Test
    @Disabled("Disabled until JIR #123 is resolved")
    void testDisabled(){
//        execute test
    }

//    Will run on all OS except for MAC OR just disabled for MAC
    @Test
    @DisabledOnOs(OS.MAC)
    void testNotForMac(){
//        execute test
    }

//    will run only if OS is mac
    @Test
    @EnabledOnOs(OS.MAC)
    void testForMac(){
//        execute test
    }

//    will run only if OS is mac or windows
    @Test
    @EnabledOnOs({OS.MAC, OS.WINDOWS})
    void testForMacAndWindows(){
//        execute test
    }

    //    will run only if OS is mac
    @Test
    @EnabledOnOs(OS.LINUX)
    void testForLinux(){
//        execute test
    }
}














