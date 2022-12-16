package cz.jeme.programu.mobi.interfaces;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface Attackable {
	public void attack(EntityDamageByEntityEvent event);
}
