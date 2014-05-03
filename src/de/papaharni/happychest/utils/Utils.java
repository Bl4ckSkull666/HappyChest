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
    public static String implodeArray(String[] inputArray, String glueString, int start) {
        if(inputArray.length > start) {
            String msg = "";
            boolean isFirst = true;
            for (int i=start; i<inputArray.length; i++) {
                if(!isFirst)
                    msg += glueString;
                msg += inputArray[i];
                isFirst = false;
            }
            return msg;
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
            sendMessage(p, "&e" + msg);
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
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&2HappyChest&f]" + msg));
    }
    
    public static Location[] sortLocations(Location loc1, Location loc2) {
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        Location min = new Location(loc1.getWorld(), minX, minY, minZ);
        Location max = new Location(loc2.getWorld(), maxX, maxY, maxZ);
        Location[] locs = {min, max};
        return locs;
    }
}
