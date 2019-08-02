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

public class CommandSet implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(!EM.hasPermission(player, "speededit.set") || !EM.hasPositionReady() || !EM.isMaterial(arg3[0]) || !EM.isArgsCorrect(arg3, 1, "/Set Material")) return true;
			try {
				List<Block> blocks = SpeedEdit.SelectedBlocks.get(player);
				Events.manipulateBlocks(player, "set", blocks, Material.matchMaterial(arg3[0]), EM);
			} catch(Exception e) {
				EM.throwException(e);
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
		Material[] list = Material.values();
		List<String> fList = Lists.newArrayList();
		if (args.length == 1) {
			for (Material s : list) {
				if (s.name().toLowerCase().startsWith(args[0].toLowerCase())) {
					fList.add(s.name().toLowerCase());
				}
			}
			return fList;
		} else {
			return null;
		}
	}
}
