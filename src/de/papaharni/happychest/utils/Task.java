/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.utils;

import de.papaharni.happychest.HappyChest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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
                ad.sendMessage("$f[$2HappyChest$f]$cEvent muss beendet werden da Arena verschwunden ist.");
            HappyChest.getInstance().getLogger().log(Level.WARNING, "Konnte Arena " + a + " nicht starten da diese nicht mehr vorhanden ist.");
            return null;
        }
        for(Player pl: Bukkit.getOnlinePlayers()) {
            pl.sendMessage("$f[$2HappyChest$f]$eIn 2 Minuten beginnt ein HappyChest Event auf der Welt " + HappyChest.getInstance().getArena(a).getName());
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
        HappyChest.getInstance().cancelArenaTask(_a);
        Player p = getPlayer(_p);
        if(!HappyChest.getInstance().isArena(_a)) {
            if(p != null)
                p.sendMessage("$f[$2HappyChest$f]$cEvent muss beendet werden da Arena verschwunden ist.");
            HappyChest.getInstance().getLogger().log(Level.WARNING, "Konnte Arena " + _a + " nicht starten da diese nicht mehr vorhanden ist.");
            return;
        }
        HappyChest.getInstance().getArena(_a).reloadChests(null);
        Arena a = HappyChest.getInstance().getArena(_a);
        if(a.getItemCount() < 1) {
            if(p != null)
                p.sendMessage("$f[$2HappyChest$f]$cEvent muss beendet werden da Arena " + _a + " keine Items hat. Mindestens 1 benötigt.");
            HappyChest.getInstance().getLogger().log(Level.WARNING, "Konnte keine Items für Arena " + _a + " finden.");
            return;
        }
        
        if(a.getChests().size() < 2) {
            if(p != null)
                p.sendMessage("$f[$2HappyChest$f]$cEvent muss beendet werden da Arena " + _a + " keine Truhen mehr hat. Mindestens 2 benötigt.");
            HappyChest.getInstance().getLogger().log(Level.WARNING, "Konnte keine Items für Arena " + _a + " finden.");
            return;
        }
        
        if(_pass > -1 && _pass == _rounds) {
            Utils.worldBroadcast("$f[$2HappyChest$f]$cDies war die letze Runde in Arena " + a.getName() + ". Bis zum nächsten mal.", a.getPos1().getWorld().getName());
            ArenaWorks.endArena(_a, p);
            return;
        }
        
        //Erstelle eine neue Item List
        List<String> itemList = getRandomItems(a);
        HappyChest.getInstance().addRoundRewards(_a, itemList);
        
        //Ermittle neue Chest Location
        Location loc = getRandomLocation(a);
        HappyChest.getInstance().getCurChests().put(_a, loc);
        
        Utils.worldBroadcast("$f[$2HappyChest$f]$cEine neue Runde in " + a.getName() + " hat begonnen. Viel Glück beim Truhe suchen.", a.getPos1().getWorld().getName());
        
        BukkitTask t = Task.nextRound(_a, _interval, _p, _pass, (_rounds+1));
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
        Collections.shuffle(items);
        return items.subList(0, ((int) (Math.random() * items.size() * 0.55)));
    }
    
    public void cancelArena(String a) {
        HappyChest.getInstance().getCurChests().remove(a);
        HappyChest.getInstance().delRoundRewards(a);
        
    }
}
