package cz.jeme.programu.mobi.morphs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import cz.jeme.programu.mobi.interfaces.Interactable;
import net.md_5.bungee.api.ChatColor;
import cz.jeme.programu.mobi.interfaces.Flyable;

public class Ghast extends Morph implements Interactable, Flyable {

	public Ghast() {
		super(10);
	}

	@Override
	public void interact(PlayerInteractEvent event) {
		if (event.getItem() == null) {
			return;
		}
		if (event.getItem().getType() == Material.FIRE_CHARGE) {
			Player player = event.getPlayer();
			if (event.getAction() == Action.RIGHT_CLICK_AIR) {

				Location location = player.getLocation();
				location.setY(location.getY() - 1);
				Fireball fireball = player.launchProjectile(Fireball.class);
				fireball.setDirection(location.getDirection());

			} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (!player.isSneaking()) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "Because fire charge has an ability assigned, you can only use it while sneaking.");
				}
			}
		}
	}

	@Override
	public void allowFlight(Player player) {
		player.setAllowFlight(true);
	}

}
