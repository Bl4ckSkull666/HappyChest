/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.events;

import de.papaharni.happychest.HappyChest;
import de.papaharni.happychest.utils.Blocks;
import de.papaharni.happychest.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
        
        if(!event.getItem().getType().equals(Material.GOLD_SWORD) && HappyChest.getInstance().getWG() == null)
            return;
        
        if(HappyChest.getInstance().getWG() != null) {
            try {
                if(HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMinimumPoint() != null && HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMaximumPoint() != null) {
                    Location loc_min = HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMinimumPoint();
                    Location loc_max = HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMaximumPoint();

                    int chests = Blocks.getChestsCountS(loc_min, loc_max);
                    int total = HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getArea();

                    if(chests < 2) {
                        Utils.sendMessage(p, "$f[$2HappyChest$f]$eEs sind " + String.valueOf(total) + " Blöcke makiert aber es fehlen mindestens 2 Truhen aktuell in diesem Bereich.");
                        return;
                    }
                    Utils.sendMessage(p, "$f[$2HappyChest$f]$eEs sind " + String.valueOf(total) + " Blöcke makiert mit insgesamt " + String.valueOf(chests) + " Truhen.");
                }
            } catch (Exception ex) {
                Utils.sendMessage(p, "Kann es sein das du keine Region selected hast?");
            }
        } else {
            if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Utils.sendMessage(p, "$f[$2HappyChest$f]$eLinke makierung gesetzt.");
                _plugin.setMarking(p.getName(), "left", b.getLocation());
            }
            if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Utils.sendMessage(p, "$f[$2HappyChest$f]$eRechte makierung gesetzt.");
                _plugin.setMarking(p.getName(), "right", b.getLocation());
            }

            if(_plugin.isMarking(p.getName(), "left") && _plugin.isMarking(p.getName(), "right")) {
                Location loc1 = _plugin.getMarking(p.getName(), "left");
                Location loc2 = _plugin.getMarking(p.getName(), "right");
                int x = Math.max(loc1.getBlockX(), loc2.getBlockX())-Math.min(loc1.getBlockX(), loc2.getBlockX());
                int y = Math.max(loc1.getBlockY(), loc2.getBlockY())-Math.min(loc1.getBlockY(), loc2.getBlockY());
                int z = Math.max(loc1.getBlockZ(), loc2.getBlockZ())-Math.min(loc1.getBlockZ(), loc2.getBlockZ());
                int total = ((x*z)*y);
                int chests = Blocks.getChestsCountU(loc1, loc2);

                if(chests < 2) {
                    Utils.sendMessage(p, "$f[$2HappyChest$f]$eEs sind " + String.valueOf(total) + " Blöcke makiert aber es fehlen mindestens 2 Truhen aktuell in diesem Bereich.");
                    return;
                }
                Utils.sendMessage(p, "$f[$2HappyChest$f]$eEs sind " + String.valueOf(total) + " Blöcke makiert mit insgesamt " + String.valueOf(chests) + " Truhen.");
            }
        }
    }
    
    public void onPlayerMove(PlayerMoveEvent e) {
    //Prüfe ob spieler in Arena ist /
    //Ist e.getFrom() an Spawn Position dann setzte e.getTo() zu e.getFrom()
    //e.setTo(e.getFrom());
    }
}
