package cz.jeme.programu.mobi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;

public class CooldownManager {
	private Map<UUID, Map<Material, Long>> cooldowns = new HashMap<UUID, Map<Material, Long>>();

	public void setTimeStamp(UUID uuid, Material item) {
		if (!cooldowns.containsKey(uuid)) {
			cooldowns.put(uuid, new HashMap<Material, Long>());
		}
		cooldowns.get(uuid).put(item, System.currentTimeMillis());
	}

	public long getTimeStamp(UUID uuid, Material item) {
		if (cooldowns.containsKey(uuid) && cooldowns.get(uuid).containsKey(item)) {
			return cooldowns.get(uuid).get(item);
		} else {
			return 0;
		}
	}

	public boolean getCooldownExpired(UUID uuid, Material item, long time) {
		return getTimeStampDifference(uuid, item) >= time;
	}
	
	public long getTimeStampDifference(UUID uuid, Material item) {
		if (cooldowns.containsKey(uuid) && cooldowns.get(uuid).containsKey(item)) {
			return System.currentTimeMillis() - cooldowns.get(uuid).get(item);
		} else {
			
			return Long.MAX_VALUE;
		}
	}
}
