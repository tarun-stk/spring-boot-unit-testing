package com.luv2code.tdd;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FizzBuzzTest {

//    Parameterized test example
//    behind the scenes Junit will loop through all values in csv file and generates results
    @DisplayName("Testing with Small data file")
    @Order(1)
    @ParameterizedTest(name="value={0}, expected={1}")
    @CsvFileSource(resources = "/small-test-data.csv")
    void testSmallDataFile(int value, String expected){

        assertEquals(expected, FizzBuzz.compute(value));

    }

    @DisplayName("Testing with Medium data file")
    @Order(2)
    @ParameterizedTest(name="value={0}, expected={1}")
    @CsvFileSource(resources = "/medium-test-data.csv")
    void testMediumDataFile(int value, String expected){

        assertEquals(expected, FizzBuzz.compute(value));

    }

    @DisplayName("Testing with Large data file")
    @Order(3)
    @ParameterizedTest(name="value={0}, expected={1}")
    @CsvFileSource(resources = "/large-test-data.csv")
    void testLargeDataFile(int value, String expected){

        assertEquals(expected, FizzBuzz.compute(value));

    }

//    Testing using csvSorce
    @DisplayName("Testing with csv Data")
    @ParameterizedTest
    @CsvSource({
            "1,1",
            "2,2",
            "3,Fizz",
            "4,4",
            "5,Buzz",
            "6,Fizz",
            "7,7"
    })
    @Order(4)
    void testCsvData(int value, String expected){
        assertEquals(expected, FizzBuzz.compute(value));
    }

}
