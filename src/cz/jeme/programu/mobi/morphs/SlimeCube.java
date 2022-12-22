package cz.jeme.programu.mobi.morphs;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import cz.jeme.programu.mobi.interfaces.Attackable;
import cz.jeme.programu.mobi.interfaces.Damageable;
import cz.jeme.programu.mobi.interfaces.Morphable;
import net.md_5.bungee.api.ChatColor;

public abstract class SlimeCube extends Morph implements Damageable, Morphable, Attackable {

	private int big = Integer.MIN_VALUE;
	private int medium = Integer.MIN_VALUE;
	private int small = Integer.MIN_VALUE;
	
	public SlimeCube() {
	}

	@Override
	public void damaged(EntityDamageEvent event) {
		Player player = (Player) event.getEntity();
		ItemStack mainItem = player.getInventory().getItemInMainHand();
		ItemStack offItem = player.getInventory().getItemInOffHand();
		
		if (!cooldownManager.getCooldownExpired(player.getUniqueId(), Material.SLIME_BALL, 3000)) {
			event.setCancelled(true);
			return;
		}
		
		if (mainItem != null && offItem != null) {
			Material mainMaterial = mainItem.getType();
			Material offMaterial = offItem.getType();
			boolean totem = mainMaterial == Material.TOTEM_OF_UNDYING || offMaterial == Material.TOTEM_OF_UNDYING;
			boolean lastLife = getMobiData().getMorphData(player, "life").equals("0");
			boolean damageCauseCorrect = event.getCause() != DamageCause.VOID;
			if (totem && lastLife && damageCauseCorrect) {
					return;
			}
		}

		if (player.getHealth() + player.getAbsorptionAmount() - event.getDamage() < 1) {
			String mobiData = getMobiData().getMorphData(player, "life");
			int life = Integer.parseInt(mobiData);

			if (life == 2) {
				getMobiData().setMorphData(player, "life", "1");
				player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(4);
			} else if (life == 1) {
				getMobiData().setMorphData(player, "life", "0");
				player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1);
			} else if (life == 0) {
				getMobiData().setMorphData(player, "life", "2");
				player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(16);
				return;
			}
			player.removePotionEffect(PotionEffectType.JUMP);
			event.setCancelled(true);
			player.sendMessage(String.valueOf(life));
			player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
			player.playSound(player, Sound.ITEM_TOTEM_USE, SoundCategory.MASTER, 100, 1);
			player.sendTitle(ChatColor.RED + "❤", String.valueOf(life) + " remaining!", 10, 20, 10);
			cooldownManager.setTimeStamp(player.getUniqueId(), Material.SLIME_BALL); // Slime ball used just for reference here, it has no effect on the item itself
		}
	}
	
	@Override
	public void onMorph(Player player) {
		getMobiData().setMorphData(player, "life", "2");
	}
	
	@Override
	public void attack(EntityDamageByEntityEvent event) {
		assert big != Integer.MIN_VALUE : "Big value was not initialized!";
		assert medium != Integer.MIN_VALUE : "Medium value was not initialized!";
		assert small != Integer.MIN_VALUE : "Small value was not initialized!";
		
		Player player = (Player) event.getDamager();
		if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
			return;
		}
		int damage = switch (Integer.parseInt(getMobiData().getMorphData(player, "life"))) {
		case 2: {
			yield big;
		}
		case 1: {
			yield medium;
		}
		case 0: {
			yield small;
		}
		default:
			throw new IllegalArgumentException("Unexpected value!");
		};
		if (damage == 0) {
			event.setCancelled(true);
		} else {
			event.setDamage(damage);
		}
	}
	
	protected void setDamages(int big, int medium, int small) {
		this.big = big;
		this.medium = medium;
		this.small = small;
	}
}