package auracle;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandOutline implements CommandExecutor, TabCompleter {
	int maxArg = 1;
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(!EM.hasPermission(player, "speededit.outline", true) || !EM.hasPositionReady() || !EM.isArgsLength(args, maxArg, "/Outline Material") || !EM.isMaterial(args[0])) return true;
			try {
				UserData userData = SpeedEdit.getUser(player);
				Instant before = Instant.now();
				List<Location> locations = Functions.getLocationsInZone("outline", userData.getPosition(1), userData.getPosition(2));
				Functions.manipulateBlocks(player, locations, Material.matchMaterial(args[0]).createBlockData(), EM);
				MessageManagement.command(player, "You used outline for §e" + locations.size() + "§7 blocks to §e" + args[0].toLowerCase() + "§7 in " + Duration.between(before, Instant.now()).toMillis() + "ms", player.getName() + " used outline for §e" + locations.size() + "§7 blocks to §e" + args[0].toLowerCase() + "§7 in " + Duration.between(before, Instant.now()).toMillis() + "ms");
				MessageManagement.command(player, "You made a §e" + args[0].toLowerCase() + "§7 outline of §e" + locations.size() + "§7 blocks in " + Duration.between(before, Instant.now()).toMillis() + "ms", player.getName() +  " made a §e" + args[0].toLowerCase() + "§7 outline of §e" + locations.size() + "§7 blocks in " + Duration.between(before, Instant.now()).toMillis() + "ms");
			} catch(Exception e) {
				EM.throwException(e);
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
		List<String> returnList = new ArrayList<String>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			Material[] materialList = Material.values();
			if (EM.isArgsLength(args, maxArg)) {
				for (Material material : materialList) {
					if (material.name().toLowerCase().startsWith(args[args.length-1].toLowerCase()) && material.isBlock()) {
						returnList.add(material.name().toLowerCase());
					}
				}
			} else
				if(args.length == maxArg+1 && args[args.length-1].toLowerCase().isEmpty())
					EM.isArgsLength(args, maxArg, "/Outline Material");
		}
		return returnList;
	}
}
