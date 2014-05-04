/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.utils;

import de.papaharni.happychest.HappyChest;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Pappi
 */
public final class Task {
    //Arena , interval ( Abstand ), Starter Name , 0 (Aktuelle Runde , Rundenanzahl )
    public static BukkitTask startRound(String a, int interval, String p, int pass, int rounds) {
        if(!HappyChest.getInstance().isArena(a)) {
            Player ad = getPlayer(p);
            if(ad != null)
                Utils.sendMessage(ad, "&cEvent muss beendet werden da Arena verschwunden ist.");
            HappyChest.getInstance().getLogger().log(Level.WARNING, "Konnte Arena " + a + " nicht starten da diese nicht mehr vorhanden ist.");
            return null;
        }
        for(Player pl: Bukkit.getOnlinePlayers()) {
            if(!pl.getWorld().getUID().equals(HappyChest.getInstance().getArena(a).getPos1().getWorld().getUID()))
                Utils.sendMessage(pl, "&eIn 2 Minuten beginnt ein HappyChest Event auf der Welt " + HappyChest.getInstance().getArena(a).getPos1().getWorld().getName());
        }
        return Bukkit.getServer().getScheduler().runTaskLater(HappyChest.getInstance(), new setNewRound(a, interval, p, pass, rounds), (60*2*20));
    }
    
    public static BukkitTask nextRound(String a, int interval, String p, int pass, int rounds) {
        return Bukkit.getServer().getScheduler().runTaskLater(HappyChest.getInstance(), new setNewRound(a, interval, p, pass, rounds), (interval*20*60));
    }
    
    private static Player getPlayer(String p) {
        Player pl = Bukkit.getPlayerExact(p);
        if(pl != null)
            return pl;
        for(Player pla : Bukkit.getOnlinePlayers()) {
            if(pla.isOp())
                return pla;
        }
        
        for(Player pla: Bukkit.getOnlinePlayers()) {
            if(pla.hasPermission("amcserver.team")) 
                return pla;
        }
        return null;
    }
}

class setNewRound implements Runnable {

    private final String _a;
    private final int _interval;
    private final String _p;
    private final int _pass;
    private final int _rounds;
    public setNewRound(String a, int interval, String p, int pass, int rounds) {
        _a = a;
        _interval = interval;
        _p = p;
        _pass = pass;
        _rounds = rounds;
    }

    @Override
    public void run() {
        HappyChest.getInstance().clearOnEventEnd(_a);
        Player p = getPlayer(_p);
        if(!HappyChest.getInstance().isArena(_a)) {
            if(p != null)
                Utils.sendMessage(p, "&cEvent muss beendet werden da Arena verschwunden ist.");
            HappyChest.getInstance().getLogger().log(Level.WARNING, "Konnte Arena " + _a + " nicht starten da diese nicht mehr vorhanden ist.");
            return;
        }
        Arena a = HappyChest.getInstance().getArena(_a);
        if(a.getItemCount() < 1) {
            if(p != null)
                Utils.sendMessage(p, "&cEvent muss beendet werden da Arena " + _a + " keine Items hat. Mindestens 1 benötigt.");
            HappyChest.getInstance().getLogger().log(Level.WARNING, "Konnte keine Items für Arena " + _a + " finden.");
            return;
        }
        
        if(a.getChests().size() < 2) {
            if(p != null)
                Utils.sendMessage(p, "&cEvent muss beendet werden da Arena " + _a + " keine Truhen mehr hat. Mindestens 2 benötigt.");
            HappyChest.getInstance().getLogger().log(Level.WARNING, "Konnte keine Items für Arena " + _a + " finden.");
            return;
        }
        
        if(_rounds > -1 && _pass == _rounds) {
            Utils.worldBroadcast("&cDies war die letze Runde in Arena " + a.getName() + ". Bis zum nächsten mal.", a.getPos1().getWorld().getName());
            ArenaWorks.endArena(_a, p);
            return;
        }
        
        //Ermittle neue Chest Location
        Location loc = getRandomLocation(a);
        HappyChest.getInstance().getCurChests().put(_a, loc);
        
        //Erstelle eine neue Item List
        List<String> itemList = getRandomItems(a);
        HappyChest.getInstance().getLogger().log(Level.INFO, "ItemList hat für diese Runde " + itemList.size() + " items in sich.");
        if(a.getOneForAll()) {
            Block b = Bukkit.getWorld(loc.getWorld().getUID()).getBlockAt(loc);
            if(b != null) {
                ItemStack[] items = new ItemStack[itemList.size()];
                int i = 0;
                for(String str: itemList) {
                    ItemStack item = Items.getItem(str);
                    if(item == null)
                        continue;
                    items[i] = item;
                    i++;
                }
                if(b.getState() instanceof DoubleChest) {
                    if(p != null)
                        Utils.sendMessage(p, "&cEs ist eine DoubleChest");
                    DoubleChest c = (DoubleChest)b.getState();
                    c.getInventory().setContents(items);
                    b.getState().update();
                } else if(b.getState() instanceof Chest) {
                    if(p != null)
                        Utils.sendMessage(p, "&cEs ist eine Chest");
                    Chest c = (Chest)b.getState();
                    c.getBlockInventory().setContents(items);
                    c.update();
                } else {
                    if(p != null)
                        Utils.sendMessage(p, "&cEvent muss beendet werden da in Arena " + _a + " an Position " + loc.getWorld() + ":" + loc.getBlockX() + " , " + loc.getBlockY() + " , " + loc.getBlockZ() + " keine Truhe mehr gefunden wurde.");
                    HappyChest.getInstance().getLogger().log(Level.WARNING, "Event muss beendet werden da in Arena " + _a + " an Position " + loc.getWorld() + ":" + loc.getBlockX() + " , " + loc.getBlockY() + " , " + loc.getBlockZ() + " keine Truhe mehr gefunden wurde.");
                    return;
                }
            } else {
                if(p != null)
                    Utils.sendMessage(p, "&cEvent muss beendet werden da in Arena " + _a + " an Position " + loc.getWorld() + ":" + loc.getBlockX() + " , " + loc.getBlockY() + " , " + loc.getBlockZ() + " keine Truhe mehr gefunden wurde.");
                HappyChest.getInstance().getLogger().log(Level.WARNING, "Event muss beendet werden da in Arena " + _a + " an Position " + loc.getWorld() + ":" + loc.getBlockX() + " , " + loc.getBlockY() + " , " + loc.getBlockZ() + " keine Truhe mehr gefunden wurde.");
                return;
            }
        } else {
            HappyChest.getInstance().addRoundRewards(_a, itemList);
        }
        
        Utils.worldBroadcast("&cEine neue Runde in " + a.getName() + " hat begonnen. Viel Glück beim Truhe suchen.", a.getPos1().getWorld().getName());
        
        BukkitTask t = Task.nextRound(_a, _interval, _p, ((_pass > -1)?(_pass+1):-1), _rounds);
        HappyChest.getInstance().setArenaTask(_a, t);
    }
    
    private Player getPlayer(String p) {
        Player pl = Bukkit.getPlayerExact(p);
        if(pl != null)
            return pl;
        for(Player pla : Bukkit.getOnlinePlayers()) {
            if(pla.isOp())
                return pla;
        }
        
        for(Player pla: Bukkit.getOnlinePlayers()) {
            if(pla.hasPermission("amcserver.team")) 
                return pla;
        }
        return null;
    }
    
    private Location getRandomLocation(Arena a) {
        return a.getChests().get(new Random().nextInt(a.getChests().size()));
    }
    
    private List<String> getRandomItems(Arena a) {
        List<String> items = a.getItemList();
        int max = (int)Math.floor((double)(items.size()/2));
        if(max < 1)
            max = 1;
        max = Rnd.get(1,max);
        Collections.shuffle(items);
        return items.subList(0, max);
    }
    
    public void cancelArena(String a) {
        HappyChest.getInstance().getCurChests().remove(a);
        HappyChest.getInstance().delRoundRewards(a);
        
    }
}
