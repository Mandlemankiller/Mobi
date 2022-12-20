package cz.jeme.programu.mobi;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import cz.jeme.programu.mobi.interfaces.Attackable;
import cz.jeme.programu.mobi.interfaces.Flyable;
import cz.jeme.programu.mobi.interfaces.Interactable;
import cz.jeme.programu.mobi.morphs.Morph;
import cz.jeme.programu.mobi.morphs.Skeleton;

public class MobiEventHandler implements Listener {

	private Config config;

	public MobiEventHandler(Config config) {
		this.config = config;
	}

	private Object getMorphObject(Player player) {
		String morph = config.players.get(player.getUniqueId());
		return Mobi.MORPHS.get(morph);
	}
	
	
	private void checkAllowFlight(Player player) {
		Object morphObject = getMorphObject(player);
		if (morphObject instanceof Flyable) {
			player.setAllowFlight(true);
			player.setFlySpeed((float) 0.05);
		} else if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
			player.sendMessage("hov");
			player.setAllowFlight(false);
		}
	}

	public void playerMorphed(Player player) {
		Object morphObject = getMorphObject(player);
		((Morph) morphObject).setPlayerHealth(player);
		checkAllowFlight(player);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!config.playerInSection(player)) {
			config.updatePlayer(player, "human");
			playerMorphed(player);
		}
		checkAllowFlight(player);
	}
	@EventHandler
	public void onEntityAttack(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			Object morphObject = getMorphObject(player);
			if (morphObject instanceof Attackable) {
				((Attackable) morphObject).attack(event);
			}
		}
	}
	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (config.players.get(player.getUniqueId()).equals("skeleton")) {
				Object morphObject = getMorphObject(player);
				((Skeleton) morphObject).shoot(event);
			}
		}
	}
	@EventHandler
	public void onPlayerItemDamage(PlayerItemDamageEvent event) {
		Player player = event.getPlayer();
		if (config.players.get(player.getUniqueId()).equals("skeleton")) {
			Object morphObject = getMorphObject(player);
			((Skeleton) morphObject).itemUsed(event);
		}

	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
			Player player = event.getPlayer();
			Object morphObject = getMorphObject(player);
			if (morphObject instanceof Interactable) {
				((Interactable) morphObject).interact(event);
			}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		checkAllowFlight(player);
	}
}
