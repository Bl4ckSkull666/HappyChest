/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.events;

import de.papaharni.happychest.HappyChest;
import de.papaharni.happychest.utils.Arena;
import de.papaharni.happychest.utils.Items;
import de.papaharni.happychest.utils.Utils;
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
        
        Location loc = null;
        if(e.getInventory().getHolder() instanceof Chest)
            loc = ((Chest)e.getInventory().getHolder()).getLocation();
        else if(e.getInventory().getHolder() instanceof DoubleChest)
            loc = ((Chest)e.getInventory().getHolder()).getLocation();
        
        if(loc != null) {
            for(Map.Entry<String, Location> en: HappyChest.getInstance().getCurChests().entrySet()) {
                Arena a = HappyChest.getInstance().getArena(en.getKey());
                if(!a.isInside(loc))
                    continue;

                if(en.getValue().distance(loc) > 1.0) {
                    Utils.sendMessage(p, "&eSchade , das ist leider nicht die Schatztruhe.");
                    return;
                }
                
                if(a.getOneForAll()) {
                    Utils.sendMessage(p, "&eGlückwunsch , du hast die richtige Truhe gefunden.");
                    if(e.getInventory().getContents().length < 1) {
                        Utils.sendMessage(p, "&eLeider wurde die Kiste in dieser Runde schon geplündert.");
                    }
                    return;
                }

                ItemStack[] items = getItemStack(en.getKey(), p, e.getInventory().getSize());
                if(items.length > 0) {
                    e.getInventory().setContents(items);
                    Utils.sendMessage(p, "&eGlückwunsch , du hast die richtige Truhe gefunden.");
                }
                return;
            }
        }
    }
    
    public ItemStack[] getItemStack(String a, Player p, int csize) {
        if(!HappyChest.getInstance().getUsedPlayersList(a).equals(p.getName())) {
            if(HappyChest.getInstance().hasReste(a, p.getName())) {
                Utils.sendMessage(p, "&eHier ist das was du noch in der Truhe vergessen hast. Bitte nimm es raus bevor es weg ist.");
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
            return items;
        }
        Utils.sendMessage(p, "&eOh du hast die Truhe bereits geplündert. Versuchs in der nächste Runde noch einmal.");
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
                    c.getInventory().setContents(new ItemStack[c.getInventory().getSize()]);
                } else {
                    HappyChest.getInstance().getUsedPlayersList(b.getKey()).add(p.getName());
                }
            }
            return;
        }
    }
}
