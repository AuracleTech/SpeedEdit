package auracle;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandLog implements CommandExecutor, TabCompleter {
	int maxArg = 0;
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(!EM.hasPermission(player, "speededit.log", true) || !EM.isArgsLength(args, maxArg, "/Log") || !EM.isCPapi()) return true;
			UserData userData = SpeedEdit.getUser(player);
			Instant before = Instant.now();
			Functions.logBlocks(player, userData.SelectedZone);
			MessageManagement.command(player, "You logged §e" + userData.SelectedZone.size() + "§7 blocks to §eCoreProtect§7 in " + Duration.between(before, Instant.now()).toMillis() + "ms", player.getName() + " logged §e" + userData.SelectedZone.size() + "§7 blocks to §eCoreProtect§7 in " + Duration.between(before, Instant.now()).toMillis() + "ms");
	
		}
		return true;
	}
	

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(args.length == maxArg+1 && args[args.length-1].toLowerCase().isEmpty())
					EM.isArgsLength(args, maxArg, "/Log");
		}
		return new ArrayList<>();
	}
}
