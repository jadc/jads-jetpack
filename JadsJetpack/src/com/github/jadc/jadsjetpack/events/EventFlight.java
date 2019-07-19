package com.github.jadc.jadsjetpack.events;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.jadc.jadsjetpack.Main;
import com.github.jadc.jadsjetpack.tools.JetpackTools;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class EventFlight implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();

		if(!JetpackTools.isJetpack(p.getInventory().getChestplate())) return;
		if(p.isFlying()) return;
		if(p.isOnGround()) return;
		if(p.isSwimming()) return;
		if(p.getLocation().getBlock().isLiquid()) return;

		ItemStack playerJetpack = p.getInventory().getChestplate();
		ItemMeta meta = playerJetpack.getItemMeta();
		List<String> lore = meta.getLore();

		if(p.isSneaking()) {
			if(JetpackTools.getFuel(playerJetpack) > 0) {
				if(p.getGameMode() != GameMode.CREATIVE) {
	    			meta.setCustomModelData(JetpackTools.getFuel(playerJetpack) - 1);
	        		lore.set(0, JetpackTools.loreFormat(JetpackTools.getFuel(playerJetpack) - 1));
	        		meta.setLore(lore);
	        		playerJetpack.setItemMeta(meta);
	    		}

	    		// Status
	    		if(Main.showHUD) p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(JetpackTools.fuelDisplay(JetpackTools.getFuel(playerJetpack)) + " coal of fuel"));

	    		// Physics
	    		p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 99999, Main.flightSpeed, true));

	    		// Effects
	    		if(Main.playSounds) {
	    			p.getLocation().getWorld().playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 0.15F, (float)ThreadLocalRandom.current().nextDouble(0.0F, 1.0F));
	    		}
	    		if(Main.showParticles) {
	    			final double spread = 0.05;
	    			p.getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, p.getLocation(), 0, 0, 0, 0);
		    		p.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 0, ThreadLocalRandom.current().nextDouble(-spread, spread), 0, ThreadLocalRandom.current().nextDouble(-spread, spread));
	    			p.getLocation().getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 0, ThreadLocalRandom.current().nextDouble(-spread, spread), 0, ThreadLocalRandom.current().nextDouble(-spread, spread));
	    		}
			}else {
				// Effects
    			if(Main.playSounds) {
        			p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 0.05F, (float)ThreadLocalRandom.current().nextDouble(0.0F, 1.0F));
        		}

    			// Removes levitation if fuel supply is empty
    			if(p.hasPotionEffect(PotionEffectType.LEVITATION)) {
            		p.removePotionEffect(PotionEffectType.LEVITATION);
        		}
			}
    	}else {
			// Removes levitation if no longer crouching
    		if(p.hasPotionEffect(PotionEffectType.LEVITATION)) {
        		p.removePotionEffect(PotionEffectType.LEVITATION);
    		}
    	}

	}

	// Absorb fall damage
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			if(e.getCause() == DamageCause.FALL) {
				Player p = (Player) e.getEntity();
				if(JetpackTools.isJetpack(p.getInventory().getChestplate())) {
					e.setDamage(e.getDamage() / 5);
					if(Main.playSounds) {
	        			p.getLocation().getWorld().playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 0.5F, (float)ThreadLocalRandom.current().nextDouble(0.0F, 1.0F));
	        		}
				}
			}
		}
	}
}
