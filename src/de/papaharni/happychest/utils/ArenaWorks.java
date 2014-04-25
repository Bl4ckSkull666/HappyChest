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
            return;
        }
        
        if(!HappyChest.getInstance().isAllowMarking(p.getName())) {
            p.sendMessage("$f[$2HappyChest$f]$eDu hast keine Makier Erlaubnis.");
            return;
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
            } catch (Exception ex) {
                p.sendMessage("Kann es sein das du keine Region selected hast?");
                return;
            }
        } else {
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
        }
        HappyChest.getInstance().delMarking(p.getName(), "all");
    }
    
    public static boolean addItemToArena(Player p, String a, String[] aitem) {
        if(!HappyChest.getInstance().isArena(a)) {
            p.sendMessage("$f[$2HappyChest$f]$cDie angegebene Arena wurde nicht gefunden.");
            return false;
        }
        
        String str = Utils.implodeArray(aitem, " ");
        if(!Items.isItem(str)) {
            p.sendMessage("$f[$2HappyChest$f]$cEs ist ein Fehler in dem Item. Bitte prüfe dies.");
            return false;
        }
        
        HappyChest.getInstance().getArena(a).addItem(str);
        p.sendMessage("$f[$2HappyChest$f]$eItem wurde erfolgreich zu Arena hinzugefügt.");
        return false;
    }
    
    public static void listArenaItems(Player p, String a) {
        if(!HappyChest.getInstance().isArena(a)) {
            p.sendMessage("$f[$2HappyChest$f]$cDie angegebene Arena wurde nicht gefunden.");
            return;
        }
        
        Arena area = HappyChest.getInstance().getArena(a);
        if(area.getItemCount() < 1) {
            p.sendMessage("$f[$2HappyChest$f]$cArena " + area.getName() + " besitzt aktuell keine Items.");
            return;
        }
        
        p.sendMessage("$f[$2HappyChest$f]$cListe alle " + area.getItemCount() + " Items für Arena " + area.getName() + " auf.");
        List<String> list = HappyChest.getInstance().getArena(a).getItemList();
        for(int i = 1; i <= list.size(); i++) {
            if(i % 2 == 0) {
                p.sendMessage("$l" + i + ". $r$7" + list.get(i-1));
            } else {
                p.sendMessage("$l" + i + ". $r$8" + list.get(i-1));
            }
        }
        p.sendMessage("$f[$2HappyChest$f]$cVerwende /hch removeitem " + area.getName() + " (id)");
    }
    
    public static void removeArenaItem(Player p, String a, int iid) {
        if(!HappyChest.getInstance().isArena(a)) {
            p.sendMessage("$f[$2HappyChest$f]$cDie angegebene Arena wurde nicht gefunden.");
            return;
        }
        
        Arena area = HappyChest.getInstance().getArena(a);
        if(area.getItemCount() < 1) {
            p.sendMessage("$f[$2HappyChest$f]$cArena " + area.getName() + " besitzt aktuell keine Items.");
            return;
        }
        
        if(iid < 1) {
            p.sendMessage("$f[$2HappyChest$f]$cBitte gib eine Zahl zwischen 1 und " + area.getItemCount() + " aus.");
            return;
        }
        
        if(area.getItemCount() < iid) {
            p.sendMessage("$f[$2HappyChest$f]$cArena " + area.getName() + " besitzt keine " + iid + " Items. Bitte verwende eine kleinere Zahl.");
            return;
        }
        
        HappyChest.getInstance().getArena(a).getItemList().remove((iid-1));
    }
}
