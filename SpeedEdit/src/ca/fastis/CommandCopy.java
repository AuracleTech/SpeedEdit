package ca.fastis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CommandCopy implements CommandExecutor, TabCompleter {
	int maxArg = 0;
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(!EM.hasPermission(player, "speededit.copy", true) || !EM.isArgsLength(args, maxArg, "/Copy")) return true;
			UserData userData = SpeedEdit.getUser(player);
			List<Location> locations = new ArrayList<Location>();
			locations = Functions.getLocationsInZone("", userData.getPosition(1), userData.getPosition(2));
			HashMap<Vector, BlockData> clipboard = new HashMap<Vector, BlockData>();
			double x = player.getLocation().getBlock().getLocation().getX();
			double y = player.getLocation().getBlock().getLocation().getY();
			double z = player.getLocation().getBlock().getLocation().getZ();
			for(Location location : locations) {
				clipboard.put(new Vector(location.getX() - x, location.getY() - y, location.getZ() - z), location.getBlock().getBlockData());
			}
			userData.clipboard = clipboard;
			userData.copyLocation = player.getLocation().add(0, 1, 0).getBlock().getLocation();
			MessageManagement.command(player, "Selected zone §ecopied§7 - §e" + clipboard.size() + "§7 blocks", null);
		}
		return true;
	}
	

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(args.length == maxArg+1 && args[args.length-1].toLowerCase().isEmpty())
					EM.isArgsLength(args, maxArg, "/Copy");
		}
		return new ArrayList<>();
	}
}
