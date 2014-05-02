/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
        
    public static boolean isDouble(String str) {
        try {
            double i = Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }   
    
    public static void worldBroadcast(String msg, String w) {
        for(Player p: Bukkit.getOnlinePlayers()) {
            if(!p.getLocation().getWorld().getName().equalsIgnoreCase(w))
                continue;
            sendMessage(p, "$f[$2HappyChest$f]$e" + msg);
        }
    }
    
    public static Location getLFST(String str) {
        String[] l = str.split(":");
        if(l.length == 4) {
            if(Bukkit.getWorld(l[0]) != null && isDouble(l[1]) && isDouble(l[2]) && isDouble(l[3])) {
                return new Location(Bukkit.getWorld(l[0]), Double.parseDouble(l[1]), Double.parseDouble(l[2]), Double.parseDouble(l[3]));
            }
        }
        return null;
    }
    
    public static void sendMessage(Player p, String msg) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
}
