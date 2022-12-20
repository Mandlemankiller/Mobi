package cz.jeme.programu.mobi.morphs;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import cz.jeme.programu.mobi.CooldownManager;

public abstract class Morph {
	
	private double maxHealth = Integer.MIN_VALUE;
	
	protected CooldownManager cooldownManager = new CooldownManager();
	
	
	public void setPlayerHealth(Player player) {
		assert maxHealth != Integer.MIN_VALUE : "maxHealth was not assigned! (" + getClass().getName() + ")";
		assert maxHealth > 0 : "maxHealth is negative! (" + getClass().getName() + ")";
		player.sendMessage("notasserted");
		double newMaxHealth = maxHealth;
		AttributeInstance playerMaxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		
		double currentMaxHealth = playerMaxHealthAttribute.getBaseValue();
		double currentHealth = player.getHealth();
		double currentHealthPercentage = currentHealth / (currentMaxHealth / 100F);
		double newHealth = (newMaxHealth / 100F) * currentHealthPercentage;
		
		playerMaxHealthAttribute.setBaseValue(newMaxHealth);
		player.setHealth(newHealth);
	}
	
	protected void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
	}	
}
