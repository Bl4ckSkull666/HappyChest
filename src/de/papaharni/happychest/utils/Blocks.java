/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Pappi
 */
public final class Blocks {
    public static int getChestsCountU(Location loc1, Location loc2) {
        return getChestsListU(loc1, loc2).size();
    }
    
    public static int getChestsCountS(Location loc1, Location loc2) {
        return getChestsListS(loc1, loc2).size();
    }
    
    public static List<Location> getChestsListU(Location loc1, Location loc2) {
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        Location min = new Location(loc1.getWorld(), minX, minY, minZ);
        Location max = new Location(loc2.getWorld(), maxX, maxY, maxZ);
        return getChestsListS(min, max);
    }
    
    public static List<Location> getChestsListS(Location loc1, Location loc2) {
        List<Location> chests = new ArrayList<>();
        List<Location> doublechests = new ArrayList<>();
        for(int x = loc1.getBlockX(); x <= loc2.getBlockX(); x++) {
            for(int z = loc1.getBlockZ(); z <= loc2.getBlockZ(); z++) {
                for(int y = loc1.getBlockY(); y <= loc2.getBlockY(); y++) {
                    Block b = Bukkit.getWorld(loc1.getWorld().getUID()).getBlockAt(x, y, z);
                    if(b.getState().getType() == Material.CHEST || b.getState().getType() == Material.TRAPPED_CHEST) {
                        boolean isCounted = false;
                        for(Location l: doublechests) {
                            if(b.getLocation().distance(l) < 1.1)
                                isCounted = true;
                        }
                        if(isCounted)
                            continue;
                        chests.add(b.getLocation());
                        doublechests.add(b.getLocation());
                    }
                }
            }
        }
        return chests;
    }
    
    public static void clearChest(Location loc) {
        Block b = Bukkit.getWorld(loc.getWorld().getUID()).getBlockAt(loc);
        if(b.getState() instanceof DoubleChest) {
            DoubleChest c = (DoubleChest)b.getState();
            c.getInventory().setContents(new ItemStack[c.getInventory().getSize()]);
            return;
        }
        if(b.getState() instanceof Chest) {
            Chest c = (Chest)b.getState();
            c.getInventory().setContents(new ItemStack[c.getInventory().getSize()]);
            return;
        }
    }
}
