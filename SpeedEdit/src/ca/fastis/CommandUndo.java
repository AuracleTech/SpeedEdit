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

import com.google.common.collect.Lists;

public class CommandUndo implements CommandExecutor, TabCompleter {
	int minArg = 0, maxArg = 1;
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(!EM.hasPermission(player, "speededit.undo", true) || !EM.isArgsLength(args, minArg, maxArg, "/Undo Quantity") || (EM.isArgsLength(args, 1) && (!EM.isNumber(args[0]) || !EM.hasUndo(Integer.parseInt(args[0])))) || !EM.hasUndo(1)) return true;
			Instant before = Instant.now();
			if(EM.isArgsLength(args, 1)) Functions.undo(player, Integer.parseInt(args[0])); else Functions.undo(player, 1);
			MessageManagement.command(player, "You undo §e" + (EM.isArgsLength(args, 0) ? 1 : args[0]) + " x§7 in " + Duration.between(before, Instant.now()).toMillis() + "ms", player.getName() + " undo §e" + (EM.isArgsLength(args, 0) ? 1 : args[0]) + " x§7 in " + Duration.between(before, Instant.now()).toMillis() + "ms");
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
		List<String> returnList = Lists.newArrayList();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if (EM.isArgsLength(args, minArg, maxArg)) {
				List<String> listNumbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");
				for (String material : listNumbers) {
					if (material.startsWith(args[args.length-1].toLowerCase())) returnList.add(material);
				}
			} else
				if(args.length == maxArg+1 && args[args.length-1].toLowerCase().isEmpty())
					EM.isArgsLength(args, maxArg, "/Undo Quantity");
		}
		return returnList;
	}
}
