package cz.jeme.programu.mobi.schedulers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import cz.jeme.programu.mobi.Mobi;
import cz.jeme.programu.mobi.MobiData;
import cz.jeme.programu.mobi.interfaces.Effectable;
import cz.jeme.programu.mobi.morphs.Morph;

public class EffectManager extends BukkitRunnable {
	
	private MobiData mobiData;
	
	public EffectManager(MobiData mobiData) {
		this.mobiData = mobiData;
	}
	
	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			String morphName = mobiData.players.get(player.getUniqueId());
			Morph morph = Mobi.MORPHS.get(morphName);
			if (morph instanceof Effectable) {
				PotionEffect[] effects = ((Effectable) morph).getEffects(player);
				for (PotionEffect effect : effects) {
					player.addPotionEffect(effect);
				}
			}
		}
	}
	
	public void clearPreviousEffects(Player player) {
		String morphName = mobiData.players.get(player.getUniqueId());
		Morph morph = Mobi.MORPHS.get(morphName);
		if (morph instanceof Effectable) {
			PotionEffect[] effects = ((Effectable) morph).getEffects(player);
			for (PotionEffect effect : effects) {
				PotionEffectType type = effect.getType();
				player.removePotionEffect(type);
			}
		}
	}

}
