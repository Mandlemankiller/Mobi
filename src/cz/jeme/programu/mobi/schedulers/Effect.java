package cz.jeme.programu.mobi.schedulers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import cz.jeme.programu.mobi.Config;
import cz.jeme.programu.mobi.Mobi;
import cz.jeme.programu.mobi.interfaces.Effectable;

public class Effect extends BukkitRunnable {
	
	private Config config;
	
	public Effect(Config config) {
		this.config = config;
	}
	
	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			String morphName = config.players.get(player.getUniqueId());
			Object morph = Mobi.MORPHS.get(morphName);
			if (morph instanceof Effectable) {
				PotionEffect[] effects = ((Effectable) morph).getEffects();
				for (PotionEffect effect : effects) {
					player.addPotionEffect(effect);
				}
			}
		}
	}

}
