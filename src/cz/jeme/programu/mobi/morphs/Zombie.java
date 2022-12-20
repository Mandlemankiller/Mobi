package cz.jeme.programu.mobi.morphs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import cz.jeme.programu.mobi.interfaces.Attackable;
import cz.jeme.programu.mobi.interfaces.Burnable;
import cz.jeme.programu.mobi.interfaces.Effectable;

public class Zombie extends Morph implements Attackable, Burnable, Effectable {
	
	public Zombie() {
		setMaxHealth(20);
	}
	@Override
	public void attack(EntityDamageByEntityEvent event) {
		Player player = (Player) event.getDamager();
		if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
			event.setDamage(3);
		}
	}
	@Override
	public PotionEffect[] getEffects() {
		PotionEffect[] effects = {new PotionEffect(PotionEffectType.NIGHT_VISION, 240, 255, true, false, false)};
		return effects;
	}
}
