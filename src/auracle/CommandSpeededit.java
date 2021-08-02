package auracle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandSpeededit implements CommandExecutor, TabCompleter {
	int maxArg = 0;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg2, String[] arg3) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			MessageManagement.command(player, "Speed edit version " + SpeedEdit.plugin.getDescription().getVersion(), null);
			MessageManagement.command(player, "Made by §aAuracle§7 (Discord)", null);
			MessageManagement.command(player, "Issues & more §ehttps://github.com/AuracleTech/SpeedEdit", null);
			MessageManagement.command(player, "If you want to donate §ehttps://ko-fi.com/AuracleTech", null);
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
		if (sender instanceof Player player) {
			ErrorManagement EM = new ErrorManagement(player);
			if(args.length == maxArg+1 && args[args.length-1].toLowerCase().isEmpty())
				EM.isArgsLength(args, maxArg, "/Speededit");
		}
		return new ArrayList<>();
	}
}