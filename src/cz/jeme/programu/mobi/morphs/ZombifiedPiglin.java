package cz.jeme.programu.mobi.morphs;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import cz.jeme.programu.mobi.interfaces.Attackable;
import cz.jeme.programu.mobi.interfaces.Effectable;

public class ZombifiedPiglin extends Morph implements Attackable, Effectable {

	public ZombifiedPiglin() {
		setMaxHealth(20);
	}

	@Override
	public void attack(EntityDamageByEntityEvent event) {
		Player player = (Player) event.getDamager();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item.getType() == Material.GOLDEN_SWORD) {
			double damage = 8;
			
			int sharpnessLevel = item.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
			
			if (sharpnessLevel != 0) {
				damage = damage + (0.5 * (Math.max(0, sharpnessLevel) - 1) + 1.0);
			}
			// TODO I am too lazy to add bane of arthropods and smite right now
		}
	}

	@Override
	public PotionEffect[] getEffects(Player player) {
		return new PotionEffect[] {new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 12030, 255, true, false, false)};
	}
}
