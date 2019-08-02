package ca.fastis;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

public class CommandReplace implements CommandExecutor, TabCompleter {
	int minArg = 1, maxArg = 2;
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(!EM.hasPermission(player, "speededit.replace") || !EM.hasPositionReady() || !EM.isArgsLength(args, minArg, maxArg, "/Replace Material Material") || !EM.isMaterial(args[0]) || (EM.isArgsLength(args, 2) && !EM.isMaterial(args[1]))) return true;
			try {
				List<Block> blocks = SpeedEdit.getUser(player).getSelectedZone();
				if(EM.isArgsLength(args, 1)) Functions.manipulateBlocks(player, "replaced", blocks, Material.matchMaterial(args[0]), null, EM);
				if(EM.isArgsLength(args, 2)) Functions.manipulateBlocks(player, "replaced", blocks, Material.matchMaterial(args[0]), Material.matchMaterial(args[1]), EM);
			} catch(Exception e) {
				EM.throwException(e);
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
			Material[] materialList = Material.values();
			if (EM.isArgsLength(args, minArg, maxArg)) {
				for (Material material : materialList) {
					if (material.name().toLowerCase().startsWith(args[args.length-1].toLowerCase()) && material.isBlock()) {
						returnList.add(material.name().toLowerCase());
					}
				}
			} else
				if(args.length == maxArg+1 && args[args.length-1].toLowerCase().isEmpty())
					EM.isArgsLength(args, minArg, maxArg, "/Replace Material Material");
		}
		return returnList;
	}
}
