/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.utils;

import de.papaharni.happychest.HappyChest;
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
    public static BukkitTask startRound(String a, int interval, String p) {
        return Bukkit.getServer().getScheduler().runTaskLater(HappyChest.getInstance(), new setNewRound(a, interval, p), (60*2*20));
    }
    
    public static BukkitTask nextRound(String a, int interval, String p) {
        return Bukkit.getServer().getScheduler().runTaskLater(HappyChest.getInstance(), new setNewRound(a, interval, p), (interval*20*60));
    }
}

class setNewRound implements Runnable {

    private final String _a;
    private final int _interval;
    private final String _p;
    public setNewRound(String a, int interval, String p) {
        _a = a;
        _interval = interval;
        _p = p;
    }

    @Override
    public void run() {
        Player p = getPlayer(_p);
        if(!HappyChest.getInstance().isArena(_a)) {
            if(p != null)
                p.sendMessage("$f[$2HappyChest$f]$cEvent muss beendet werden da Arena verschwunden ist.");
            HappyChest.getInstance().getLogger().log(Level.WARNING, "Konnte Arena " + _a + " nicht starten da diese nicht mehr vorhanden ist.");
            HappyChest.getInstance().cancelArenaTask(_a);
            return;
        }
        HappyChest.getInstance().getArena(_a).reloadChests(null);
        Arena a = HappyChest.getInstance().getArena(_a);
        if(a.getItemCount() < 1) {
            if(p != null)
                p.sendMessage("$f[$2HappyChest$f]$cEvent muss beendet werden da Arena " + _a + " keine Items hat. Mindestens 1 benötigt.");
            HappyChest.getInstance().getLogger().log(Level.WARNING, "Konnte keine Items für Arena " + _a + " finden.");
            HappyChest.getInstance().cancelArenaTask(_a);
            return;
        }
        
        if(a.getChests().size() < 2) {
            if(p != null)
                p.sendMessage("$f[$2HappyChest$f]$cEvent muss beendet werden da Arena " + _a + " keine Truhen mehr hat. Mindestens 2 benötigt.");
            HappyChest.getInstance().getLogger().log(Level.WARNING, "Konnte keine Items für Arena " + _a + " finden.");
            HappyChest.getInstance().cancelArenaTask(_a);
            return;
        }
        
        Location loc = getRandomLocation(a, p);
        a.getChests().get(new Random().nextInt(a.getChests().size()));
        HappyChest.getInstance().getCurChests().put(_a, loc);
        
        
        
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
    
    private void worldBroadcast(String msg, String w) {
        for(Player p: Bukkit.getOnlinePlayers()) {
            if(!p.getLocation().getWorld().getName().equalsIgnoreCase(w))
                continue;
            p.sendMessage("$f[$2HappyChest$f]$eSoeben wurde eine neue Truhe gesichtet. Beeil dich , du hast nur " + _interval + " Minuten Zeit.");
        }
    }
    
    private Location getRandomLocation(Arena a, Player p) {
        
        return null;
    }
    
}
