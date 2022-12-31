package cz.jeme.programu.mobi.morphs;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import cz.jeme.programu.mobi.interfaces.Attackable;
import cz.jeme.programu.mobi.interfaces.Flyable;
import cz.jeme.programu.mobi.interfaces.WaterDamagable;

public class Bee extends Morph implements Flyable, Attackable, WaterDamagable {
	
	public Bee() {
		setMaxHealth(10);
	}
	
	@Override
	public void attack(EntityDamageByEntityEvent event) {
		LivingEntity entity = (LivingEntity) event.getEntity();
		entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0));
	}

	@Override
	public void allowFlight(Player player) {
		player.setAllowFlight(true);
	}
}
