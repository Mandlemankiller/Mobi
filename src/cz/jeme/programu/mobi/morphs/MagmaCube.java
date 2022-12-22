package cz.jeme.programu.mobi.morphs;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import cz.jeme.programu.mobi.interfaces.Effectable;

public class MagmaCube extends SlimeCube implements Effectable {

	public MagmaCube() {
		setMaxHealth(16);
		setDamages(6, 4, 3);
	}

	@Override
	public PotionEffect[] getEffects(Player player) {
		int life = Integer.parseInt(getMobiData().getMorphData(player, "life"));

		PotionEffect[] effects = { new PotionEffect(PotionEffectType.JUMP, 12030, life, true, false, false) };
		return effects;
	}

}
