/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Pappi
 */
public class Arena {
    String _name;
    Location _pos1;
    Location _pos2;
    
    List<Location> _chests = new ArrayList<>();
    List<String> _items = new ArrayList<>();
    boolean _oneForAll = false;
    
    public Arena(String name, Location loc1, Location loc2, List<Location> chest) {
        _name = name;
        _pos1 = loc1;
        _pos2 = loc2;
        _chests = chest;
    }
    
    public void setOneForAll(boolean bl) {
        _oneForAll = bl;
    }
    
    public boolean getOneForAll() {
        return _oneForAll;
    }
    
    public void addChest(Location loc) {
        _chests.add(loc);
    }
    
    public void delChest(Location loc) {
        _chests.remove(loc);
    }
    
    public List<Location> getChests() {
        return _chests;
    }
    
    public void addItem(String str) {
        if(Items.isItem(str))
            _items.add(str);
    }
    
    public void setItemList(List<String> str) {
        _items = str;
    }
    
    public int getItemCount() {
        return _items.size();
    }
    
    public ItemStack getItem(int i) {
        if(i <= getItemCount())
            return Items.getItem(_items.get(i));
        return null;
    }
    
    public List<String> getItemList() {
        return _items;
    }
    
    public List<ItemStack> getItemStackList() {
        List<ItemStack> list = new ArrayList<>();
        for(String str: _items) {
            ItemStack item = Items.getItem(str);
            if(item != null)
                list.add(item);
        }
        return list;
    }
    
    public void removeItem(int i) {
        if(i == -1)
            _items.clear();
        if(i < _items.size())
            _items.remove(i);
    }
    
    public String getName() {
        return _name;
    }
    
    public void setName(String name) {
        _name = name;
    }
    
    public Location getPos1() {
        return _pos1;
    }
    
    public void setPos1(Location loc) {
        _pos1 = loc;
    }
    
    public Location getPos2() {
        return _pos2;
    }
    
    public void setPos2(Location loc) {
        _pos2 = loc;
    }
    
    public void reloadChests(Player p) {
        List<Location> chest = Blocks.getChestsListU(_pos1, _pos2);
        if(chest.size() < 2) {
            if(p != null)
                Utils.sendMessage(p, "&c Konnte keine Truhen mehr finden.");
            _chests = chest;
            return;
        }
        _chests = chest;
        if(p != null)
            Utils.sendMessage(p, "&eEs wurden " + chest.size() + " Truhen in dieser Arena gefunden.");
    }
    
    public boolean isInside(Location loc) {
        //Prüfe Welt
        if(loc.getWorld() != _pos1.getWorld())
            return false;
        //Prüfe X
        if(loc.getBlockX() < _pos1.getBlockX() && loc.getBlockX() < _pos2.getBlockX() && loc.getBlockX() > _pos1.getBlockX() && loc.getBlockX() > _pos2.getBlockX())
            return false;
        //Prüfe Y
        if(loc.getBlockY() < _pos1.getBlockY() && loc.getBlockY() < _pos2.getBlockY() && loc.getBlockY() > _pos1.getBlockY() && loc.getBlockY() > _pos2.getBlockY())
            return false;
        //Prüfe Z
        if(loc.getBlockZ() < _pos1.getBlockZ() && loc.getBlockZ() < _pos2.getBlockZ() && loc.getBlockZ() > _pos1.getBlockZ() && loc.getBlockZ() > _pos2.getBlockZ())
            return false;
        return true;
    }
}
