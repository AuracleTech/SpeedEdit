package auracle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandSphere implements CommandExecutor, TabCompleter {
	int maxArg = 2;
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		Player player;
		if (sender instanceof Player) {
			player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(!EM.hasPermission(player, "speededit.sphere", true) || !EM.isArgsLength(args, maxArg, "/Sphere Material Radius") || !EM.isMaterial(args[0]) || !EM.isNumber(args[1])) return true;
			try {
				UserData userData = SpeedEdit.getUser(player);
				Instant before = Instant.now();
				List<Location> locations = Functions.getLocationsInSphere("sphere", player.getLocation().add(0, -1, 0).getBlock().getLocation(), Integer.parseInt(args[1]));
				Functions.manipulateBlocks(player, locations, Material.matchMaterial(args[0]).createBlockData(), EM);
				MessageManagement.command(player, "You made a §e" + args[0].toLowerCase() + "§7 sphere radius of §e" + args[1] + "§7 blocks containing §e" + locations.size() + "§7 blocks in " + Duration.between(before, Instant.now()).toMillis() + "ms", player.getName() +  " made a §e" + args[0].toLowerCase() + "§7 sphere radius of §e" + args[1] + "§7 blocks containing §e" + locations.size() + "§7 blocks in " + Duration.between(before, Instant.now()).toMillis() + "ms");
			} catch(Exception e) {
				EM.throwException(e);
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
		List<String> returnList = new ArrayList<>();
		Player player;
		if (sender instanceof Player) {
			player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			Material[] materialList = Material.values();
			if (args.length == 1) {
				for (Material material : materialList) {
					if (material.name().toLowerCase().startsWith(args[args.length - 1].toLowerCase()) && material.isBlock()) {
						returnList.add(material.name().toLowerCase());
					}
				}
			} else if (args.length == 2) {
				List<String> listNumbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");
				for (String material : listNumbers) {
					if (material.startsWith(args[args.length - 1].toLowerCase())) returnList.add(material);
				}
			} else if (!EM.isArgsLength(args, maxArg))
				if(args.length == maxArg+1 && args[args.length-1].isEmpty())
					EM.isArgsLength(args, maxArg, "/Sphere Material Radius");
		}
		return returnList;
	}
}
