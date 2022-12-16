package cz.jeme.programu.mobi.interfaces;

import org.bukkit.event.player.PlayerItemConsumeEvent;

public interface Consumable {
	public void consume(PlayerItemConsumeEvent event);
}
