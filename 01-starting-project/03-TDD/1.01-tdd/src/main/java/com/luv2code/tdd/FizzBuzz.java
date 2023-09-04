package com.luv2code.tdd;

public class FizzBuzz {

    /*
    static String compute(int n){
        if(n % 5 == 0 && n % 3 == 0)
            return "FizzBuzz";
        else if(n % 3 == 0)
            return "Fizz";
        else if(n % 5 == 0)
            return "Buzz";
        return n + "";
    }
    */

//    Refactored version:
    static String compute(int n){
        StringBuilder result = new StringBuilder();
        if(n % 3 == 0)
            result.append("Fizz");
        if(n % 5 == 0)
            result.append("Buzz");
        if(result.isEmpty())
            result.append(n);
        return result.toString();
    }

}
