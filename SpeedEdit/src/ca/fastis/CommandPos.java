package ca.fastis;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPos implements CommandExecutor {
	int PosToSet;
	public CommandPos(int i) {
		this.PosToSet = i;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("speededit.use")) {
				if(SpeedEdit.ListSEPos.containsKey(player)) {
					if(PosToSet > 1)
						SpeedEdit.ListSEPos.put(player, new SEPos(player, player.getLocation(), SpeedEdit.ListSEPos.get(player).pos2));
					else
						SpeedEdit.ListSEPos.put(player, new SEPos(player, SpeedEdit.ListSEPos.get(player).pos1, player.getLocation()));
				} else {
					if(PosToSet > 1)
						SpeedEdit.ListSEPos.put(player, new SEPos(player, player.getLocation(), null));
					else
						SpeedEdit.ListSEPos.put(player, new SEPos(player, null, player.getLocation()));
				}
				player.sendMessage(ChatColor.DARK_GRAY + "SpeedEdit " + ChatColor.GREEN + "Position " + PosToSet + ChatColor.DARK_GRAY + " set");
				return true;
			}
		}
		return false;
	}

}
