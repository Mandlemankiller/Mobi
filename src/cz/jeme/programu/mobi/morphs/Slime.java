package cz.jeme.programu.mobi.morphs;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import cz.jeme.programu.mobi.interfaces.Effectable;

public class Slime extends SlimeCube implements Effectable {

	public Slime() {
		setMaxHealth(16);
		setDamages(4, 2, 0);
	}

	@Override
	public PotionEffect[] getEffects(Player player) {
		int life = Integer.parseInt(getMobiData().getMorphData(player, "life"));

		if (life == 0) {
			return new PotionEffect[0];
		}

		PotionEffect[] effects = { new PotionEffect(PotionEffectType.JUMP, 12030, life - 1, true, false, false) };
		return effects;

	}
}
