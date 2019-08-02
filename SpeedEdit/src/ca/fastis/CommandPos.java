package ca.fastis;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandPos implements CommandExecutor, TabCompleter {
	int maxArg = 1;
	int PosToSet;
	public CommandPos(int i) {
		this.PosToSet = i;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(!EM.hasPermission(player, "speededit.use") || !EM.isArgsCorrect(args, maxArg, "/Pos" + PosToSet)) return true;
			
		}
		
		if (sender instanceof Player) {//TODO : REMOVE AND FIX CODE BELOW :: COPY PASTE TO EDIT IN URGENCE
			Player player = (Player) sender;
			if(player.hasPermission("speededit.use")) {
				if(PosToSet == 1) {
					SpeedEdit.ListPosition1.put(player, player.getLocation().add(0, -1, 0).getBlock().getLocation());
				} else {
					SpeedEdit.ListPosition2.put(player, player.getLocation().add(0, -1, 0).getBlock().getLocation());
				}

				SpeedEdit.refreshSelectionZone(player, PosToSet);
				return true;
			} else {
				player.sendMessage(ChatColor.RED + "No permission.");
				return true;
			}
		}
		return false;
	}
	

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) { //TODO: TEST
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(args.length == maxArg+1 && args[args.length-1].toLowerCase().isEmpty())
					EM.isArgsCorrect(args, maxArg, "/Pos" + PosToSet);
		}
		return new ArrayList<>();
	}
}
