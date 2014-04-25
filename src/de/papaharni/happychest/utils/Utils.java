/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.utils;

/**
 *
 * @author Pappi
 */
public final class Utils {
    public static String implodeArray(String[] inputArray, String glueString) {
        if (inputArray.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(inputArray[0]);
            for (int i=1; i<inputArray.length; i++) {
                if(!inputArray[i].isEmpty() && !inputArray[i].contains("") && inputArray[i] != "") {
                    sb.append(glueString);
                    sb.append(inputArray[i]);
                }
            }
            return sb.toString();
        }
        return "";
    }
    
    public static boolean isNumeric(String str) {
        try {
            int i = Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
