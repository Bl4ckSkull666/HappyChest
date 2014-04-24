/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.commands;

import de.papaharni.happychest.HappyChest;
import de.papaharni.happychest.utils.Arena;
import de.papaharni.happychest.utils.Blocks;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Pappi
 */
public final class Create {
    public static void create(String name, Player p) {
        if(HappyChest.getInstance().isArena(name)) {
            p.sendMessage("$f[$2HappyChest$f]$eEs existiert bereits eine Region mit diesem Namen.");
            return;
        }
        
        if(HappyChest.getInstance().getWG() != null) {
            try {
                if(HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMinimumPoint() != null && HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMaximumPoint() != null) {
                    Location loc_min = HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMinimumPoint();
                    Location loc_max = HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMaximumPoint();
                    
                    List<Location> chests = Blocks.getChestsListS(loc_min, loc_max);
                    if(chests.size() < 2) {
                        p.sendMessage("$f[$2HappyChest$f]$eEs sind zu wenig Truhen im ausgewählten Bereich. Bitte setze noch ein paar Truhen vor dem Erstellen.");
                        return;
                    }
                    
                    Arena a = new Arena(name, loc_min, loc_max, chests);
                    HappyChest.getInstance().addArena(a);
                    p.sendMessage("$f[$2HappyChest$f]$eArena " + name + " wurde erfolgreich erstellt.");
                    return;
                }
            } catch(Exception e) {
            //Fehler im Plugin
            }
        } else if(HappyChest.getInstance().isAllowMarking(p.getName())) {
            Location loc1 = HappyChest.getInstance().getMarking(p.getName(), "left");
            Location loc2 = HappyChest.getInstance().getMarking(p.getName(), "right");
            
            if(loc1 == null || loc2 == null) {
                p.sendMessage("$f[$2HappyChest$f]$eDu hast keinen Markierten Bereich. Bitte hole dieses nach.");
                return;
            }
            
            List<Location> chests = Blocks.getChestsListU(loc1, loc2);
            if(chests.size() < 2) {
                p.sendMessage("$f[$2HappyChest$f]$eEs sind zu wenig Truhen im ausgewählten Bereich. Bitte setze noch ein paar Truhen vor dem Erstellen.");
                return;
            }
            
            Arena a = new Arena(name, loc1, loc2, chests);
            HappyChest.getInstance().addArena(a);
            p.sendMessage("$f[$2HappyChest$f]$eArena " + name + " wurde erfolgreich erstellt.");
            HappyChest.getInstance().denyMarking(p.getName());
            HappyChest.getInstance().delMarking(p.getName(), "all");
            return;
        }
    }
    
    public static void remove(String name, Player p) {
        
    }
}

