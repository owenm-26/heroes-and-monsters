package Validators;

public class Integers {

    public static void validatePositiveIntegers(int... numbers){
        for (int num: numbers){
            if (num < 1) throw new IllegalArgumentException("Values must be positive");
        }
    }
}
