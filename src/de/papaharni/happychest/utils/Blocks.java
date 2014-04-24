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

/**
 *
 * @author Pappi
 */
public final class Blocks {
    public static int getChestsCountU(Location loc1, Location loc2) {
        List<Location> chests = new ArrayList<>();
        int mx = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int my = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int mz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        
        for(int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x <= mx; x++) {
            for(int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z <= mz; z++) {
                for(int y = Math.min(loc1.getBlockY(), loc2.getBlockY()); y <= my; y++) {
                    Block b = Bukkit.getWorld(loc1.getWorld().getUID()).getBlockAt(x, y, z);
                    if(b.getType().equals(Material.CHEST) || b.getType().equals(Material.ENDER_CHEST) || b.getType().equals(Material.TRAPPED_CHEST)) {
                        if(b instanceof DoubleChest) {
                            DoubleChest chest = (DoubleChest)b;
                            chests.add(chest.getLocation());
                            continue;
                        }
                        if(b instanceof Chest) {
                            Chest chest = (Chest)b;
                            chests.add(chest.getLocation());
                            continue;
                        }
                    }
                }
            }
        }
        return chests.size();
    }
    
    public static int getChestsCountS(Location loc1, Location loc2) {
        List<Location> chests = new ArrayList<>();
        for(int x = loc1.getBlockX(); x <= loc2.getBlockX(); x++) {
            for(int z = loc1.getBlockZ(); z <= loc2.getBlockZ(); z++) {
                for(int y = loc1.getBlockY(); y <= loc2.getBlockY(); y++) {
                    Block b = Bukkit.getWorld(loc1.getWorld().getUID()).getBlockAt(x, y, z);
                    if(b.getType().equals(Material.CHEST) || b.getType().equals(Material.ENDER_CHEST) || b.getType().equals(Material.TRAPPED_CHEST)) {
                        if(b instanceof DoubleChest) {
                            DoubleChest chest = (DoubleChest)b;
                            chests.add(chest.getLocation());
                            continue;
                        }
                        if(b instanceof Chest) {
                            Chest chest = (Chest)b;
                            chests.add(chest.getLocation());
                            continue;
                        }
                    }
                }
            }
        }
        return chests.size();
    }
    
    public static List<Location> getChestsListU(Location loc1, Location loc2) {
        List<Location> chests = new ArrayList<>();      
        int mx = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int my = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int mz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        
        for(int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x <= mx; x++) {
            for(int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z <= mz; z++) {
                for(int y = Math.min(loc1.getBlockY(), loc2.getBlockY()); y <= my; y++) {
                    Block b = Bukkit.getWorld(loc1.getWorld().getUID()).getBlockAt(x, y, z);
                    if(b.getType().equals(Material.CHEST) || b.getType().equals(Material.ENDER_CHEST) || b.getType().equals(Material.TRAPPED_CHEST)) {
                        if(b instanceof DoubleChest) {
                            DoubleChest chest = (DoubleChest)b;
                            chests.add(chest.getLocation());
                            continue;
                        }
                        if(b instanceof Chest) {
                            Chest chest = (Chest)b;
                            chests.add(chest.getLocation());
                        }
                    }
                }
            }
        }
        return chests;
    }
    
    public static List<Location> getChestsListS(Location loc1, Location loc2) {
        List<Location> chests = new ArrayList<>();
        for(int x = loc1.getBlockX(); x <= loc2.getBlockX(); x++) {
            for(int z = loc1.getBlockZ(); z <= loc2.getBlockY(); z++) {
                for(int y = loc1.getBlockY(); y <= loc2.getBlockZ(); y++) {
                    Block b = Bukkit.getWorld(loc1.getWorld().getUID()).getBlockAt(x, y, z);
                    if(b.getType().equals(Material.CHEST) || b.getType().equals(Material.ENDER_CHEST) || b.getType().equals(Material.TRAPPED_CHEST)) {
                        if(b instanceof DoubleChest) {
                            DoubleChest chest = (DoubleChest)b;
                            chests.add(chest.getLocation());
                            continue;
                        }
                        if(b instanceof Chest) {
                            Chest chest = (Chest)b;
                            chests.add(chest.getLocation());
                        }
                    }
                }
            }
        }
        return chests;
    }
}
