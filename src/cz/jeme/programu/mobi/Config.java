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

public class Config {

	// Yaml configuration variables
	private static final String CONFIG_FILE_NAME = "players.yml";
	private static final String SECTION_NAME = "Players";
	public File configFile = null;
	public FileConfiguration configFileYaml = null;
	public ConfigurationSection configFileYamlSection = null;

	// Player maps
	public Map<UUID, String> players = new HashMap<UUID, String>();
	public Map<String, Set<UUID>> morphs = new HashMap<String, Set<UUID>>();
	
	private File dataDir;

	public Config(File dataDir) {
		this.dataDir = dataDir;

		if (!dataDir.exists()) {
			dataDir.mkdir();
		}

		reloadConfig();
		loadPlayers();
	}

	public void reloadConfig() {
		refreshConfigVars();

		if (!(configFile.exists())) {

			try {
				configFile.createNewFile();

			} catch (IOException e) {
				Mobi.serverLog(Level.SEVERE, e.toString());
			}
		}

		if (configFileYamlSection == null) {
			configFileYamlSection = configFileYaml.createSection(SECTION_NAME);
		}
		saveConfigFile();

	}

	private void saveConfigFile() {

		try {
			configFileYaml.save(configFile);

		} catch (IOException e) {
			Mobi.serverLog(Level.SEVERE, e.toString());
		}
	}

	private void refreshConfigVars() {
		configFile = new File(dataDir, CONFIG_FILE_NAME);
		configFileYaml = YamlConfiguration.loadConfiguration(configFile);
		configFileYamlSection = configFileYaml.getConfigurationSection(SECTION_NAME);
	}
	
	public void updatePlayer(Player player, String value) {
		configFileYamlSection.set(player.getUniqueId().toString(), value);
		players.put(player.getUniqueId(), value);
		if (!morphs.containsKey(value)) {
			morphs.put(value, new HashSet<UUID>());
		}
		morphs.get(value).add(player.getUniqueId());
		saveConfigFile();
	}
	
	private void loadPlayers() {
		for (String key : configFileYamlSection.getKeys(false)) {
			String value = configFileYamlSection.getString(key);
			players.put(UUID.fromString(key), value);
			if (!morphs.containsKey(key)) {
				morphs.put(value, new HashSet<UUID>());
			}
			morphs.get(value).add(UUID.fromString(key));
		}
	}
	
	public boolean playerInSection(Player player) {
		return configFileYamlSection.getKeys(false).contains(player.getUniqueId().toString());
	}

}
