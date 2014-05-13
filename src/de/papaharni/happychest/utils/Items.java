/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.utils;

import de.papaharni.happychest.HappyChest;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Pappi
 */
public final class Items {
    public static boolean isItem(String str) {
        return ((getItem(str) != null)?true:false);
    }
    
    public static ItemStack getItem(String str) {
        String[] args = str.split(" ");
        if(args.length < 2) {
            HappyChest.getInstance().getLogger().log(Level.INFO, str + " hat zu wenig Argumente Mindestens : Itemname Itemmenge - sind gefordert.");
            return null;
        }
        
        if(!Utils.isNumeric(args[1])) {
            HappyChest.getInstance().getLogger().log(Level.INFO, args[1] + " muss eine Zahl sein");
            return null;
        }
        String[] itemname = args[0].split("\\:");
        if(Material.matchMaterial(itemname[0]) == null) {
            HappyChest.getInstance().getLogger().log(Level.INFO, itemname[0] + " ist kein gultiges Item.");
            return null;
        }
        
        ItemStack i = new ItemStack(Material.matchMaterial(itemname[0]), Integer.parseInt(args[1]));
        if(i == null) {
            HappyChest.getInstance().getLogger().log(Level.INFO, "Konnte kein Item aus " + str + " erstellen.");
            return null;
        }
        
        if(args.length >= 3) {
            for(int a = 2; a < args.length; a++) {
                ItemMeta im = i.getItemMeta();
                String[] sargs = args[a].split(":");
                if(sargs.length == 2) {
                    switch(sargs[0]) {
                        case "lore":
                            String[] msg = sargs[1].split("\\|");
                            if(msg.length > 4)
                                break;
                            List<String> lore = new ArrayList<>();
                            for(int b = 0; b < msg.length; b++) {
                                lore.add(ChatColor.translateAlternateColorCodes('&', msg[b].replaceAll("_", " ")));
                            }
                            im.setLore(lore);
                            break;
                        case "name":
                            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', sargs[1]));
                            break;
                        case "heal":
                            if(!Utils.isNumeric(sargs[1]))
                                continue;
                            int heal = Integer.parseInt(sargs[1]);
                            if(heal > 20)
                                heal = 20;
                            if(heal < 1)
                                heal = 1;
                            i.setDurability((short)heal);
                        default:
                            if(Enchantment.getByName(sargs[0].toLowerCase()) != null && Utils.isNumeric(sargs[1])) {
                                im.addEnchant(Enchantment.getByName(sargs[0].toLowerCase()), Integer.parseInt(sargs[1]), false);
                            } else if(HappyChest.getInstance().getEnchants().containsKey(sargs[0].toLowerCase()) && Utils.isNumeric(sargs[1])) {
                                im.addEnchant(HappyChest.getInstance().getEnchants().get(sargs[0].toLowerCase()), Integer.parseInt(sargs[1]), false);
                            } else {
                                HappyChest.getInstance().getLogger().log(Level.INFO, "Ignoriere " + sargs[0] + " da es nirgendwo rein passt.");
                            }
                            break;
                    }
                }
                i.setItemMeta(im);
            }
        }
        return i;
    }
}
