package cz.jeme.programu.mobi.morphs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import cz.jeme.programu.mobi.interfaces.Attackable;
import cz.jeme.programu.mobi.interfaces.Burnable;

public class Zombie extends Morph implements Attackable, Burnable {
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
}
