package cz.jeme.programu.mobi;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			return new ArrayList<String>(Mobi.CORRECT_ARGS.values());
		} else if (args.length == 2 && (args[0].equals("set") || args[0].equals("get") )) {
			List<String> onlinePlayers = new ArrayList<String>();
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				onlinePlayers.add(p.getName());
			}
			
			return onlinePlayers;
		} else if (args.length == 3 && (args[0].equals("set"))) {
			return new ArrayList<String>(Mobi.MORPHS.keySet());
		}
		return new ArrayList<String>();
	}
}