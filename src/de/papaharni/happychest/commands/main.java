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
                case "mark":
                    //Aktiviere/Deakiviere das Markieren
                    if(_plugin.isAllowMarking(p.getName())) {
                        p.sendMessage("$f[$2HappyChest$f]$eDas Makieren wurde deaktiviert.");
                        _plugin.denyMarking(p.getName());
                    } else {
                        p.sendMessage("$f[$2HappyChest$f]$eVerwende ein Gold Schwert zum makieren.");
                        _plugin.allowMarking(p.getName());
                    }
                    break;
                case "create":
                    if(args.length < 2) {
                        p.sendMessage("$f[$2HappyChest$f]$cBitte verwenden /hch create (ArenaName)");
                        return true;
                    }
                    //Erstelle eine Arena
                    Create.create(args[1], p);
                    break;
                case "delete":
                    if(args.length < 2) {
                        p.sendMessage("$f[$2HappyChest$f]$cBitte verwenden /hch delete (ArenaName)");
                        return true;
                    }
                    
                    if(!HappyChest.getInstance().isRemRequest(p.getName())) {
                        p.sendMessage("$f[$2HappyChest$f]$cBitte bestätige das Löschen mit der erneuten Eingabe des Befehls.");
                        HappyChest.getInstance().addRemRequest(p.getName(), args[1]);
                        return true;
                    }
                    
                    if(!HappyChest.getInstance().isRemRequest(label))
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
                    p.sendMessage("$f[$2HappyChest$f]$cBitte verwenden /hch (mark/create/delete/start/end/add/reloadchest/remove)");
                    break;
            }
            return true;
        }
        //Argument(e) vergessen
        p.sendMessage("$f[$2HappyChest$f]$cBitte verwenden /hch (mark/create/delete/start/end/add/reloadchest/remove)");
        return true;
    }
    
}
