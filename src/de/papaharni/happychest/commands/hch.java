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
            Utils.sendMessage(p, "&cDu hast nicht die erforderlichen Rechte um diesen Befehl zu verwenden.");
            return true;
        }
        
        if(args.length >= 1) {
            switch(args[0].toLowerCase()) {
                case "mark": //FERTIG
                    //Aktiviere/Deakiviere das Markieren
                    if(_plugin.isAllowMarking(p.getName())) {
                        Utils.sendMessage(p, "&eDas Makieren wurde deaktiviert.");
                        _plugin.denyMarking(p.getName());
                    } else {
                        if(HappyChest.getInstance().getWG() != null) {
                            Utils.sendMessage(p, "&eVerwende bitte nun WorldEdit zum makieren.");
                        } else {
                            Utils.sendMessage(p, "&eVerwende bitte ein Gold Schwert zum makieren.");
                        }
                        _plugin.allowMarking(p.getName());
                    }
                    break;
                case "create": //FERTIG
                    if(args.length < 2) {
                        Utils.sendMessage(p, "&cBitte verwenden /hch create (ArenaName)");
                        return true;
                    }
                    //Erstelle eine Arena
                    ArenaWorks.create(p, args[1]);
                    break;
                case "delete": //FERTIG
                    if(args.length < 2) {
                        Utils.sendMessage(p, "&cBitte verwenden /hch delete (ArenaName)");
                        return true;
                    }
                    
                    //Wurde noch kein Remove beauftragt?
                    if(!HappyChest.getInstance().isRemRequest(p.getName())) {
                        Utils.sendMessage(p, "&cBitte bestätige das Löschen der Arena " + args[1] + " mit der erneuten Eingabe des Befehls innerhalb von 30 Sekunden.");
                        HappyChest.getInstance().addRemRequest(p.getName(), args[1]);
                        return true;
                    }
                    
                    //Ist die alte Arena die gleiche wie jetzt? 
                    if(!HappyChest.getInstance().getRemRequest(p.getName()).equalsIgnoreCase(args[1])) {
                        Utils.sendMessage(p, "&cDu hast einen neuen Löschantrag für die Arena " + args[1] + " gestellt.");
                        Utils.sendMessage(p, "&cBitte bestätige das Löschen der Arena " + args[1] + " mit der erneuten Eingabe des Befehls innerhalb von 30 Sekunden.");
                        HappyChest.getInstance().addRemRequest(p.getName(), args[1]);
                        return true;
                    }
                    
                    //Ist die Zeit noch nciht abgelaufen?
                    if(HappyChest.getInstance().getRemRequestTime(p.getName()) < (System.currentTimeMillis()-3000)) {
                        Utils.sendMessage(p, "&cDu hast zu lange für deine Löschbestätigung gebraucht.");
                        HappyChest.getInstance().delRemRequest(p.getName());
                        return true;
                    }
                    
                    //Lösche Arena
                    HappyChest.getInstance().delArena(args[1]);
                    HappyChest.getInstance().delRemRequest(p.getName());
                    Utils.sendMessage(p, "&cArena wurde erfolgreich gelöscht.");
                    break;
                case "list":
                    ArenaWorks.listAreas(p);
                    break;
                case "info":
                    if(args.length < 2) {
                        Utils.sendMessage(p, "&cBitte verwende /hch info (ArenaName)");
                        return true;
                    }
                    ArenaWorks.infoArea(p, args[1]);
                    break;
                case "start": //FERTIG
                    if(args.length < 3) {
                        Utils.sendMessage(p, "&cBitte verwende /hch start (ArenaName) (Interval in Minuten) (Optional Max. Runden)");
                        return true;
                    }
                    
                    if(!HappyChest.getInstance().isArena(args[1])) {
                        Utils.sendMessage(p, "&cArena Name konnte nicht gefunden werden.");
                        Utils.sendMessage(p, "&cBitte verwenden /hch start (ArenaName) (Interval in Minuten) (Optional Max. Runden)");
                        return true;
                    }
                    
                    if(!Utils.isNumeric(args[2])) {
                        Utils.sendMessage(p, "&cDer Interval muss eine Nummer sein.");
                        Utils.sendMessage(p, "&cBitte verwenden /hch start (ArenaName) (Interval in Minuten) (Optional Max. Runden)");
                        return true;
                    }
                    
                    int rounds = -1;
                    if(args.length >= 4) {
                        if(Utils.isNumeric(args[3])) {
                            rounds = Integer.parseInt(args[3]);
                            if(rounds < 1) {
                                Utils.sendMessage(p, "&cDie Runden Anzahl muss eine Zahl sein.");
                                Utils.sendMessage(p, "&cBitte verwenden /hch start (ArenaName) (Interval in Minuten) (Optional Max. Runden)");
                                return true;
                            }
                        }
                    }
                    //Arena , interval ( Abstand ), Starter Name , 0 (Aktuelle Runde , Rundenanzahl )
                    Task.startRound(args[1], Integer.parseInt(args[2]), p.getName(), 0, rounds);
                    Utils.sendMessage(p, "&eDie Erste Runde geht in 2 Minuten los.");
                    for(Player pl: Bukkit.getOnlinePlayers()) {
                        if(pl != p) {
                            Utils.sendMessage(pl, "&eIn 2 Minuten startet in Arena " + args[1] + " eine Schatzjagt.");
                        }
                    }
                    //Starte eine Runde in Arena
                    break;
                case "end":
                    //Beende Arena
                    if(args.length < 2) {
                        Utils.sendMessage(p, "&cBitte verwende /hch end (ArenaName)");
                        return true;
                    }
                    
                    if(!HappyChest.getInstance().isArena(args[1])) {
                        Utils.sendMessage(p, "&cArena Name konnte nicht gefunden werden.");
                        Utils.sendMessage(p, "&cBitte verwenden /hch end (ArenaName)");
                        return true;
                    }
                    
                    if(!HappyChest.getInstance().isArenaTask(args[1])) {
                        Utils.sendMessage(p, "&cDiese Arena läuft gerade nicht.");
                        return true;
                    }
                    
                    Utils.worldBroadcast("Das Event in Arena " + HappyChest.getInstance().getArena(args[1]).getName() + " wurde beendet.", HappyChest.getInstance().getArena(args[1]).getPos1().getWorld().getName());
                    HappyChest.getInstance().clearOnEventEnd(args[1]);
                    break;
                case "additem": //FERTIG
                    if(args.length < 4) {
                        Utils.sendMessage(p, "&cBitte verwenden /hch additem (ArenaName) (Itemname) (Menge)");
                        return true;
                    }
                    //Füge Random Item hinzu
                    ArenaWorks.addItemToArena(p, args[1], Utils.implodeArray(args, " ", 2));
                    break;
                case "listitems":
                    if(args.length < 2) {
                        Utils.sendMessage(p, "&cBitte verwenden /hch listitems (ArenaName)");
                        return true;
                    }
                    ArenaWorks.listArenaItems(p, args[1]);
                    break;
                case "removeitem": //FERTIG
                    if(!p.hasPermission("happychest.removeitem") && !p.hasPermission("happychest.*") && !p.isOp()) {
                        Utils.sendMessage(p, "&cDu hast keine Rechte um die Areas neuzuladen.");
                        return true;
                    }
                    //Lösche Item aus der Random Liste
                    if(args.length < 2) {
                        Utils.sendMessage(p, "&cBitte verwenden /hch removeitem (ArenaName) (Optional Item-Nr.)");
                        return true;
                    }
                    
                    if(args.length < 3) {
                        ArenaWorks.listArenaItems(p, args[1]);
                        return true;
                    } else {
                        if(!Utils.isNumeric(args[2])) {
                            Utils.sendMessage(p, "&cAn Stelle 3 muss eine Zahl stehen.");
                            return true;
                        }
                        ArenaWorks.removeArenaItem(p, args[1], Integer.parseInt(args[2]));
                    }
                    break;
                case "reloadchest": //FERTIG
                    if(args.length < 2) {
                        Utils.sendMessage(p, "&cBitte verwenden /hch reloadchest (ArenaName)");
                        return true;
                    }
                    
                    //Prüfe ob Arena vorhanden ist
                    if(!HappyChest.getInstance().isArena(args[1])) {
                        Utils.sendMessage(p, "&cDie gewünschte Arena existiert nicht.");
                        return true;
                    }
                    
                    //Hole Arena und prüfe ist nicht null
                    Arena a = HappyChest.getInstance().getArena(args[1]);
                    if(a == null) {
                        Utils.sendMessage(p, "&cKonnte die Arena nicht finden.");
                        return true;
                    }
                    
                    //Prüfe Arena erneut auf Truhen
                    a.reloadChests(p);
                    break;
                case "load": //FERTIG
                    if(!p.hasPermission("happychest.load") && !p.hasPermission("happychest.*") && !p.isOp()) {
                        Utils.sendMessage(p, "&cDu hast keine Rechte um die Areas neuzuladen.");
                        return true;
                    }
                    ArenaWorks.loadAreas();
                    Utils.sendMessage(p, "&eAlle HappyChest Areas wurden neugeladen.");
                    break;
                case "save": //FERTIG
                    if(!p.hasPermission("happychest.save") && !p.hasPermission("happychest.save") && !p.isOp()) {
                        Utils.sendMessage(p, "&cDu hast keine Rechte um die Areas zu speichern.");
                        return true;
                    }
                    ArenaWorks.saveAreas();
                    Utils.sendMessage(p, "&eAlle HappyChest Areas wurden gespeichert.");
                    break;
                case "4one":
                    if(!p.hasPermission("happychest.removeitem") && !p.hasPermission("happychest.*") && !p.isOp()) {
                        Utils.sendMessage(p, "&cDu hast keine Rechte um die Areas neuzuladen.");
                        return true;
                    }
                    //Lösche Item aus der Random Liste
                    if(args.length < 2) {
                        Utils.sendMessage(p, "&cBitte verwenden /hch 4one");
                        return true;
                    }
                
                    ArenaWorks.set4One(p, args[1]);
                    break;
                default:
                    //Unbekannter Begriff an stelle 1
                    Utils.sendMessage(p, "&cBitte verwenden /hch (mark/create/delete/start/end/add/reloadchest/remove/load/save)");
                    break;
            }
            return true;
        }
        //Argument(e) vergessen
        Utils.sendMessage(p, "&cBitte verwenden /hch (mark/create/delete/start/end/add/reloadchest/remove)");
        return true;
    }
    
    private boolean hasPerm(Player p, String perm) {
        if(!p.hasPermission("happychest." + perm) || !p.hasPermission("happychest.*") || !p.isOp())
            return false;
        return true;
    }
}