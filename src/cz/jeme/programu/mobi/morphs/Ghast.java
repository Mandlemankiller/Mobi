package cz.jeme.programu.mobi.morphs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import cz.jeme.programu.mobi.CooldownManager;
import cz.jeme.programu.mobi.interfaces.Flyable;
import cz.jeme.programu.mobi.interfaces.Interactable;
import net.md_5.bungee.api.ChatColor;

public class Ghast extends Morph implements Interactable, Flyable {

	private CooldownManager cooldownManager;

	public Ghast() {
		setMaxHealth(10);
		this.cooldownManager = new CooldownManager();
	}

	@Override
	public void interact(PlayerInteractEvent event) {
		if (event.getItem() == null) {
			return;
		}
		Material item = event.getItem().getType();
		if (item == Material.FIRE_CHARGE) {
			Player player = event.getPlayer();
			switch (event.getAction()) {
			case RIGHT_CLICK_AIR:
				long cooldownTime = 3000;
				if (cooldownManager.getCooldownExpired(player.getUniqueId(), item, cooldownTime)) {
					Location location = player.getLocation();
					location.setY(location.getY() - 1);
					Fireball fireball = player.launchProjectile(Fireball.class);
					fireball.setDirection(location.getDirection());
					cooldownManager.setTimeStamp(player.getUniqueId(), item);
				} else {
					long timeStampDifference = cooldownManager.getTimeStampDifference(player.getUniqueId(), item);
					double remaining = (cooldownTime - timeStampDifference)/1000d;
					player.sendMessage(ChatColor.RED + "Your ability will be availible again in " + String.format("%.2f", remaining) + " seconds.");
				}
				break;
			case RIGHT_CLICK_BLOCK:
				if (!player.isSneaking()) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED
							+ "Because fire charge has an ability assigned, you can only use it while sneaking.");
				}
				break;
			}
		}
	}

	@Override
	public void allowFlight(Player player) {
		player.setAllowFlight(true);
	}

}
