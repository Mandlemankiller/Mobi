package cz.jeme.programu.mobi.morphs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import cz.jeme.programu.mobi.interfaces.Burnable;

public class Skeleton extends Morph implements Burnable {
	public Skeleton() {
		super(20);
	}
	public void shoot(EntityShootBowEvent event) {
		Player player = (Player) event.getEntity();
		event.setConsumeItem(false);
		player.updateInventory();
	}
	public void itemUsed(PlayerItemDamageEvent event) {
		if (event.getItem().getType() == Material.BOW) {
			event.setCancelled(true);
		}
	}
}
