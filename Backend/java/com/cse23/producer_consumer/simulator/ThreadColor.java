package com.cse23.producer_consumer.simulator;

import java.util.ArrayList;
import java.util.Random;

public class ThreadColor {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    private static final String[] colors= new String[]{ANSI_RESET,ANSI_BLACK,ANSI_BLUE,ANSI_CYAN,ANSI_GREEN,ANSI_PURPLE,ANSI_RED};

    private static char[] colourNums= {'0','1' , '2' , '3' , '4','5','6','7','8','9','A','B','C','D','E','F'};

    public static String getAColor(){

        return  colors[new Random().nextInt(colors.length)];
    }

    public static String getARandomColour(){
        String colour = "#";
        for(int i = 0 ;i<6 ; i++)
            colour += colourNums[new Random().nextInt(colourNums.length)];
        return colour;
    }





}
