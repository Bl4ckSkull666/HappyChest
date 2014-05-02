/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.commands;

import de.papaharni.happychest.HappyChest;
import de.papaharni.happychest.utils.Arena;
import de.papaharni.happychest.utils.ArenaWorks;
import de.papaharni.happychest.utils.Task;
import de.papaharni.happychest.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Pappi
 */
public class hch implements CommandExecutor {
    private HappyChest _plugin;
    
    public hch(HappyChest plugin) {
        _plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(p == null) {
            sender.sendMessage("Dieser Befehl kann nur von Spielern ausgeführt werden.");
            return true;
        }
        
        if(!p.hasPermission("happychest.use") && !p.isOp()) {
            sendMessage(p, "&f[&2HappyChest&f]&cDu hast nicht die erforderlichen Rechte um diesen Befehl zu verwenden.");
            return true;
        }
        
        if(args.length >= 1) {
            switch(args[0].toLowerCase()) {
                case "mark":
                    //Aktiviere/Deakiviere das Markieren
                    if(_plugin.isAllowMarking(p.getName())) {
                        sendMessage(p, "&f[&2HappyChest&f]&eDas Makieren wurde deaktiviert.");
                        _plugin.denyMarking(p.getName());
                    } else {
                        if(HappyChest.getInstance().getWG() != null) {
                            sendMessage(p, "&f[&2HappyChest&f]&eVerwende bitte nun WorldEdit zum makieren.");
                        } else {
                            sendMessage(p, "&f[&2HappyChest&f]&eVerwende bitte ein Gold Schwert zum makieren.");
                        }
                        _plugin.allowMarking(p.getName());
                    }
                    break;
                case "create":
                    if(args.length < 2) {
                        sendMessage(p, "&f[&2HappyChest&f]&cBitte verwenden /hch create (ArenaName)");
                        return true;
                    }
                    //Erstelle eine Arena
                    ArenaWorks.create(p, args[1]);
                    break;
                case "delete":
                    if(args.length < 2) {
                        sendMessage(p, "&f[&2HappyChest&f]&cBitte verwenden /hch delete (ArenaName)");
                        return true;
                    }
                    
                    //Wurde noch kein Remove beauftragt?
                    if(!HappyChest.getInstance().isRemRequest(p.getName())) {
                        sendMessage(p, "&f[&2HappyChest&f]&cBitte bestätige das Löschen der Arena " + args[1] + " mit der erneuten Eingabe des Befehls innerhalb von 30 Sekunden.");
                        HappyChest.getInstance().addRemRequest(p.getName(), args[1]);
                        return true;
                    }
                    
                    //Ist die alte Arena die gleiche wie jetzt? 
                    if(!HappyChest.getInstance().getRemRequest(p.getName()).equalsIgnoreCase(args[0])) {
                        sendMessage(p, "&f[&2HappyChest&f]&cDu hast einen neuen Löschantrag für die Arena " + args[1] + " gestellt.");
                        sendMessage(p, "&f[&2HappyChest&f]&cBitte bestätige das Löschen der Arena " + args[1] + " mit der erneuten Eingabe des Befehls innerhalb von 30 Sekunden.");
                        HappyChest.getInstance().addRemRequest(p.getName(), args[1]);
                        return true;
                    }
                    
                    //Ist die Zeit noch nciht abgelaufen?
                    if(HappyChest.getInstance().getRemRequestTime(p.getName()) < (System.currentTimeMillis()-3000)) {
                        sendMessage(p, "&f[&2HappyChest&f]&cDu hast zu lange für deine Löschbestätigung gebraucht.");
                        HappyChest.getInstance().addRemRequest(p.getName(), args[1]);
                        return true;
                    }
                    
                    //Lösche Arena
                    HappyChest.getInstance().delArena(args[1]);
                    HappyChest.getInstance().delRemRequest(p.getName());
                    break;
                case "start":
                    if(args.length < 3) {
                        sendMessage(p, "&f[&2HappyChest&f]&cBitte verwende /hch start (ArenaName) (Interval in Minuten) (Optional Max. Runden)");
                        return true;
                    }
                    
                    if(!HappyChest.getInstance().isArena(args[1])) {
                        sendMessage(p, "&f[&2HappyChest&f]&cArena Name konnte nicht gefunden werden.");
                        sendMessage(p, "&f[&2HappyChest&f]&cBitte verwenden /hch start (ArenaName) (Interval in Minuten) (Optional Max. Runden)");
                        return true;
                    }
                    
                    if(!Utils.isNumeric(args[2])) {
                        sendMessage(p, "&f[&2HappyChest&f]&cDer Interval muss eine Nummer sein.");
                        sendMessage(p, "&f[&2HappyChest&f]&cBitte verwenden /hch start (ArenaName) (Interval in Minuten) (Optional Max. Runden)");
                        return true;
                    }
                    
                    int rounds = -1;
                    if(args.length >= 3) {
                        if(Utils.isNumeric(args[3])) {
                            rounds = Integer.parseInt(args[3]);
                            if(rounds < 1) {
                                sendMessage(p, "&f[&2HappyChest&f]&cDie Runden Anzahl muss eine Zahl sein.");
                                sendMessage(p, "&f[&2HappyChest&f]&cBitte verwenden /hch start (ArenaName) (Interval in Minuten) (Optional Max. Runden)");
                                return true;
                            }
                        }
                    }
                    //Arena , interval ( Abstand ), Starter Name , 0 (Aktuelle Runde , Rundenanzahl )
                    Task.startRound(args[1], Integer.parseInt(args[2]), p.getName(), 0, rounds);
                    sendMessage(p, "&f[&2HappyChest&f]&eDie Erste Runde geht in 2 Minuten los.");
                    for(Player pl: Bukkit.getOnlinePlayers()) {
                        if(pl != p) {
                            pl.sendMessage("&f[&2HappyChest&f]&eIn 2 Minuten startet in Arena " + args[1] + " eine Schatzjagt.");
                        }
                    }
                    //Starte eine Runde in Arena
                    break;
                case "end":
                    //Beende Arena
                    break;
                case "additem":
                    if(args.length < 2) {
                        sendMessage(p, "&f[&2HappyChest&f]&cBitte verwenden /hch reloadchest (ArenaName)");
                        return true;
                    }
                    //Füge Random Item hinzu
                    break;
                case "reloadchest":
                    if(args.length < 2) {
                        sendMessage(p, "&f[&2HappyChest&f]&cBitte verwenden /hch reloadchest (ArenaName)");
                        return true;
                    }
                    
                    //Prüfe ob Arena vorhanden ist
                    if(!HappyChest.getInstance().isArena(args[1])) {
                        sendMessage(p, "&f[&2HappyChest&f]&cDie gewünschte Arena existiert nicht.");
                        return true;
                    }
                    
                    //Hole Arena und prüfe ist nicht null
                    Arena a = HappyChest.getInstance().getArena(args[1]);
                    if(a == null) {
                        sendMessage(p, "&f[&2HappyChest&f]&cKonnte die Arena nicht finden.");
                        return true;
                    }
                    
                    //Prüfe Arena erneut auf Truhen
                    a.reloadChests(p);
                    break;
                case "removeitem":
                    if(!p.hasPermission("happychest.load") && !p.hasPermission("happychest.*") && !p.isOp()) {
                        sendMessage(p, "&f[&2HappyChest&f]&cDu hast keine Rechte um die Areas neuzuladen.");
                        return true;
                    }
                    //Lösche Item aus der Random Liste
                    if(args.length < 2) {
                        sendMessage(p, "&f[&2HappyChest&f]&cBitte verwenden /hch removeitem (ArenaName) (Optional Item-Nr.)");
                        return true;
                    }
                    
                    if(args.length < 3) {
                        ArenaWorks.listArenaItems(p, args[1]);
                        return true;
                    } else {
                        if(!Utils.isNumeric(args[2])) {
                            sendMessage(p, "&f[&2HappyChest&f]&cAn Stelle 3 muss eine Zahl stehen.");
                            return true;
                        }
                        ArenaWorks.removeArenaItem(p, args[1], Integer.parseInt(args[2]));
                    }
                    break;
                case "load":
                    if(!p.hasPermission("happychest.load") && !p.hasPermission("happychest.*") && !p.isOp()) {
                        sendMessage(p, "&f[&2HappyChest&f]&cDu hast keine Rechte um die Areas neuzuladen.");
                        return true;
                    }
                    ArenaWorks.loadAreas(_plugin);
                    sendMessage(p, "&f[&2HappyChest&f]&eAlle HappyChest Areas wurden neugeladen.");
                    break;
                case "save":
                    if(!p.hasPermission("happychest.save") && !p.hasPermission("happychest.save") && !p.isOp()) {
                        sendMessage(p, "&f[&2HappyChest&f]&cDu hast keine Rechte um die Areas zu speichern.");
                        return true;
                    }
                    ArenaWorks.saveAreas(_plugin);
                    sendMessage(p, "&f[&2HappyChest&f]&eAlle HappyChest Areas wurden neugeladen.");
                default:
                    //Unbekannter Begriff an stelle 1
                    sendMessage(p, "&f[&2HappyChest&f]&cBitte verwenden /hch (mark/create/delete/start/end/add/reloadchest/remove/load/save)");
                    break;
            }
            return true;
        }
        //Argument(e) vergessen
        sendMessage(p, "&f[&2HappyChest&f]&cBitte verwenden /hch (mark/create/delete/start/end/add/reloadchest/remove)");
        return true;
    }
    
    private boolean hasPerm(Player p, String perm) {
        if(!p.hasPermission("happychest." + perm) || !p.hasPermission("happychest.*") || !p.isOp())
            return false;
        return true;
    }
    
    private void sendMessage(Player p, String msg) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
    
}