package cz.jeme.programu.mobi.interfaces;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface Damageable {
	public void damaged(EntityDamageByEntityEvent event);
}
