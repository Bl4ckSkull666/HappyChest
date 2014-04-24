/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.utils;

import de.papaharni.happychest.HappyChest;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Pappi
 */
public final class ArenaWorks {
    public static void create(Player p, String a) {
        if(HappyChest.getInstance().isArena(a)) {
            p.sendMessage("$f[$2HappyChest$f]$eEs existiert bereits eine Arena mit dem Namen " + a + ". Verwende /hch redefine (Arenaname)");
        }
        if(HappyChest.getInstance().getWG() != null) {
            try {
                if(HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMinimumPoint() == null || HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMaximumPoint() == null) {
                    p.sendMessage("$f[$2HappyChest$f]$eEs fehlen die Makierungen um eine Arena zu erstellen.");
                    return;
                }
                Location loc_min = HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMinimumPoint();
                Location loc_max = HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMaximumPoint();

                List<Location> chests = Blocks.getChestsListS(loc_min, loc_max);
                int total = HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getArea();

                if(chests.size() < 2) {
                    p.sendMessage("$f[$2HappyChest$f]$eEs sind " + String.valueOf(total) + " Blöcke makiert aber es fehlen mindestens 2 Truhen aktuell in diesem Bereich um eine Arena zu erstellen.");
                    return;
                }
                    
                Arena ar = new Arena(a, loc_min, loc_max, chests);
                HappyChest.getInstance().addArena(ar);
                p.sendMessage("$f[$2HappyChest$f]$eArena " + a + " wurde erfolgreich erstellt.");
                return;
            } catch (Exception ex) {
                p.sendMessage("Kann es sein das du keine Region selected hast?");
                return;
            }
        } else if(HappyChest.getInstance().isAllowMarking(p.getName())) {
            if(!HappyChest.getInstance().isMarking(p.getName(), "left") || !HappyChest.getInstance().isMarking(p.getName(), "right")) {
                p.sendMessage("$f[$2HappyChest$f]$eEs fehlen die Makierungen um eine Arena zu erstellen.");
                return;
            }
            
            Location loc1 = HappyChest.getInstance().getMarking(p.getName(), "left");
            Location loc2 = HappyChest.getInstance().getMarking(p.getName(), "right");
            int x = Math.max(loc1.getBlockX(), loc2.getBlockX())-Math.min(loc1.getBlockX(), loc2.getBlockX());
            int y = Math.max(loc1.getBlockY(), loc2.getBlockY())-Math.min(loc1.getBlockY(), loc2.getBlockY());
            int z = Math.max(loc1.getBlockZ(), loc2.getBlockZ())-Math.min(loc1.getBlockZ(), loc2.getBlockZ());
            int total = ((x*z)*y);
            List<Location> chests = Blocks.getChestsListU(loc1, loc2);

            if(chests.size() < 2) {
                p.sendMessage("$f[$2HappyChest$f]$eEs sind " + String.valueOf(total) + " Blöcke makiert aber es fehlen mindestens 2 Truhen aktuell in diesem Bereich.");
                return;
            }
            
            Arena ar = new Arena(a, loc1, loc2, chests);
            HappyChest.getInstance().addArena(ar);
            p.sendMessage("$f[$2HappyChest$f]$eArena " + a + " wurde erfolgreich erstellt.");
            return;
        } else {
            p.sendMessage("$f[$2HappyChest$f]$eUnerwarteter Fehler in der Erstellung einer Arena.");
        }
    }
}
