/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.events;

import de.papaharni.happychest.HappyChest;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Pappi
 */
public class PlayerInteract implements Listener {
    
    private final HappyChest _plugin;
    
    public PlayerInteract(HappyChest plugin) {
        _plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Block b = event.getClickedBlock();
        if(b == null || p == null || !_plugin.isAllowMarking(p.getName()) || !event.hasItem())
            return;
        
        if(!event.getItem().getType().equals(Material.GOLD_SWORD))
            return;
        
        if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            p.sendMessage("$f[$2HappyChest$f]$eLinke makierung gesetzt.");
            _plugin.setMarking(p.getName(), "left", b.getLocation());
        }
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            p.sendMessage("$f[$2HappyChest$f]$eRechte makierung gesetzt.");
            _plugin.setMarking(p.getName(), "right", b.getLocation());
        }
        
        if(_plugin.isMarking(p.getName(), "left") && _plugin.isMarking(p.getName(), "right")) {
            Location loc1 = _plugin.getMarking(p.getName(), "left");
            Location loc2 = _plugin.getMarking(p.getName(), "right");
            int x = Math.max(loc1.getBlockX(), loc2.getBlockX())-Math.min(loc1.getBlockX(), loc2.getBlockX());
            int y = Math.max(loc1.getBlockY(), loc2.getBlockY())-Math.min(loc1.getBlockY(), loc2.getBlockY());
            int z = Math.max(loc1.getBlockZ(), loc2.getBlockZ())-Math.min(loc1.getBlockZ(), loc2.getBlockZ());
            int total = ((x*z)*y);
            int chests = getChests(loc1, loc2);
            
            if(chests < 2) {
                p.sendMessage("$f[$2HappyChest$f]$eEs sind " + String.valueOf(total) + " Blöcke makiert aber es fehlen mindestens 2 Truhen aktuell in diesem Bereich.");
                return;
            }
            p.sendMessage("$f[$2HappyChest$f]$eEs sind " + String.valueOf(total) + " Blöcke makiert mit insgesamt " + String.valueOf(chests) + " Truhen.");
        }
    }
    
    private int getChests(Location loc1, Location loc2) {
        List<Location> chests = new ArrayList<>();
        int x = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int y = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int z = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        
        int mx = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int my = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int mz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        
        for(x = x; x <= mx; x++) {
            for(z = z; z <= mz; z++) {
                for(y = y; y <= my; y++) {
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
    
}
