package ca.fastis;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRedo implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("speededit.use")) {
				/*if(PosToSet == 1) {
					SpeedEdit.ListPosition1.put(player, player.getLocation().add(0, -1, 0).getBlock().getLocation());
				} else {
					SpeedEdit.ListPosition2.put(player, player.getLocation().add(0, -1, 0).getBlock().getLocation());
				}

				SpeedEdit.refreshSelectionZone(player, PosToSet);*/
				return true;
			} else {
				player.sendMessage(ChatColor.RED + "No permission.");
				return true;
			}
		}
		return false;
	}

}
