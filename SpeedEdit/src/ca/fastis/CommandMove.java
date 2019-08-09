package ca.fastis;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;

public class CommandMove implements CommandExecutor, TabCompleter {
	int maxArg = 1;
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(!EM.hasPermission(player, "speededit.move", true) || !EM.hasPositionReady() || !EM.isArgsLength(args, maxArg, "/Move Distance") || !EM.isNumber(args[0])) return true;
			int distance = Integer.parseInt(args[0]);
			String direction = Functions.getPitch(player);
			UserData userData = SpeedEdit.getUser(player);
			Instant before = Instant.now();
			switch(direction) {
			case "UP":
				Functions.moveBlocks(player, new Vector(0, distance, 0), "up", distance);
				MessageManagement.command(player, "You moved §e" + userData.SelectedZone.size() + "§7 blocks §eup§f in " + Duration.between(before, Instant.now()).toMillis() + "ms", player.getName() + " moved §e" + userData.SelectedZone.size() + "§7 blocks §eup§f in " + Duration.between(before, Instant.now()).toMillis() + "ms");
				return true;
			case "DOWN":
				Functions.moveBlocks(player, new Vector(0, 0-distance, 0), "down", distance);
				MessageManagement.command(player, "You moved §e" + userData.SelectedZone.size() + "§7 blocks §edown§f in " + Duration.between(before, Instant.now()).toMillis() + "ms", player.getName() + " moved §e" + userData.SelectedZone.size() + "§7 blocks §edown§f in " + Duration.between(before, Instant.now()).toMillis() + "ms");
				return true;
			}
			direction = Functions.getCardinalDirection(player);
			switch(Functions.getCardinalDirection(player)) {
			case "SOUTH":
				Functions.moveBlocks(player, new Vector(0, 0, distance), "south", distance);
				MessageManagement.command(player, "You moved §e" + userData.SelectedZone.size() + "§7 blocks §esouth§f in " + Duration.between(before, Instant.now()).toMillis() + "ms", player.getName() + " moved §e" + userData.SelectedZone.size() + "§7 blocks §esouth§f in " + Duration.between(before, Instant.now()).toMillis() + "ms");
				return true;
			case "NORTH":
				Functions.moveBlocks(player, new Vector(0, 0, 0-distance), "north", distance);
				MessageManagement.command(player, "You moved §e" + userData.SelectedZone.size() + "§7 blocks §enorth§f in " + Duration.between(before, Instant.now()).toMillis() + "ms", player.getName() + " moved §e" + userData.SelectedZone.size() + "§7 blocks §enorth§f in " + Duration.between(before, Instant.now()).toMillis() + "ms");
				return true;
			case "EAST":
				Functions.moveBlocks(player, new Vector(distance, 0, 0), "east", distance);
				MessageManagement.command(player, "You moved §e" + userData.SelectedZone.size() + "§7 blocks §eeast§f in " + Duration.between(before, Instant.now()).toMillis() + "ms", player.getName() + " moved §e" + userData.SelectedZone.size() + "§7 blocks §eeast§f in " + Duration.between(before, Instant.now()).toMillis() + "ms");
				return true;
			case "WEST":
				Functions.moveBlocks(player, new Vector(0-distance, 0, 0), "west", distance);
				MessageManagement.command(player, "You moved §e" + userData.SelectedZone.size() + "§7 blocks §ewest§f in " + Duration.between(before, Instant.now()).toMillis() + "ms", player.getName() + " moved §e" + userData.SelectedZone.size() + "§7 blocks §ewest§f in " + Duration.between(before, Instant.now()).toMillis() + "ms");
				return true;
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
		List<String> returnList = Lists.newArrayList();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if (EM.isArgsLength(args, maxArg)) {
				List<String> listNumbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");
				for (String material : listNumbers) {
					if (material.startsWith(args[args.length-1].toLowerCase())) returnList.add(material);
				}
			} else
				if(args.length == maxArg+1 && args[args.length-1].toLowerCase().isEmpty())
					EM.isArgsLength(args, maxArg, "/Move Distance");
		}
		return returnList;
	}
}
