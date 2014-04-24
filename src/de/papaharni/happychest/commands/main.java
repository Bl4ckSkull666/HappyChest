/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.commands;

import de.papaharni.happychest.HappyChest;
import de.papaharni.happychest.utils.Arena;
import de.papaharni.happychest.utils.ArenaWorks;
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
                    ArenaWorks.create(p, args[1]);
                    break;
                case "delete":
                    if(args.length < 2) {
                        p.sendMessage("$f[$2HappyChest$f]$cBitte verwenden /hch delete (ArenaName)");
                        return true;
                    }
                    
                    //Wurde noch kein Remove beauftragt?
                    if(!HappyChest.getInstance().isRemRequest(p.getName())) {
                        p.sendMessage("$f[$2HappyChest$f]$cBitte bestätige das Löschen der Arena " + args[0] + " mit der erneuten Eingabe des Befehls innerhalb von 30 Sekunden.");
                        HappyChest.getInstance().addRemRequest(p.getName(), args[1]);
                        return true;
                    }
                    
                    //Ist die alte Arena die gleiche wie jetzt? 
                    if(!HappyChest.getInstance().getRemRequest(p.getName()).equalsIgnoreCase(args[0])) {
                        p.sendMessage("$f[$2HappyChest$f]$cDu hast einen neuen Löschantrag für die Arena " + args[0] + " gestellt.");
                        p.sendMessage("$f[$2HappyChest$f]$cBitte bestätige das Löschen der Arena " + args[0] + " mit der erneuten Eingabe des Befehls innerhalb von 30 Sekunden.");
                        HappyChest.getInstance().addRemRequest(p.getName(), args[1]);
                        return true;
                    }
                    
                    //Ist die Zeit noch nciht abgelaufen?
                    if(HappyChest.getInstance().getRemRequestTime(p.getName()) < (System.currentTimeMillis()-3000)) {
                        p.sendMessage("$f[$2HappyChest$f]$cDu hast zu lange für deine Löschbestätigung gebraucht.");
                        HappyChest.getInstance().addRemRequest(p.getName(), args[1]);
                        return true;
                    }
                    
                    //Lösche Arena
                    HappyChest.getInstance().delArena(args[1]);
                    HappyChest.getInstance().delRemRequest(p.getName());
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
                    if(args.length < 2) {
                        p.sendMessage("$f[$2HappyChest$f]$cBitte verwenden /hch reloadchest (ArenaName)");
                        return true;
                    }
                    
                    //Prüfe ob Arena vorhanden ist
                    if(!HappyChest.getInstance().isArena(args[1])) {
                        p.sendMessage("$f[$2HappyChest$f]$cDie gewünschte Arena existiert nicht.");
                        return true;
                    }
                    
                    //Hole Arena und prüfe ist nicht null
                    Arena a = HappyChest.getInstance().getArena(args[1]);
                    if(a == null) {
                        p.sendMessage("$f[$2HappyChest$f]$cKonnte die Arena nicht finden.");
                        return true;
                    }
                    
                    //Prüfe Arena erneut auf Truhen
                    a.reloadChests(p);
                    break;
                case "removeitem":
                    //Lösche Item aus der Random Liste
                    if(args.length < 2) {
                        p.sendMessage("$f[$2HappyChest$f]$cBitte verwenden /hch removeitem (ArenaName) (Optional Item-Nr.)");
                        return true;
                    }
                    
                    
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