package cz.jeme.programu.mobi.morphs;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import cz.jeme.programu.mobi.interfaces.Effectable;
import cz.jeme.programu.mobi.interfaces.Flyable;

public class Bat extends Morph implements Flyable, Effectable {
	public Bat() {
		setMaxHealth(6);
	}

	@Override
	public PotionEffect[] getEffects(Player player) {
		PotionEffect[] effects = {new PotionEffect(PotionEffectType.NIGHT_VISION, 12030, 255, true, false, false)};
		return effects;
	}

	@Override
	public void allowFlight(Player player) {
		player.setAllowFlight(true);
	}
	
}
