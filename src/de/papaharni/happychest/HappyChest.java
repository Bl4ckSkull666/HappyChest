/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.papaharni.happychest.commands.hch;
import de.papaharni.happychest.utils.Arena;
import de.papaharni.happychest.utils.ArenaWorks;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Pappi
 */
public class HappyChest extends JavaPlugin {
    
    private static HappyChest _instance;
    private static boolean _debugMode;
    
    //Arena Tasks / Arena Name = Task
    private final Map<String, BukkitTask> _games = new HashMap<>();
    //Arena / Aktuelle suchende Truhe
    private final Map<String, Location> _curchests = new HashMap<>();
    //Arena / Liste der bereits gefundenen Spielernamen
    private final Map<String, List<String>> _usedPlayers = new HashMap<>();
    //Arena / Liste der zu erhaltenden Items
    private final Map<String, List<String>> _roundRewards = new HashMap<>();
    //
    private final List<String> _eventChest = new ArrayList<>();
    private final Map<String, HashMap<String, ItemStack[]>> _reste = new HashMap<>();
    
    //Runden Tasks Speicherung
    private final Map<String, BukkitTask> _areaTask = new HashMap<>();
    
    //Zur Markierung falls kein WE vorhanden ist.
    private final List<String> _allowMark = new ArrayList<>();
    private final Map<String, Location> _playerMarksLeft = new HashMap<>();
    private final Map<String, Location> _playerMarksRight = new HashMap<>();
    
    //Arena Saver
    private final Map<String, Arena> _areas = new HashMap<>();
    
    //Löschungs Maps - Spielername / Arena - Spielername / Zeit
    private final Map<String, String> _remrequest = new HashMap<>();
    private final Map<String, Long> _remrequesttime = new HashMap<>();

    //Sonstiges - Externe Plugins
    private WorldGuardPlugin _wg;
    private static Economy economy = null;
    
    @Override
    public void onEnable() {
        _wg = getWorldGuard();
        ArenaWorks.loadAreas(this);
        this.getCommand("hch").setExecutor(new hch(this));
        _instance = this;
    }
    
    @Override
    public void onDisable() {
        ArenaWorks.saveAreas(this);
    }
    
    public static HappyChest getInstance() {
        return _instance;
    }
    
    public Economy getEconomy() {
        return economy;
    }
    
    private boolean setupEconomy() {
        RegisteredServiceProvider economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if(economyProvider != null)
            economy = (Economy)economyProvider.getProvider();
        return economy != null;
    }
    
    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }
        return (WorldGuardPlugin) plugin;
    }
    
    public WorldGuardPlugin getWG() {
        return _wg;
    }
    
    public boolean isAllowMarking(String p) {
        return _allowMark.contains(p)?true:false;
    }
    
    public void allowMarking(String p) {
        _allowMark.add(p);
    }
    
    public void denyMarking(String p) {
        _allowMark.remove(p);
    }
    
    public void setMarking(String p, String size, Location loc) {
        switch(size) {
            case "left":
                _playerMarksLeft.put(p, loc);
                break;
            case "right":
                _playerMarksRight.put(p, loc);
                break;
            default:
                break;
        }
    }
    
    public void delMarking(String p, String size) {
        switch(size) {
            case "left":
                _playerMarksLeft.remove(p);
                break;
            case "right":
                _playerMarksRight.remove(p);
                break;
            default:
                _playerMarksLeft.remove(p);
                _playerMarksRight.remove(p);
                break;
        }
    }
    
    public boolean isMarking(String p, String size) {
        switch(size) {
            case "left":
                return _playerMarksLeft.containsKey(p);
            case "right":
                return _playerMarksRight.containsKey(p);
            default:
                return false;
        }
    }
    
    public Location getMarking(String p, String size) {
        switch(size) {
            case "left":
                return _playerMarksLeft.get(p);
            case "right":
                return _playerMarksRight.get(p);
            default:
                return null;
        }
    }
    
    public void addArena(Arena a) {
        _areas.put(a.getName().toLowerCase(), a);
    }
    
    public boolean isArena(String a) {
        return _areas.containsKey(a.toLowerCase());
    }
    
    public Arena getArena(String a) {
        return isArena(a)?_areas.get(a.toLowerCase()):null;
    }
    
    public Map<String, Arena> getArenas() {
        return _areas;
    }
    
    public void delArena(String a) {
        _areas.remove(a.toLowerCase());
    }
    
    public void addRemRequest(String p, String a) {
        _remrequest.put(p.toLowerCase(), a.toLowerCase());
        _remrequesttime.put(p.toLowerCase(), System.currentTimeMillis());
    }
    
    public String getRemRequest(String p) {
        return (isRemRequest(p.toLowerCase()))?_remrequest.get(p.toLowerCase()):"";
    }
    
    public boolean isRemRequest(String p) {
        return _remrequest.containsKey(p.toLowerCase());
    }
    
    public void delRemRequest(String p) {
        _remrequest.remove(p.toLowerCase());
        _remrequesttime.remove(p.toLowerCase());
    }
    
    public Long getRemRequestTime(String p) {
        return (_remrequesttime.containsKey(p.toLowerCase()))?_remrequesttime.get(p.toLowerCase()):0;
    }
    
    public Map<String, Location> getCurChests() {
        return _curchests;
    }
    
    public List<String> getUsedPlayersList(String a) {
        if(!_usedPlayers.containsKey(a.toLowerCase()))
            addUsedPlayersList(a.toLowerCase());
        return _usedPlayers.get(a.toLowerCase());
    }
    
    public void addUsedPlayersList(String a) {
        List<String> list = new ArrayList<>();
        _usedPlayers.put(a.toLowerCase(), list);
    }
    
    public void delUsedPlayersList(String a) {
        _usedPlayers.remove(a.toLowerCase());
    }
    
    public List<String> getRoundRewards(String a) {
        if(!_usedPlayers.containsKey(a.toLowerCase())) {
            List<String> list = new ArrayList<>();
            addRoundRewards(a.toLowerCase(), list);
        }
        return _usedPlayers.get(a.toLowerCase());
    }
    
    public void addRoundRewards(String a, List<String> list) {
        _usedPlayers.put(a.toLowerCase(), list);
    }
    
    public void delRoundRewards(String a) {
        _usedPlayers.remove(a.toLowerCase());
    }
    
    public boolean hasReste(String a, String p) {
        if(!_reste.containsKey(a))
            return false;
        if(!_reste.get(a).containsKey(p))
            return false;
        return true;
    }
    
    public ItemStack[] getReste(String a, String p) {
        ItemStack[] item = new ItemStack[1];
        if(!hasReste(a, p))
            return item;
        return _reste.get(a).get(p);
    }
    
    public void setReste(String a, String p, ItemStack[] items) {
        if(!_reste.containsKey(a)) {
            HashMap<String, ItemStack[]> rest = new HashMap<>();
            _reste.put(a, rest);
        }
        _reste.get(a).put(p, items);
    }
    
    public void cancelArenaTask(String a) {
        if(_areaTask.containsKey(a)) {
            if(_areaTask.get(a) != null) {
                _areaTask.get(a).cancel();
            }
            _areaTask.remove(a);
        }
    }
    
    public void setArenaTask(String a, BukkitTask t) {
        cancelArenaTask(a);
        _areaTask.put(a, t);
    }
}
