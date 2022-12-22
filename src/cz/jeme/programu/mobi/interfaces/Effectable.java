package cz.jeme.programu.mobi.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public interface Effectable {
	public PotionEffect[] getEffects(Player player);
}
