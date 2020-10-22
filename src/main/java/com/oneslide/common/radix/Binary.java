package com.oneslide.common.radix;

public class Binary {

    public static String toBinary(int decimal) {
        if (decimal==0){
            return "0";
        }
        StringBuilder builder = new StringBuilder();
        int remainder = 0;
        int quotient = decimal;
        while (quotient != 0) {
            remainder = quotient % 2;
            quotient = quotient / 2;
            builder.insert(0, remainder);
        }
        return builder.toString();
    }
    
}
