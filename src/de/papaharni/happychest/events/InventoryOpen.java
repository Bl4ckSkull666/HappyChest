/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.events;

import de.papaharni.happychest.HappyChest;
import de.papaharni.happychest.utils.Arena;
import de.papaharni.happychest.utils.Items;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Pappi
 */
public class InventoryOpen implements Listener {
    
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Player p = (Player)e.getPlayer();
        if(p == null || !e.getView().getType().equals(InventoryType.CHEST) && !e.getView().getType().equals(InventoryType.ENDER_CHEST)) {
            return;
        }
        
        if(e.getInventory().getHolder() instanceof DoubleChest) {
            DoubleChest c = (DoubleChest)e.getInventory().getHolder();
            checkDoubleChest(p, c.getLocation(), c);
            return;
        }
        if(e.getInventory().getHolder() instanceof Chest) {
            Chest c = (Chest)e.getInventory().getHolder();
            checkChest(p, c.getLocation(), c);
            return;
        }
        
    }
    
    public boolean checkChest(Player p, Location loc, Chest c) {
        for(Map.Entry<String, Location> e: HappyChest.getInstance().getCurChests().entrySet()) {
            Arena a = HappyChest.getInstance().getArena(e.getKey());
            if(!a.isInside(loc))
                continue;
            
            if(e.getValue().distance(loc) > 1.0) {
                p.sendMessage("$f[$2HappyChest$f]$eSchade , das ist leider nicht die Schatztruhe.");
                return false;
            }
            
            if(a.getOneForAll()) {
                p.sendMessage("$f[$2HappyChest$f]$eGlückwunsch , du hast die richtige Truhe gefunden.");
                if(c.getInventory().getContents().length < 1) {
                    p.sendMessage("$f[$2HappyChest$f]$eLeider wurde die Kiste in dieser Runde schon geplündert.");
                }
                return true;
            }
            
            ItemStack[] items = getItemStack(e.getKey(), p, c.getInventory().getSize());
            c.getInventory().setContents(items);
            p.sendMessage("$f[$2HappyChest$f]$eGlückwunsch , du hast die richtige Truhe gefunden.");
            return true;
        } 
        return false;
    }
    
    public boolean checkDoubleChest(Player p, Location loc, DoubleChest c) {
        for(Map.Entry<String, Location> e: HappyChest.getInstance().getCurChests().entrySet()) {
            Arena a = HappyChest.getInstance().getArena(e.getKey());
            if(!a.isInside(loc))
                continue;
            
            if(e.getValue().distance(loc) > 1.0) {
                p.sendMessage("$f[$2HappyChest$f]$eSchade , das ist leider nicht die Schatztruhe.");
                return false;
            }
            
            ItemStack[] items = getItemStack(e.getKey(), p, c.getInventory().getSize());
            c.getInventory().setContents(items);
            return true;
        }
        return false;
    }
    
    public ItemStack[] getItemStack(String a, Player p, int csize) {
        if(!HappyChest.getInstance().getUsedPlayersList(a).equals(p.getName())) {
            if(HappyChest.getInstance().hasReste(a, p.getName())) {
                p.sendMessage("$f[$2HappyChest$f]$eHier ist das was du noch in der Truhe vergessen hast. Bitte nimm es raus bevor es weg ist.");
                return HappyChest.getInstance().getReste(a, p.getName());
            }
            List<String> list = HappyChest.getInstance().getRoundRewards(a);
            ItemStack[] items = new ItemStack[list.size()];
            int i = 0;
            for(String str: list) {
                ItemStack item = Items.getItem(str);
                if(item == null)
                    continue;
                items[i] = item;
            }
            HappyChest.getInstance().getUsedPlayersList(a).add(p.getName());
            p.sendMessage("$f[$2HappyChest$f]$eGlückwunsch , du hast die richtige Truhe gefunden.");
            return items;
        }
        p.sendMessage("$f[$2HappyChest$f]$eOh du hast die Truhe bereits geplündert. Versuchs in der nächste Runde noch einmal.");
        ItemStack[] items = new ItemStack[csize];
        return items;
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();
        if(p == null || !e.getView().getType().equals(InventoryType.CHEST) && !e.getView().getType().equals(InventoryType.ENDER_CHEST)) {
            return;
        }

        if(e.getInventory().getHolder() instanceof DoubleChest) {
            DoubleChest c = (DoubleChest)e.getInventory().getHolder();
            for(Map.Entry<String, Location> b: HappyChest.getInstance().getCurChests().entrySet()) {
                Arena a = HappyChest.getInstance().getArena(b.getKey());
                if(!a.isInside(c.getLocation()))
                    continue;
                if(b.getValue().distance(c.getLocation()) > 1.0)
                    return;
                if(c.getInventory().getContents().length > 0) {
                    HappyChest.getInstance().setReste(b.getKey(), p.getName(), c.getInventory().getContents());
                } else {
                    HappyChest.getInstance().getUsedPlayersList(b.getKey()).add(p.getName());
                }
            }
            return;
        }
        if(e.getInventory().getHolder() instanceof Chest) {
            Chest c = (Chest)e.getInventory().getHolder();
            for(Map.Entry<String, Location> b: HappyChest.getInstance().getCurChests().entrySet()) {
                Arena a = HappyChest.getInstance().getArena(b.getKey());
                if(!a.isInside(c.getLocation()))
                    continue;
                if(b.getValue().distance(c.getLocation()) > 1.0)
                    return;
                if(c.getInventory().getContents().length > 0) {
                    HappyChest.getInstance().setReste(b.getKey(), p.getName(), c.getInventory().getContents());
                } else {
                    HappyChest.getInstance().getUsedPlayersList(b.getKey()).add(p.getName());
                }
            }
            return;
        }
    }
}
