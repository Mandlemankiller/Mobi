package cz.jeme.programu.mobi;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import cz.jeme.programu.mobi.morphs.Morph;
import cz.jeme.programu.mobi.schedulers.BurnManager;
import cz.jeme.programu.mobi.schedulers.EffectManager;
import net.md_5.bungee.api.ChatColor;

public class Mobi extends JavaPlugin {

	// Map of all valid arguments
	public static final Map<String, String> CORRECT_ARGS = new HashMap<String, String>();

	// Set of all valid morphs
	public static final Map<String, Morph> MORPHS = new HashMap<String, Morph>();

	// Preifx
	public static final String PREFIX = ChatColor.DARK_GRAY.toString() + "[" + ChatColor.GOLD.toString()
			+ ChatColor.BOLD.toString() + "ᴍᴏʙɪ" + ChatColor.DARK_GRAY.toString() + "]: ";

	// Objects
	private MobiEventHandler eventHandler = null;
	private MobiData mobiData = null;

	// Fill the maps
	{
		CORRECT_ARGS.put("set", "set");
		CORRECT_ARGS.put("get", "get");
		CORRECT_ARGS.put("reload", "reload");
		CORRECT_ARGS.put("help", "help");

		MORPHS.put("zombie", null);
		MORPHS.put("human", null);
		MORPHS.put("skeleton", null);
		MORPHS.put("ghast", null);
		MORPHS.put("bat", null);
		MORPHS.put("slime", null);
		MORPHS.put("magma_cube", null);
	}

	@Override
	public void onEnable() {
		getCommand("mobi").setTabCompleter(new CommandTabCompleter());

		mobiData = new MobiData(getDataFolder());
		eventHandler = new MobiEventHandler(mobiData);
		
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(eventHandler, this);

		new BurnManager(mobiData).runTaskTimer(this, 0L, 20L);
		new EffectManager(mobiData).runTaskTimer(this, 0L, 20L);
		
		
		serverLog(Level.INFO, "Started morphs reflection");
		for (String morph : MORPHS.keySet()) {
			try {
				MORPHS.put(morph, reflection(morph));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				serverLog(Level.SEVERE, e.toString());
			}
		}
		serverLog(Level.INFO, "Morphs reflection success");

	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("mobi")) { // When command called
			if (args.length >= 1) {
				if (args[0].equals(CORRECT_ARGS.get("set"))) { // Get and set
					if (args.length < 3) { // Not enough arguments
						sender.sendMessage(PREFIX + ChatColor.RED.toString() + "Not enough arguments!");
						return true;
					} else if (args.length > 3) { // Too many arguments
						sender.sendMessage(PREFIX + ChatColor.RED.toString() + "Too many arguments!");
						return true;
					} else if (Bukkit.getPlayer(args[1]) == null) { // Arguments ok, check if player valid
						sender.sendMessage(PREFIX + ChatColor.RED.toString() + "That player is not online!");
						return true;
					} else { // Everything is alright
						String playerName = Bukkit.getPlayer(args[1]).getName();
						if (MORPHS.containsKey(args[2])) {
							set(args[1], args[2]);
							sender.sendMessage(PREFIX + ChatColor.GREEN.toString() + "The new " + playerName
									+ "'s morph is " + args[2] + ".");
							return true;
						} else {
							sender.sendMessage(PREFIX + ChatColor.RED.toString() + "That is not a valid morph!");
							return true;
						}
					}
				} else if (args[0].equals(CORRECT_ARGS.get("get"))) {
					if (args.length < 2) { // Not enough arguments
						sender.sendMessage(PREFIX + ChatColor.RED.toString() + "Not enough arguments!");
						return true;
					} else if (args.length > 2) { // Too many arguments
						sender.sendMessage(PREFIX + ChatColor.RED.toString() + "Too many arguments!");
						return true;
					} else if (Bukkit.getPlayer(args[1]) == null) { // Arguments ok, check if player valid
						sender.sendMessage(PREFIX + ChatColor.RED.toString() + "That player is not online!");
						return true;
					} else { // Everything is alright
						String playerName = Bukkit.getPlayer(args[1]).getName();
						sender.sendMessage(PREFIX + ChatColor.GREEN.toString() + playerName + "'s current morph is "
								+ get(args[1]));
						return true;
					}
				} else if (args[0].equals(CORRECT_ARGS.get("reload"))) {
					mobiData.reloadData();
					sender.sendMessage(PREFIX + ChatColor.GREEN.toString() + "MobiData reloaded!");
					return true;
				} else if (args[0].equals(CORRECT_ARGS.get("help"))) {
					return false;
				}
			}
		}
		sender.sendMessage(PREFIX + ChatColor.RED.toString() + "Unknown command!");
		return true;

	}

	private void set(String playerName, String key) {
		Player player = Bukkit.getPlayer(playerName);
		mobiData.updatePlayer(player, key);
		eventHandler.playerMorphed(player);
	}
	

	private String get(String playerName) {
		UUID uuid = Bukkit.getPlayer(playerName).getUniqueId();
		return mobiData.players.get(uuid);
	}

	public static void serverLog(Level lvl, String msg) {
		if (msg == null) {
			msg = "null";
		}
		Bukkit.getServer().getLogger().log(lvl, msg);
	}

	private Morph reflection(String morphName)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		List<String> nameSepa = Arrays.asList(morphName.split("_"));
		String className = "";
		for (String item : nameSepa) {
			className = className + item.substring(0, 1).toUpperCase() + item.substring(1);
		}
		Class<?> morphClass = Class.forName(this.getClass().getPackageName() + ".morphs." + className);
		Object morphObject = morphClass.getDeclaredConstructor().newInstance();
		Morph morph = (Morph) morphObject;
		morph.setMobiData(mobiData);
		return morph;
	}
}