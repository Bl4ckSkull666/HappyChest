/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.papaharni.happychest.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
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
    
    Items _item = new Items();
    
    public Arena(String name, Location loc1, Location loc2) {
        _name = name;
        _pos1 = loc1;
        _pos2 = loc2;
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
        if(_item.isItem(str))
            _items.add(str);
    }
    
    public int getItemCount() {
        return _items.size();
    }
    
    public ItemStack getItem(int i) {
        if(i <= getItemCount())
            return _item.getItem(_items.get(i));
        return null;
    }
    
    public List<String> getItemList() {
        return _items;
    }
    
    public List<ItemStack> getItemStackList() {
        List<ItemStack> list = new ArrayList<>();
        for(String str: _items) {
            ItemStack item = _item.getItem(str);
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
}
