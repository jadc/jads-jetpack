package com.github.jadc.jadsjetpack.recipes;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapelessRecipe;

import com.github.jadc.jadsjetpack.Main;
import com.github.jadc.jadsjetpack.tools.JetpackTools;

public class RecipeJetpack {
	public static void register() {
		ShapelessRecipe r = new ShapelessRecipe(new NamespacedKey(Main.getInstance(), Main.getInstance().getDescription().getName()), JetpackTools.getItem());
		if(Main.recipe.size() > 9) {
			Bukkit.getLogger().log(Level.SEVERE, "'recipe' key in Jad's Jetpack config.yml contains an invalid quantity of elements. You cannot have more than 9 ingredients in a recipe.");
			Bukkit.getLogger().log(Level.INFO, "Jetpack recipe disabled.");
			return;
		}
		for(String s : Main.recipe) {
			Material m = Material.getMaterial(s);
			if(m == null) {
				Bukkit.getLogger().log(Level.SEVERE, "'recipe' key in Jad's Jetpack config.yml contains an invalid material. You might have spelt it wrong.");
				Bukkit.getLogger().log(Level.INFO, "Jetpack recipe disabled.");
				return;
			}else {
				r.addIngredient(m);
			}
		}
		Bukkit.addRecipe(r);
	}
}
