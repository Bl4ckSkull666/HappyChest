/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.utils;

import de.papaharni.happychest.HappyChest;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Pappi
 */
public final class ArenaWorks {
    public static void create(Player p, String a) {
        if(HappyChest.getInstance().isArena(a)) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&eEs existiert bereits eine Arena mit dem Namen " + a + ". Verwende /hch redefine (Arenaname)");
            return;
        }
        
        if(!HappyChest.getInstance().isAllowMarking(p.getName())) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&eDu hast keine Makier Erlaubnis.");
            return;
        }
        
        if(HappyChest.getInstance().getWG() != null) {
            try {
                if(HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMinimumPoint() == null || HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMaximumPoint() == null) {
                    Utils.sendMessage(p, "&f[&2HappyChest&f]&eEs fehlen die Makierungen um eine Arena zu erstellen.");
                    return;
                }
                Location loc_min = HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMinimumPoint();
                Location loc_max = HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getMaximumPoint();

                List<Location> chests = Blocks.getChestsListU(loc_min, loc_max);
                int total = HappyChest.getInstance().getWG().getWorldEdit().getSelection(p).getArea();
                
                if(chests.size() < 2) {
                    Utils.sendMessage(p, "&f[&2HappyChest&f]&eEs sind " + String.valueOf(total) + " Blöcke makiert aber es fehlen mindestens 2 Truhen aktuell in diesem Bereich um eine Arena zu erstellen.");
                    return;
                }
                
                
                Arena ar = new Arena(a, loc_min, loc_max, chests);
                HappyChest.getInstance().addArena(ar);
                Utils.sendMessage(p, "&f[&2HappyChest&f]&eArena " + a + " wurde erfolgreich erstellt.");
            } catch (Exception ex) {
                Utils.sendMessage(p, "Kann es sein das du keine Region selected hast?");
                return;
            }
        } else {
            if(!HappyChest.getInstance().isMarking(p.getName(), "left") || !HappyChest.getInstance().isMarking(p.getName(), "right")) {
                Utils.sendMessage(p, "&f[&2HappyChest&f]&eEs fehlen die Makierungen um eine Arena zu erstellen.");
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
                Utils.sendMessage(p, "&f[&2HappyChest&f]&eEs sind " + String.valueOf(total) + " Blöcke makiert aber es fehlen mindestens 2 Truhen aktuell in diesem Bereich.");
                return;
            }
            
            Arena ar = new Arena(a, loc1, loc2, chests);
            HappyChest.getInstance().addArena(ar);
            Utils.sendMessage(p, "&f[&2HappyChest&f]&eArena " + a + " wurde erfolgreich erstellt.");
        }
        HappyChest.getInstance().delMarking(p.getName(), "all");
    }
    
    public static boolean addItemToArena(Player p, String a, String aitem) {
        if(!HappyChest.getInstance().isArena(a)) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&cDie angegebene Arena wurde nicht gefunden.");
            return false;
        }
        
        if(!Items.isItem(aitem)) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&cEs ist ein Fehler in dem Item. Bitte prüfe dies.");
            return false;
        }
        
        HappyChest.getInstance().getArena(a).addItem(aitem);
        Utils.sendMessage(p, "&f[&2HappyChest&f]&eItem wurde erfolgreich zu Arena hinzugefügt.");
        return false;
    }
    
    public static void listArenaItems(Player p, String a) {
        if(!HappyChest.getInstance().isArena(a)) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&cDie angegebene Arena wurde nicht gefunden.");
            return;
        }
        
        Arena area = HappyChest.getInstance().getArena(a);
        if(area.getItemCount() < 1) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&cArena " + area.getName() + " besitzt aktuell keine Items.");
            return;
        }
        
        Utils.sendMessage(p, "&f[&2HappyChest&f]&cListe alle " + area.getItemCount() + " Items für Arena " + area.getName() + " auf.");
        List<String> list = HappyChest.getInstance().getArena(a).getItemList();
        for(int i = 1; i <= list.size(); i++) {
            if(i % 2 == 0) {
                Utils.sendMessage(p, "&o" + i + ". &r&7" + list.get(i-1));
            } else {
                Utils.sendMessage(p, "&o" + i + ". &r&8" + list.get(i-1));
            }
        }
        Utils.sendMessage(p, "&f[&2HappyChest&f]&cVerwende /hch removeitem " + area.getName() + " (id)");
    }
    
    public static void removeArenaItem(Player p, String a, int iid) {
        if(!HappyChest.getInstance().isArena(a)) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&cDie angegebene Arena wurde nicht gefunden.");
            return;
        }
        
        Arena area = HappyChest.getInstance().getArena(a);
        if(area.getItemCount() < 1) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&cArena " + area.getName() + " besitzt aktuell keine Items.");
            return;
        }
        
        if(iid < 1) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&cBitte gib eine Zahl zwischen 1 und " + area.getItemCount() + " aus.");
            return;
        }
        
        if(area.getItemCount() < iid) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&cArena " + area.getName() + " besitzt keine " + iid + " Items. Bitte verwende eine kleinere Zahl.");
            return;
        }
        
        HappyChest.getInstance().getArena(a).getItemList().remove((iid-1));
        Utils.sendMessage(p, "&f[&2HappyChest&f]&cItem " + iid + " wurde erfolgreich gelöscht.");
    }
    
    public static void endArena(String a, Player p) {
        if(!HappyChest.getInstance().isArena(a)) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&cDie angegebene Arena wurde nicht gefunden.");
            return;
        }
        
        HappyChest.getInstance().cancelArenaTask(a);
    }
    
    public static void set4One(Player p, String a) {
        if(!HappyChest.getInstance().isArena(a)) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&cDie angegebene Arena wurde nicht gefunden.");
            return;
        }
        if(HappyChest.getInstance().getArena(a).getOneForAll()) {
            HappyChest.getInstance().getArena(a).setOneForAll(false);
            Utils.sendMessage(p, "&f[&2HappyChest&f]&eDie Truhe ist nun für jeden verfügbar in Arena " + HappyChest.getInstance().getArena(a).getName() + ".");
        } else {
            HappyChest.getInstance().getArena(a).setOneForAll(true);
            Utils.sendMessage(p, "&f[&2HappyChest&f]&eDie Truhe gibt es nun nur noch 1 mal in Arena " + HappyChest.getInstance().getArena(a).getName() + ".");
        }
        
    }
    
    public static void listAreas(Player p) {
        int i = 1;
        for(Map.Entry<String, Arena> e: HappyChest.getInstance().getArenas().entrySet()) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&e " + i + ". " + e.getKey() + " - " + e.getValue().getName());
            i++;
        }
    }
    
    public static void infoArea(Player p, String a) {
        if(!HappyChest.getInstance().isArena(a)) {
            Utils.sendMessage(p, "&f[&2HappyChest&f]&cDie angegebene Arena wurde nicht gefunden.");
            return;
        }
        
        Arena area = HappyChest.getInstance().getArena(a);
        Utils.sendMessage(p, "&f~~~~~~ &2HappyChest &f~~~~~~");
        Utils.sendMessage(p, "&eName : " + area.getName());
        Utils.sendMessage(p, "&e1. Ecke : " + area.getPos1().getWorld().getName() + " " + area.getPos1().getBlockX() + " " + area.getPos1().getBlockY() + " " + area.getPos1().getBlockZ());
        Utils.sendMessage(p, "&e2. Ecke : " + area.getPos2().getWorld().getName() + " " + area.getPos2().getBlockX() + " " + area.getPos2().getBlockY() + " " + area.getPos2().getBlockZ());
        Utils.sendMessage(p, "&eTruhen : " + area.getChests().size());
        Utils.sendMessage(p, "&eItems : " + area.getItemCount());
        Utils.sendMessage(p, "&eOne 4 All : " + (area.getOneForAll()?"Ja":"Nein"));
        
    }
    
    public static void loadAreas() {
        if(!HappyChest.getInstance().getDataFolder().exists())
            HappyChest.getInstance().getDataFolder().mkdir();
        
        File files = new File(HappyChest.getInstance().getDataFolder().toURI());
        for(File f: files.listFiles()) {
            if(f.getName().startsWith("arena_") && f.getName().endsWith(".yml") && !f.isDirectory()) {
                FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
                if(conf.isString("name") && Utils.getLFST(conf.getString("pos1")) != null && Utils.getLFST(conf.getString("pos2")) != null && conf.isList("chests")) {
                    List<Location> chloc = new ArrayList<>();
                    for(String l: conf.getStringList("chests")) {
                        Location loc = Utils.getLFST(l);
                        if(loc != null)
                            chloc.add(loc);
                    }
                    Arena a = new Arena(conf.getString("name"),Utils.getLFST(conf.getString("pos1")), Utils.getLFST(conf.getString("pos2")), chloc);
                    if(conf.isList("items"))
                        a.setItemList(conf.getStringList("items"));
                    if(conf.isBoolean("4one"))
                        a.setOneForAll(conf.getBoolean("4one"));
                    
                    HappyChest.getInstance().addArena(a);
                }
            }
        }
    }
    
    public static void saveAreas() {
        for(Map.Entry<String, Arena> e: HappyChest.getInstance().getArenas().entrySet()) {
            File file = new File(HappyChest.getInstance().getDataFolder(), "arena_" + e.getKey() + ".yml");
            FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
            conf.set("name", e.getValue().getName());
            Location loc1 = e.getValue().getPos1();
            Location loc2 = e.getValue().getPos2();
            conf.set("pos1", loc1.getWorld().getName() + ":" + loc1.getBlockX() + ":" + loc1.getBlockY() + ":" + loc1.getBlockZ());
            conf.set("pos2", loc2.getWorld().getName() + ":" + loc2.getBlockX() + ":" + loc2.getBlockY() + ":" + loc2.getBlockZ());
            List<String> chest = new ArrayList<>();
            for(Location loc: e.getValue().getChests()) {
                chest.add(loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ());
            }
            conf.set("chests", chest);
            conf.set("items", e.getValue().getItemList());
            conf.set("4one", e.getValue().getOneForAll());
            try {
                conf.save(file);
            } catch(IOException ex) {
                HappyChest.getInstance().getLogger().log(Level.WARNING, "Konnte Arena " + e.getKey() + " nicht speichern,", ex);
            }
        }
    }
}
