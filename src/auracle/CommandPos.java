package auracle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandPos implements CommandExecutor, TabCompleter {
	int maxArg = 0;
	int PosToSet;
	public CommandPos(int i) {
		this.PosToSet = i;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(!EM.hasPermission(player, "speededit.use", true) || !EM.isArgsLength(args, maxArg, "/Pos" + PosToSet)) return true;
			SpeedEdit.getUser(player).setPosition(PosToSet, player.getLocation().add(0, -1, 0).getBlock().getLocation(), true);
		}
		return true;
	}
	

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(args.length == maxArg+1 && args[args.length-1].toLowerCase().isEmpty())
					EM.isArgsLength(args, maxArg, "/Pos" + PosToSet);
		}
		return new ArrayList<>();
	}
}
