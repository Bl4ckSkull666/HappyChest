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
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Pappi
 */
public final class Items {
    
    public boolean isItem(String str) {
        String[] args = str.split(" ");
        if(args.length < 2) {
            HappyChest.getInstance().getLogger().log(Level.INFO, str + " hat zu wenig Argumente Mindestens : Itemname Itemmenge - sind gefordert.");
            return false;
        }
        
        if(!isNumeric(args[1])) {
            HappyChest.getInstance().getLogger().log(Level.INFO, args[1] + " muss eine Zahl sein");
            return false;
        }
        
        ItemStack i = new ItemStack(Material.matchMaterial(args[0]), Integer.parseInt(args[1]));
        if(i == null) {
            HappyChest.getInstance().getLogger().log(Level.INFO, "Konnte kein Item aus " + str + " erstellen.");
            return false;
        }
        
        if(args.length >= 3) {
            for(int a = 2; a < args.length; a++) {
                String[] sargs = args[a].split(":");
                if(sargs.length == 2) {
                    switch(sargs[0]) {
                        case "lore":
                            String[] msg = sargs[1].split("|");
                            if(msg.length > 4)
                                break;
                            ItemMeta iml = i.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            for(int b = 0; b < msg.length; b++)
                                lore.add(msg[b]);
                            iml.setLore(lore);
                            i.setItemMeta(iml);
                            
                            break;
                        case "name":
                            ItemMeta imn = i.getItemMeta();
                            imn.setDisplayName(sargs[1]);
                            i.setItemMeta(imn);
                            break;
                        default:
                            if(Enchantment.getByName(sargs[0]) != null && isNumeric(sargs[1])) {
                                i.addEnchantment(Enchantment.getByName(sargs[0]), Integer.parseInt(sargs[1]));
                            } else {
                                HappyChest.getInstance().getLogger().log(Level.INFO, "Ignoriere " + sargs[0] + " da es nirgendwo rein passt.");
                            }
                            break;
                    }
                }
            }
        }
        return true;
    }
    public ItemStack getItem(String str) {
        String[] args = str.split(" ");
        if(args.length < 2) {
            HappyChest.getInstance().getLogger().log(Level.INFO, str + " hat zu wenig Argumente Mindestens : Itemname Itemmenge - sind gefordert.");
            return null;
        }
        
        if(!isNumeric(args[1])) {
            HappyChest.getInstance().getLogger().log(Level.INFO, args[1] + " muss eine Zahl sein");
            return null;
        }
        
        ItemStack i = new ItemStack(Material.matchMaterial(args[0]), Integer.parseInt(args[1]));
        if(i == null) {
            HappyChest.getInstance().getLogger().log(Level.INFO, "Konnte kein Item aus " + str + " erstellen.");
            return null;
        }
        
        if(args.length >= 3) {
            for(int a = 2; a < args.length; a++) {
                String[] sargs = args[a].split(":");
                if(sargs.length == 2) {
                    switch(sargs[0]) {
                        case "lore":
                            String[] msg = sargs[1].split("|");
                            if(msg.length > 4)
                                break;
                            ItemMeta iml = i.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            for(int b = 0; b < msg.length; b++)
                                lore.add(msg[b]);
                            iml.setLore(lore);
                            i.setItemMeta(iml);
                            
                            break;
                        case "name":
                            ItemMeta imn = i.getItemMeta();
                            imn.setDisplayName(sargs[1]);
                            i.setItemMeta(imn);
                            break;
                        default:
                            if(Enchantment.getByName(sargs[0]) != null && isNumeric(sargs[1])) {
                                i.addEnchantment(Enchantment.getByName(sargs[0]), Integer.parseInt(sargs[1]));
                            } else {
                                HappyChest.getInstance().getLogger().log(Level.INFO, "Ignoriere " + sargs[0] + " da es nirgendwo rein passt.");
                            }
                            break;
                    }
                }
            }
        }
        return i;
    }
    
    private boolean isNumeric(String str) {
        try {
            int num = Integer.valueOf(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
