/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.commands;

import de.papaharni.happychest.HappyChest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Pappi
 */
public class main implements CommandExecutor {
    private HappyChest _plugin;
    
    public void main(HappyChest plugin) {
        _plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(p == null) {
            sender.sendMessage("Dieser Befehl kann nur von Spielern ausgeführt werden.");
            return true;
        }
        
        if(args.length >= 1) {
            switch(args[0].toLowerCase()) {
                case "create":
                    //Erstelle eine Arena
                    break;
                case "delete":
                    //Lösche Arena
                    break;
                case "start":
                    //Starte eine Runde in Arena
                    break;
                case "end":
                    //Beende Arena
                    break;
                case "add":
                    //Füge Random Item hinzu
                    break;
                case "reloadchest":
                    //Prüfe Arena erneut auf Kisten
                    break;
                case "remove":
                    //Lösche Item aus der Random Liste
                    break;
                default:
                    //Unbekannter Begriff an stelle 1
            }
        }
        //Argument(e) vergessen
        
        
        return true;
    }
    
}
