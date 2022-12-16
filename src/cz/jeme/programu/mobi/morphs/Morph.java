package cz.jeme.programu.mobi.morphs;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public abstract class Morph {
	
	private double maxHealth = Integer.MIN_VALUE;
	
	public void setPlayerHealth(Player player) {
		assert maxHealth != Integer.MIN_VALUE : "maxHealth was not assigned! (" + getClass().getName() + ")";
		assert maxHealth > 0 : "maxHealth is negative! (" + getClass().getName() + ")";
		player.sendMessage("notasserted");
		double newMaxHealth = maxHealth;
		AttributeInstance playerMaxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		
		double currentMaxHealth = playerMaxHealthAttribute.getBaseValue();
		double currentHealth = player.getHealth();
		double currentHealthPercentage = currentHealth / (currentMaxHealth / 100);
		double newHealth = (newMaxHealth / 100) * currentHealthPercentage;
		
		playerMaxHealthAttribute.setBaseValue(newMaxHealth);
		player.setHealth(newHealth);
	}
	
	protected void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
	}
}
