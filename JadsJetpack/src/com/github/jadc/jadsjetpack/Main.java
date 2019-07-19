package com.github.jadc.jadsjetpack;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.jadc.jadsjetpack.events.EventFlight;
import com.github.jadc.jadsjetpack.events.EventRefuel;
import com.github.jadc.jadsjetpack.recipes.RecipeJetpack;

public class Main extends JavaPlugin {
	
	public static Main instance;
	
	// Config file variable declaration
	public static List<String> recipe = new ArrayList<String>();
	public static int flightSpeed;
	public static boolean showHUD;
	public static boolean showParticles;
	public static boolean playSounds;
	
	public void onEnable() {
		instance = this;
		
		// Config file variable initialization
		saveDefaultConfig();
		
		if(getConfig().getInt("flightSpeed") < 0 || getConfig().getInt("flightSpeed") > 127) {
			Bukkit.getLogger().log(Level.WARNING, "'flightSpeed' key in Jad's Jetpack config.yml is set to an out of bounds value. (Valid range is 0-127 inclusive)");
			flightSpeed = 10;
		}else {
			flightSpeed = getConfig().getInt("flightSpeed");
		}
		
		recipe = getConfig().getStringList("recipe");
		showHUD = getConfig().getBoolean("showHUD");
		showParticles = getConfig().getBoolean("showParticles");
		playSounds = getConfig().getBoolean("playSounds");
		
		// Initializations
		Bukkit.getPluginManager().registerEvents(new EventFlight(), this);
		Bukkit.getPluginManager().registerEvents(new EventRefuel(), this);
		RecipeJetpack.register();
	}
	
	public void onDisable() {
		
	}
	
	public static Main getInstance() {
		return instance;
	}
}
