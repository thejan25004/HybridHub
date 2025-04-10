package org.example.backend;

public class test {
    public static void main(String[] args) {
        String positiveDoubleRegex = "^\\d*\\.?\\d+$";

        System.out.println("123.45 is double: " + "123.45".matches(positiveDoubleRegex));  // true
        System.out.println("0.99 is double: " + "0.99".matches(positiveDoubleRegex));  // true
        System.out.println(".5 is double: " + ".5".matches(positiveDoubleRegex));  // true
        System.out.println("-12.3 is double: " + "-12.3".matches(positiveDoubleRegex));  // false
    }
}
