package cz.jeme.programu.mobi.morphs;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public abstract class Morph {
	
	private double newMaxHealth;
	
	public Morph(double health) {
		this.newMaxHealth = health;
	}
	
	public void setHealth(Player player) {
		AttributeInstance playerMaxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		
		double currentMaxHealth = playerMaxHealthAttribute.getBaseValue();
		double currentHealth = player.getHealth();
		double currentHealthPercentage = currentHealth / (currentMaxHealth / 100);
		double newHealth = (newMaxHealth / 100) * currentHealthPercentage;
		
		playerMaxHealthAttribute.setBaseValue(newMaxHealth);
		player.setHealth(newHealth);
	}	
}
