package ca.fastis;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CommandPaste implements CommandExecutor, TabCompleter {
	int maxArg = 0;
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(!EM.hasPermission(player, "speededit.paste", true) || !EM.isArgsLength(args, maxArg, "/Paste")) return true;
			UserData userData = SpeedEdit.getUser(player);
			Instant before = Instant.now();
			for(Entry<Vector, BlockData> entry : userData.clipboard.entrySet()) {
				player.getWorld().getBlockAt(player.getLocation().add(entry.getKey())).setBlockData(entry.getValue());
			}
			MessageManagement.command(player, "You §epasted§7 the clipboard in " + Duration.between(before, Instant.now()).toMillis() + "ms", player.getName() + " §epasted§7 the clipboard in " + Duration.between(before, Instant.now()).toMillis() + "ms");
		}
		return true;
	}
	

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(args.length == maxArg+1 && args[args.length-1].toLowerCase().isEmpty())
					EM.isArgsLength(args, maxArg, "/Paste");
		}
		return new ArrayList<>();
	}
}
