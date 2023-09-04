package com.luv2code.tdd;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FizzBuzzTest {

//    if number is divisible by 3 then print Fizz
    @Test
    @DisplayName("Divisible by Three")
    @Order(1)
    void testForDivisibleByThree(){
        String expected = "Fizz";
        assertEquals(expected, FizzBuzz.compute(3), "should return Fizz");
    }

//    if number is divisible by 5 then print Buzz
    @Test
    @DisplayName("Divisible by Five")
    @Order(2)
    void testForDivisibleByFive(){
        String expected = "Buzz";
        assertEquals(expected, FizzBuzz.compute(5), "should return Buzz");
    }

//    if number is divisible by 3 & 5 then print FizzBuzz
    @Test
    @DisplayName("Divisible by Three & Five")
    @Order(2)
    void testForDivisibleByThreeAndFive(){
        String expected = "FizzBuzz";
        assertEquals(expected, FizzBuzz.compute(15), "should return FizzBuzz");
    }

//    if number is NOT divisible by 3 or 5 then print the number
    @Test
    @DisplayName("Not Divisible by Three OR Five")
    @Order(2)
    void testForNotDivisibleByThreeOrFive(){
//        String expected = "FizzBuzz";
        int n = 11;
        assertEquals(n+"", FizzBuzz.compute(n), "should return " + n);
    }

}
