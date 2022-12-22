package cz.jeme.programu.mobi.morphs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import cz.jeme.programu.mobi.interfaces.Burnable;
import cz.jeme.programu.mobi.interfaces.Effectable;

public class Skeleton extends Morph implements Burnable, Effectable {
	public Skeleton() {
		setMaxHealth(20);
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
	@Override
	public PotionEffect[] getEffects(Player player) {
		PotionEffect[] effects = {new PotionEffect(PotionEffectType.NIGHT_VISION, 12030, 255, true, false, false)};
		return effects;
	}
}
