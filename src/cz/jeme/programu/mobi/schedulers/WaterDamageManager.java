package cz.jeme.programu.mobi.schedulers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cz.jeme.programu.mobi.Mobi;
import cz.jeme.programu.mobi.MobiData;
import cz.jeme.programu.mobi.interfaces.WaterDamagable;
import cz.jeme.programu.mobi.morphs.Morph;

public class WaterDamageManager extends BukkitRunnable {
	
	MobiData mobiData;
	
	public WaterDamageManager(MobiData mobiData) {
		this.mobiData = mobiData;
	}
	

	@Override
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			String morphName = mobiData.players.get(player.getUniqueId());
			Morph morph = Mobi.MORPHS.get(morphName);
			if (!(morph instanceof WaterDamagable)) {
				return;
			}
			Block legs = player.getLocation().getBlock();
			Location headLocation = player.getLocation();
			headLocation.setY(headLocation.getY()+1);
			Block head = headLocation.getBlock();
			
			boolean legsInWater = legs.getType() == Material.WATER || legs.getType() == Material.BUBBLE_COLUMN;
			boolean headInWater = head.getType() == Material.WATER || head.getType() == Material.BUBBLE_COLUMN;
			
			boolean legsWaterlogged = false;
			boolean headWaterlogged = false;
			
			if (legs.getBlockData() instanceof Waterlogged) {
				legsWaterlogged = ((Waterlogged) legs.getBlockData()).isWaterlogged();
			}
			
			if (head.getBlockData() instanceof Waterlogged) {
				headWaterlogged = ((Waterlogged) head.getBlockData()).isWaterlogged();
			}
			
			if (legsInWater || headInWater || headWaterlogged || legsWaterlogged) {
				player.damage(1);
			}
		}
	}

}
