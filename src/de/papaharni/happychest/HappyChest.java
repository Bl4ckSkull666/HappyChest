/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
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
    private static Economy economy = null;
    
    private final HashMap<String, BukkitTask> _games = new HashMap<>();
    private final List<String> _allowMark = new ArrayList<>();
    private final HashMap<String, Location> _playerMarksLeft = new HashMap<>();
    private final HashMap<String, Location> _playerMarksRight = new HashMap<>();
    private WorldGuardPlugin _wg;
    
    @Override
    public void onEnable() {
        _wg = getWorldGuard();
    }
    
    @Override
    public void onDisable() {
    
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
    
    
}
