/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.commands;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.regions.Region;
import de.papaharni.happychest.HappyChest;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Pappi
 */
public class create {
    public void create(String name, Player p) {
        try {
            LocalSession s = HappyChest.getInstance().getWG().getWorldEdit().getSession(p);
            LocalWorld w = s.getSelectionWorld();
            
            try {
                Region r = s.getSelection(w);
                Location loc_min = new Location(Bukkit.getWorld(r.getWorld().getName()), r.getMinimumPoint().getBlockX(), r.getMinimumPoint().getBlockY(), r.getMinimumPoint().getBlockZ());
                Location loc_max = new Location(Bukkit.getWorld(r.getWorld().getName()), r.getMaximumPoint().getBlockX(), r.getMaximumPoint().getBlockY(), r.getMaximumPoint().getBlockZ());
                
                List<Location> chests = getChests(loc_min, loc_max);
            } catch(IncompleteRegionException e) {
                //Du hast noch keine Region selected
            }
        } catch(CommandException e) {
            //Fehler im Plugin
        }
        
    }
    
    private List<Location> getChests(Location loc_min, Location loc_max) {
        List<Location> chests = new ArrayList<>();
        
        return chests;
    }
}
