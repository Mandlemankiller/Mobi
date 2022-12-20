package cz.jeme.programu.mobi.schedulers;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;

import cz.jeme.programu.mobi.Config;
import cz.jeme.programu.mobi.Mobi;
import cz.jeme.programu.mobi.interfaces.Burnable;

public class Burn extends BukkitRunnable {
	private Config config;
	private Random random = new Random();

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
			boolean isCorrectGamemode = gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE;

			if (isOverworld && isDaytime && isBurningMob && isCorrectGamemode && world.isClearWeather()) {

				int topBlockY = world.getHighestBlockYAt(location);
				Location topBlockLocation = new Location(world, location.getX(), topBlockY, location.getZ());
				while (!world.getBlockAt(topBlockLocation).getType().isOccluding()) {
					topBlockLocation.setY(topBlockLocation.getY() - 1);
				}
				topBlockY = topBlockLocation.getBlockY();

				if (location.getBlockY() > topBlockY) {
					ItemStack item = player.getInventory().getHelmet();
					if (item != null) {
						Damageable meta = (Damageable) item.getItemMeta();
						int itemDamage = meta.getDamage();
						int unbreakingLevel = meta.getEnchantLevel(Enchantment.DURABILITY);

						float damageProbability = 100 / (unbreakingLevel + 1F);

						int randInt = random.nextInt(100) + 1;
						if (randInt <= damageProbability) {
							if (random.nextInt(100) + 1 <= 20) {
								meta.setDamage(itemDamage + 1);
							}
						}
						if (meta.getDamage() == item.getType().getMaxDurability()) {
							player.getInventory().setHelmet(new ItemStack(Material.AIR));
							player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 1F);
						} else {
							item.setItemMeta(meta);
						}
					} else {
						player.setFireTicks(160);
					}
				}
			}
		}

	}
}
