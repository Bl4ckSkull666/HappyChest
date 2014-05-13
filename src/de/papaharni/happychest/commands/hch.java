/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.commands;

import de.papaharni.happychest.HappyChest;
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
            sender.sendMessage(HappyChest.getLang().getString("cmdOnlyPlayer"));
            return true;
        }
        
        if(!Utils.hasPerm(p, "use"))
            return true;
        
        if(args.length >= 1) {
            switch(args[0].toLowerCase()) {
                case "mark": //FERTIG
                    if(!Utils.hasPerm(p, "mark"))
                        return true;
                    //Aktiviere/Deakiviere das Markieren
                    if(_plugin.isAllowMarking(p.getName())) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdDisMark"));
                        _plugin.denyMarking(p.getName());
                    } else {
                        if(HappyChest.getInstance().getWG() != null) {
                            Utils.sendMessage(p, HappyChest.getLang().getString("cmdWEMark"));
                        } else {
                            Utils.sendMessage(p, HappyChest.getLang().getString("cmdGSMark"));
                        }
                        _plugin.allowMarking(p.getName());
                    }
                    break;
                case "create": //FERTIG
                    if(!Utils.hasPerm(p, "create"))
                        return true;
                    if(args.length < 2) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdUseCreate"));
                        return true;
                    }
                    //Erstelle eine Arena
                    ArenaWorks.create(p, args[1]);
                    break;
                case "delete": //FERTIG
                    if(!Utils.hasPerm(p, "delete"))
                        return true;
                    if(args.length < 2) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdUseDelete"));
                        return true;
                    }
                    
                    if(!HappyChest.getInstance().isArena(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("noArena").replace("%arena%", args[1]));
                        return true;
                    }
                    
                    if(HappyChest.getInstance().isArenaTask(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("runArena").replace("%arena%", args[1]));
                        return true;
                    }
                    
                    //Wurde noch kein Remove beauftragt?
                    if(!HappyChest.getInstance().isRemRequest(p.getName())) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdReqDelete").replace("%arena%", args[1]));
                        HappyChest.getInstance().addRemRequest(p.getName(), args[1]);
                        return true;
                    }
                    
                    //Ist die alte Arena die gleiche wie jetzt? 
                    if(!HappyChest.getInstance().getRemRequest(p.getName()).equalsIgnoreCase(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdOthDelete").replace("%arena%", args[1]));
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdReqDelete").replace("%arena%", args[1]));
                        HappyChest.getInstance().addRemRequest(p.getName(), args[1]);
                        return true;
                    }
                    
                    //Ist die Zeit noch nciht abgelaufen?
                    if(HappyChest.getInstance().getRemRequestTime(p.getName()) < (System.currentTimeMillis()-3000)) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdTimDelete"));
                        HappyChest.getInstance().delRemRequest(p.getName());
                        return true;
                    }
                    
                    //Lösche Arena
                    HappyChest.getInstance().delArena(args[1]);
                    HappyChest.getInstance().delRemRequest(p.getName());
                    Utils.sendMessage(p, HappyChest.getLang().getString("cmdSucDelete"));
                    break;
                case "list":
                    if(!Utils.hasPerm(p, "list"))
                        return true;
                    ArenaWorks.listAreas(p);
                    break;
                case "info":
                    if(!Utils.hasPerm(p, "info"))
                        return true;
                    if(args.length < 2) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdUseInfo"));
                        return true;
                    }
                    ArenaWorks.infoArea(p, args[1]);
                    break;
                case "start": //FERTIG
                    if(!Utils.hasPerm(p, "start"))
                        return true;
                    
                    if(args.length < 3) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdUseStart"));
                        return true;
                    }
                    
                    if(!HappyChest.getInstance().isArena(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("noArena").replace("%arena%", args[1]));
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdUseStart"));
                        return true;
                    }
                    
                    if(HappyChest.getInstance().isArenaTask(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("runArena").replace("%arena%", args[1]));
                        return true;
                    }
                    
                    if(!Utils.isNumeric(args[2])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdWroIntStart"));
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdUseStart"));
                        return true;
                    }
                    
                    if(Integer.parseInt(args[2]) < 1) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdIntToLowStart"));
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdUseStart"));
                        return true;
                    }
                    
                    int rounds = -1;
                    if(args.length >= 4) {
                        if(Utils.isNumeric(args[3])) {
                            rounds = Integer.parseInt(args[3]);
                            if(rounds < 1) {
                                Utils.sendMessage(p, HappyChest.getLang().getString("cmdRoNoNumStart"));
                                Utils.sendMessage(p, HappyChest.getLang().getString("cmdUseStart"));
                                return true;
                            }
                        }
                    }
                    //Arena , interval ( Abstand ), Starter Name , 0 (Aktuelle Runde , Rundenanzahl )
                    Task.startRound(args[1], Integer.parseInt(args[2]), p.getName(), 0, rounds);
                    Utils.sendMessage(p, HappyChest.getLang().getString("cmdAdminStart"));
                    for(Player pl: Bukkit.getOnlinePlayers()) {
                        if(pl != p) {
                            Utils.sendMessage(pl, HappyChest.getLang().getString("cmdPplStart").replace("%arena%", args[1]));
                        }
                    }
                    //Starte eine Runde in Arena
                    break;
                case "end":
                    if(!Utils.hasPerm(p, "end"))
                        return true;
                    //Beende Arena
                    if(args.length < 2) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdUseEnd"));
                        return true;
                    }
                    
                    if(!HappyChest.getInstance().isArena(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("noArena").replace("%arena%", args[1]));
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdUseEnd"));
                        return true;
                    }
                    
                    if(!HappyChest.getInstance().isArenaTask(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdNoRunArena").replace("%arena%", args[1]));
                        return true;
                    }
                    
                    Utils.worldBroadcast(HappyChest.getLang().getString("cmdAdminEnd").replace("%arena%", HappyChest.getInstance().getArena(args[1]).getName()), HappyChest.getInstance().getArena(args[1]).getPos1().getWorld().getName());
                    HappyChest.getInstance().clearOnEventEnd(args[1]);
                    break;
                case "additem": //FERTIG
                    if(!Utils.hasPerm(p, "additem"))
                        return true;
                    
                    if(args.length < 4) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdUseAddItem"));
                        return true;
                    }
                    if(!HappyChest.getInstance().isArena(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("noArena").replace("%arena%", args[1]));
                        return true;
                    }
                    
                    if(HappyChest.getInstance().isArenaTask(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("runArena").replace("%arena%", args[1]));
                        return true;
                    }
                    //Füge Random Item hinzu
                    ArenaWorks.addItemToArena(p, args[1], Utils.implodeArray(args, " ", 2));
                    break;
                case "listitems":
                    if(!Utils.hasPerm(p, "listitems"))
                        return true;
                    
                    if(args.length < 2) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdUseListItem"));
                        return true;
                    }
                    if(!HappyChest.getInstance().isArena(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("noArena").replace("%arena%", args[1]));
                        return true;
                    }
                    
                    ArenaWorks.listArenaItems(p, args[1]);
                    break;
                case "remitem": //FERTIG
                    if(!Utils.hasPerm(p, "remitem"))
                        return true;
                    //Lösche Item aus der Random Liste
                    if(args.length < 2) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdUseRemItem"));
                        return true;
                    }
                    
                    if(args.length < 3) {
                        ArenaWorks.listArenaItems(p, args[1]);
                        return true;
                    } else {
                        if(!HappyChest.getInstance().isArena(args[1])) {
                            Utils.sendMessage(p, HappyChest.getLang().getString("noArena").replace("%arena%", args[1]));
                            return true;
                        }

                        if(HappyChest.getInstance().isArenaTask(args[1])) {
                            Utils.sendMessage(p, HappyChest.getLang().getString("runArena").replace("%arena%", args[1]));
                            return true;
                        }
                        
                        if(!Utils.isNumeric(args[2])) {
                            Utils.sendMessage(p, HappyChest.getLang().getString("cmdNeNoItem"));
                            return true;
                        }
                        ArenaWorks.removeArenaItem(p, args[1], Integer.parseInt(args[2]));
                    }
                    break;
                case "reloadchest": //FERTIG
                    if(!Utils.hasPerm(p, "reloadchest"))
                        return true;
                    
                    if(args.length < 2) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdChReload"));
                        return true;
                    }
                    
                    //Prüfe ob Arena vorhanden ist
                    if(!HappyChest.getInstance().isArena(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("noArena").replace("%arena%", args[1]));
                        return true;
                    }
                    
                    if(HappyChest.getInstance().isArenaTask(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("runArena").replace("%arena%", args[1]));
                        return true;
                    }
                    
                    //Hole Arena und prüfe ist nicht null
                    HappyChest.getInstance().getArena(args[1]).reloadChests(p);
                    break;
                case "load": //FERTIG
                    if(!Utils.hasPerm(p, "load"))
                        return true;
                    
                    ArenaWorks.loadAreas();
                    Utils.sendMessage(p, HappyChest.getLang().getString("cmdLoadArena"));
                    break;
                case "save": //FERTIG
                    if(!Utils.hasPerm(p, "save"))
                        return true;
                    
                    ArenaWorks.saveAreas();
                    Utils.sendMessage(p, HappyChest.getLang().getString("cmdLoadArena"));
                    break;
                case "4one":
                    if(!Utils.hasPerm(p, "4one"))
                        return true;
                    
                    //Lösche Item aus der Random Liste
                    if(args.length < 2) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("cmdSet4One"));
                        return true;
                    }
                    
                    if(!HappyChest.getInstance().isArena(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("noArena").replace("%arena%", args[1]));
                        return true;
                    }
                    
                    if(HappyChest.getInstance().isArenaTask(args[1])) {
                        Utils.sendMessage(p, HappyChest.getLang().getString("runArena").replace("%arena%", args[1]));
                        return true;
                    }
                
                    ArenaWorks.set4One(p, args[1]);
                    break;
                default:
                    //Unbekannter Begriff an stelle 1
                    Utils.sendMessage(p, HappyChest.getLang().getString("cmdUse"));
                    break;
            }
            return true;
        }
        //Argument(e) vergessen
        Utils.sendMessage(p, HappyChest.getLang().getString("cmdUse"));
        return true;
    }
    
    /*
    private static void sendMail(MailServer server, Mail mail) throws EmailException {
        SimpleEmail email = new SimpleEmail();
        email.setHostName(server.getHost());
        email.setSmtpPort(server.getPort());
        email.setAuthentication(server.getUser(), server.getPassword());
        email.setDebug(false);
        email.setTLS(true);
        
        email.addTo(mail.getEmail());
        email.setFrom(server.getEmail(), mail.getName());
        email.setSubject(mail.getSubject());
        email.setMsg(mail.getMsg());
        email.send();
    }
    
    public static String sendeEmailMitAnhang(
         String mailserver, String username, String password, String absender, String empfaenger,
         String textCharset, String betreff, String text,
         String anhangContentType, InputStream anhangInputStream, String anhangDateiName, String anhangBeschreibung )
         throws IOException, EmailException
   {
      MultiPartEmail email = new MultiPartEmail();
      if( username != null && password != null ) {
         email.setAuthenticator( new DefaultAuthenticator( username, password ) );
         email.setSSLOnConnect( true );
      }
      email.setHostName( mailserver  );
      email.setFrom(     absender    );
      email.addTo(       empfaenger  );
      email.setCharset(  textCharset );
      email.setSubject(  betreff     );
      email.setMsg(      text        );
      return email.send();
   }
   */
}