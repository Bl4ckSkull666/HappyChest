/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.utils;

import de.papaharni.happychest.HappyChest;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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
        int i = 1;
        for(int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x <= mx; x++) {
            for(int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z <= mz; z++) {
                for(int y = Math.min(loc1.getBlockY(), loc2.getBlockY()); y <= my; y++) {
                    Block b = Bukkit.getWorld(loc1.getWorld().getUID()).getBlockAt(x, y, z);
                    if(b.getType() == Material.CHEST || b.getType() == Material.ENDER_CHEST || b.getType() == Material.TRAPPED_CHEST) {
                        HappyChest.getInstance().getLogger().log(Level.INFO, "Block " + i + " ist eine Chest.");
                        if(b instanceof DoubleChest) {
                            DoubleChest chest = (DoubleChest)b;
                            chests.add(chest.getLocation());
                            continue;
                        }
                        if(b instanceof Chest) {
                            chests.add(b.getLocation());
                            continue;
                        }
                    }
                    i++;
                }
                i++;
            }
            i++;
        }
        HappyChest.getInstance().getLogger().log(Level.INFO, "Es wurden " + i + " Blöcke durchsucht.");
        return chests.size();
    }
    
    public static int getChestsCountS(Location loc1, Location loc2) {
        List<Location> chests = new ArrayList<>();
        int i = 1;
        for(int x = loc1.getBlockX(); x <= loc2.getBlockX(); x++) {
            for(int z = loc1.getBlockZ(); z <= loc2.getBlockZ(); z++) {
                for(int y = loc1.getBlockY(); y <= loc2.getBlockY(); y++) {
                    Block b = Bukkit.getWorld(loc1.getWorld().getUID()).getBlockAt(x, y, z);
                    if(b.getType() == Material.CHEST || b.getType() == Material.ENDER_CHEST || b.getType() == Material.TRAPPED_CHEST) {
                        HappyChest.getInstance().getLogger().log(Level.INFO, "Block " + i + " ist eine Chest.");
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
                    i++;
                }
                i++;
            }
            i++;
        }
        HappyChest.getInstance().getLogger().log(Level.INFO, "Es wurden " + i + " Blöcke durchsucht.");
        return chests.size();
    }
    
    public static List<Location> getChestsListU(Location loc1, Location loc2) {
        List<Location> chests = new ArrayList<>();      
        int mx = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int my = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int mz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        int i = 1;
        for(int x = Math.min(loc1.getBlockX(), loc2.getBlockX()); x <= mx; x++) {
            for(int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ()); z <= mz; z++) {
                for(int y = Math.min(loc1.getBlockY(), loc2.getBlockY()); y <= my; y++) {
                    Block b = Bukkit.getWorld(loc1.getWorld().getUID()).getBlockAt(x, y, z);
                    if(b.getType() == Material.CHEST || b.getType() == Material.ENDER_CHEST || b.getType() == Material.TRAPPED_CHEST) {
                        HappyChest.getInstance().getLogger().log(Level.INFO, "Block " + i + " ist eine Chest.");
                        if(b instanceof DoubleChest) {
                            DoubleChest chest = (DoubleChest)b;
                            chests.add(chest.getLocation());
                            continue;
                        }
                        if(b instanceof Chest) {
                            chests.add(b.getLocation());
                        }
                    }
                    i++;
                }
                i++;
            }
            i++;
        }
        HappyChest.getInstance().getLogger().log(Level.INFO, "Es wurden " + i + " Blöcke durchsucht.");
        return chests;
    }
    
    public static List<Location> getChestsListS(Location loc1, Location loc2) {
        List<Location> chests = new ArrayList<>();
        int i = 1;
        for(int x = loc1.getBlockX(); x <= loc2.getBlockX(); x++) {
            for(int z = loc1.getBlockZ(); z <= loc2.getBlockY(); z++) {
                for(int y = loc1.getBlockY(); y <= loc2.getBlockZ(); y++) {
                    Block b = Bukkit.getWorld(loc1.getWorld().getUID()).getBlockAt(x, y, z);
                    if(b.getType() == Material.CHEST || b.getType() == Material.ENDER_CHEST || b.getType() == Material.TRAPPED_CHEST) {
                        HappyChest.getInstance().getLogger().log(Level.INFO, "Block " + i + " ist eine Chest.");
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
                    i++;
                }
                i++;
            }
            i++;
        }
        HappyChest.getInstance().getLogger().log(Level.INFO, "Es wurden " + i + " Blöcke durchsucht.");
        return chests;
    }
}
