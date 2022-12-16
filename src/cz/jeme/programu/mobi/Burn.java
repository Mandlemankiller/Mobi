package cz.jeme.programu.mobi;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cz.jeme.programu.mobi.interfaces.Burnable;

public class Burn extends BukkitRunnable {
	private Config config;

	public Burn(Config config) {
		this.config = config;
	}

	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			Location location = player.getLocation();
			World world = location.getWorld();

			Environment enviroment = world.getEnvironment();
			boolean isOverworld = enviroment == Environment.NORMAL;

			long time = world.getTime();
			boolean isDaytime = time >= 0 && time <= 12000;

			String morph = config.players.get(player.getUniqueId());
			boolean isBurningMob = Mobi.MORPHS.get(morph) instanceof Burnable;
			
			GameMode gameMode = player.getGameMode();
			boolean isSurival = gameMode == GameMode.SURVIVAL;

			if (isOverworld && isDaytime && isBurningMob && isSurival) {
				int topBlockY = world.getHighestBlockYAt(location);
				if (location.getBlockY() > topBlockY) {
					player.setFireTicks(160);
				}
			}

		}
	}

}
