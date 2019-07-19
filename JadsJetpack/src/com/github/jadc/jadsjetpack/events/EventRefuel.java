package com.github.jadc.jadsjetpack.events;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.jadc.jadsjetpack.Main;
import com.github.jadc.jadsjetpack.tools.JetpackTools;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class EventRefuel implements Listener {
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getHand() == EquipmentSlot.HAND) {
			if(e.getAction() != Action.RIGHT_CLICK_AIR) return;
			
			int newTicks = -1;
			if(e.getItem().getType() == Material.COAL) newTicks = 40;
			if(e.getItem().getType() == Material.CHARCOAL) newTicks = 40;
			if(e.getItem().getType() == Material.COAL_BLOCK) newTicks = 400;
			if(e.getItem().getType() == Material.LAVA_BUCKET) newTicks = 500;
			if(newTicks == -1) return;
			
			if(JetpackTools.isJetpack(e.getPlayer().getInventory().getChestplate())) {
				// Lore change
				ItemStack playerJetpack = e.getPlayer().getInventory().getChestplate();
				ItemMeta meta = playerJetpack.getItemMeta();
				List<String> lore = meta.getLore();
				meta.setCustomModelData(JetpackTools.getFuel(playerJetpack) + newTicks);
				lore.set(0, JetpackTools.loreFormat(JetpackTools.getFuel(playerJetpack) + newTicks));
	    		meta.setLore(lore);
				playerJetpack.setItemMeta(meta);
				
				// Consumption
				if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
					if(e.getItem().getType() == Material.LAVA_BUCKET) {
						e.getItem().setType(Material.BUCKET);
						e.getItem().setAmount(1);
					}else {
						if(e.getItem().getAmount() > 1) {
							e.getItem().setAmount(e.getItem().getAmount() - 1);
						}else {
							e.getItem().setAmount(0);
						}
					}
				}
				
				// Effects/Status
				String text = ChatColor.GREEN + "+" + (newTicks / 40) + " " + ChatColor.RESET + JetpackTools.fuelDisplay(JetpackTools.getFuel(playerJetpack)) + " coal";
				if(Main.showHUD) {
					e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
				}else {
					e.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Jetpack Fuel: " + text);
				}
				if(Main.playSounds) e.getPlayer().getLocation().getWorld().playSound(e.getPlayer().getLocation(), Sound.BLOCK_PISTON_CONTRACT, 0.25F, 0.1F);
			}
		}
	}
}
