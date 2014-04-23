/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest;

import java.util.HashMap;
import net.milkbowl.vault.economy.Economy;
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

    
    @Override
    public void onEnable() {
        
    }
    
    @Override
    public void onDisable() {
    
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
}
