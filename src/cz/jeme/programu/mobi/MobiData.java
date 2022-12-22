package cz.jeme.programu.mobi;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import cz.jeme.programu.mobi.schedulers.EffectManager;

public class MobiData {

	// Yaml configuration variables
	private static final String DATA_FILE_NAME = "data.yml";
	private static final String DATA_SECTION_NAME = "Data";

	public File dataFile = null;
	public FileConfiguration dataFileYaml = null;
	public ConfigurationSection dataFileYamlSection = null;

	// Player maps
	public Map<UUID, String> players = new HashMap<UUID, String>();
	public Map<String, Set<UUID>> morphs = new HashMap<String, Set<UUID>>();

	private File dataDir;

	public MobiData(File dataDir) {
		this.dataDir = dataDir;

		if (!dataDir.exists()) {
			dataDir.mkdir();
		}

		reloadData();
		loadPlayers();
	}

	public void reloadData() {
		refreshDataVars();
		if (!(dataFile.exists())) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				Mobi.serverLog(Level.SEVERE, e.toString());
			}
		}
		saveDataFile();
	}

	private void saveDataFile() {

		try {
			dataFileYaml.save(dataFile);

		} catch (IOException e) {
			Mobi.serverLog(Level.SEVERE, e.toString());
		}
	}

	private void refreshDataVars() {
		dataFile = new File(dataDir, DATA_FILE_NAME);
		dataFileYaml = YamlConfiguration.loadConfiguration(dataFile);

		dataFileYamlSection = dataFileYaml.getConfigurationSection(DATA_SECTION_NAME);
		if (dataFileYamlSection == null) {
			dataFileYamlSection = dataFileYaml.createSection(DATA_SECTION_NAME);
		}
	}

	public void updatePlayer(Player player, String value) {
		new EffectManager(this).clearPreviousEffects(player);
		players.put(player.getUniqueId(), value);
		if (!morphs.containsKey(value)) {
			morphs.put(value, new HashSet<UUID>());
		}
		morphs.get(value).add(player.getUniqueId());
		
		String uuid = player.getUniqueId().toString();
		dataFileYamlSection.set(uuid + "." + "morph", value);
		saveDataFile();
	}

	private void loadPlayers() {
		for (String key : dataFileYamlSection.getKeys(false)) {
			String value = dataFileYamlSection.getConfigurationSection(key).getString("morph");
			players.put(UUID.fromString(key), value);
			if (!morphs.containsKey(key)) {
				morphs.put(value, new HashSet<UUID>());
			}
			morphs.get(value).add(UUID.fromString(key));
		}
	}

	public boolean playerInSection(Player player) {
		return dataFileYamlSection.getKeys(false).contains(player.getUniqueId().toString());
	}

	public void setMorphData(Player player, String key, String value) {
		UUID uuid = player.getUniqueId();
		String morph = players.get(uuid);
		dataFileYamlSection.set(uuid.toString() + "." + morph + "." + key, value);
		saveDataFile();
	}

	public String getMorphData(Player player, String key) {
		UUID uuid = player.getUniqueId();
		String morph = players.get(uuid);
		return dataFileYamlSection.getString(uuid.toString() + "." + morph + "." + key);
	}
	
	public void deleteMorphData(Player player) {
		UUID uuid = player.getUniqueId();
		String morph = players.get(uuid);
		dataFileYamlSection.set(uuid + "." + morph, null);
		saveDataFile();
	}
}
