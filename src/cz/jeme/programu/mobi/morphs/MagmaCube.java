package cz.jeme.programu.mobi.morphs;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import cz.jeme.programu.mobi.interfaces.Effectable;
import cz.jeme.programu.mobi.interfaces.Moveable;

public class MagmaCube extends SlimeCube implements Effectable, Moveable {

	public MagmaCube() {
		setMaxHealth(16);
		setDamages(6, 4, 3);
	}

	@Override
	public PotionEffect[] getEffects(Player player) {
		int life = Integer.parseInt(getMobiData().getMorphData(player, "life"));

		PotionEffect[] effects = { new PotionEffect(PotionEffectType.JUMP, 12030, life, true, false, false),
				new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 12030, 0, true, false, false) };
		return effects;
	}

	@Override
	public void move(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		Block legsBlock = player.getLocation().getBlock();
		Block headBlock = player.getLocation().add(0, 1, 0).getBlock();
		
		Set<Material> liquids = new HashSet<Material>();
		
		liquids.add(Material.WATER);
		liquids.add(Material.LAVA);
		liquids.add(Material.BUBBLE_COLUMN);
		
		boolean bodyInLiquid = liquids.contains(legsBlock.getType()) || liquids.contains(headBlock.getType());
		
		if (bodyInLiquid) {
			player.setAllowFlight(true);
			player.setFlying(true);
			player.setFlySpeed(0.06F);
		} else {
			player.setAllowFlight(false);
			player.setFlying(false);
		}
	}

}
