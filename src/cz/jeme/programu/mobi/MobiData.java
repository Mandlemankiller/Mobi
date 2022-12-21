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
	private final Map<String, String> NAMES = new HashMap<String, String>();
	private final Map<String, ConfigurationSection> SECTIONS = new HashMap<String, ConfigurationSection>();

	private static final String DATA_FILE_NAME = "data.yml";

	public File dataFile = null;
	public FileConfiguration dataFileYaml = null;

	// Player maps
	public Map<UUID, String> players = new HashMap<UUID, String>();
	public Map<String, Set<UUID>> morphs = new HashMap<String, Set<UUID>>();

	private File dataDir;

	{
		NAMES.put("MORPHS", "Morphs");
		NAMES.put("DATA", "Data");
		NAMES.put("SLIMES", "Data.Slimes");
	}

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
		SECTIONS.clear();
		for (String path : NAMES.values()) {
			ConfigurationSection section = dataFileYaml.getConfigurationSection(path);
			if (section == null) {
				SECTIONS.put(path, dataFileYaml.createSection(path));
			} else {
				SECTIONS.put(path, section);
			}
		}
	}

	public void updatePlayer(Player player, String value) {
		new EffectManager(this).clearPreviousEffects(player);
		SECTIONS.get(NAMES.get("MORPHS")).set(player.getUniqueId().toString(), value);
		players.put(player.getUniqueId(), value);
		if (!morphs.containsKey(value)) {
			morphs.put(value, new HashSet<UUID>());
		}
		morphs.get(value).add(player.getUniqueId());
		saveDataFile();
	}

	private void loadPlayers() {
		ConfigurationSection section = SECTIONS.get(NAMES.get("MORPHS"));
		for (String key : section.getKeys(false)) {
			String value = section.getString(key);
			players.put(UUID.fromString(key), value);
			if (!morphs.containsKey(key)) {
				morphs.put(value, new HashSet<UUID>());
			}
			morphs.get(value).add(UUID.fromString(key));
		}
	}

	public boolean playerInSection(Player player) {
		return SECTIONS.get(NAMES.get("MORPHS")).getKeys(false).contains(player.getUniqueId().toString());
	}

	public void updateData(Player player, String dataSection, String key, String value) {
		ConfigurationSection section = SECTIONS
				.get(NAMES.get("DATA") + "." + dataSection + "." + player.getUniqueId().toString());
		section.set(key, value);
	}

	public String getData(Player player, String dataSection, String key, String value) {
		ConfigurationSection section = SECTIONS
				.get(NAMES.get("DATA") + "." + dataSection + "." + player.getUniqueId().toString());
		return section.getString(key);
	}

}
