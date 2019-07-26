package ca.fastis;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSet implements CommandExecutor {

	Server server = SpeedEdit.server;
	Map<Player, SEPos> ListSEPos = SpeedEdit.ListSEPos;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("speededit.set")) {
				if(ListSEPos.containsKey(player) && ListSEPos.get(player) != null && ListSEPos.get(player).pos1 != null && ListSEPos.get(player).pos2 != null) {
					try {
						Location Pos1 = ListSEPos.get(player).pos1;
						Location Pos2 = ListSEPos.get(player).pos2;
						List<Block> Selected = SpeedEdit.getBlocks(Pos1, Pos2);
						for(Block block : Selected) {
							server.broadcastMessage("BLOCK" + block.getLocation().toString());
							//block.setType(Material.BEDROCK);
						}
					} catch(Exception e) {
						server.broadcastMessage("If you read this I fucked up again ¯_(ツ)_/¯" + e.getMessage());
					}
				} else {
					player.sendMessage(ChatColor.DARK_GRAY + "You need to select " + ChatColor.DARK_RED + "2 positions" + ChatColor.DARK_GRAY + ", use right and left click with a brick on blocks.");
				}
				return true;
			}
		}
		return false;
	}

}
