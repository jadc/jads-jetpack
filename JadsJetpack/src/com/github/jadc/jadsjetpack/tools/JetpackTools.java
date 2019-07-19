package com.github.jadc.jadsjetpack.tools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JetpackTools implements Listener {
	
	private static DecimalFormat decFormat = new DecimalFormat("0.0");
	
	public static ItemStack getItem() {
		ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Jetpack");
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setCustomModelData(0);
		
		List<String> lore = new ArrayList<String>();
		lore.add(loreFormat(0));
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return item;
	}
	
	public static boolean isJetpack(ItemStack i) {
		if(i == null) return false;
		if(!i.hasItemMeta()) return false;
		if(i.getItemMeta() == null) return false;
		if(!i.getItemMeta().hasLore()) return false;
		if(i.getItemMeta().getLore() == null) return false;
		if(!i.getItemMeta().getLore().get(0).contains("Fueled")) return false;
		return true;
	}
	
	public static String fuelDisplay(int fuel) {
		double num = ((double)fuel - 1) / 40;
		if(num < 0) num = 0;
		return decFormat.format(num);
	}
	
	public static String loreFormat(int fuel) {
		return ChatColor.GRAY + "Fueled with " + fuelDisplay(fuel) + " coal";
	}
	
	public static int getFuel(ItemStack i) {
		return i.getItemMeta().getCustomModelData();
	}
	
	// Debug
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.getPlayer().getInventory().addItem(JetpackTools.getItem());
	}
}
