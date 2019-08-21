package ca.fastis;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpeededit implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg2, String[] arg3) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			MessageManagement.command(player, "Speed edit version " + SpeedEdit.plugin.getDescription().getVersion(), null);
			MessageManagement.command(player, "Made by Auracle#5220 (Discord)", null);
			MessageManagement.command(player, "Report issues or request to https://github.com/VirgilFabre/SpeedEdit", null);
			MessageManagement.command(player, "Donate to help me continue https://bit.ly/2zd4Lyj", null);
		}
		return true;
	}

}
