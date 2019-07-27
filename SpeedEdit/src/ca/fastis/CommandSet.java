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
				if(!ListSEPos.containsKey(player) || ListSEPos.get(player) == null || ListSEPos.get(player).pos1 == null || ListSEPos.get(player).pos2  == null) {
					player.sendMessage(ChatColor.DARK_GRAY + "You need to select " + ChatColor.DARK_RED + "2 positions" + ChatColor.DARK_GRAY + ", use right and left click with a brick on blocks");
					return true;
				}
				if(ListSEPos.get(player).pos1.getBlock().getWorld().getName() != ListSEPos.get(player).pos2.getBlock().getWorld().getName()) {
					player.sendMessage(ChatColor.DARK_GRAY + "Your 2 positions must be in the " + ChatColor.DARK_RED + "same world");
					return true;
				}
				if(arg3.length < 1 || arg3.length > 1) {
					player.sendMessage(ChatColor.DARK_GRAY + "Use the command like this " + ChatColor.DARK_RED + "/Set Material");
					return true;
				}
				if(Material.matchMaterial(arg3[0]) == null) {
					player.sendMessage(ChatColor.DARK_GRAY + "The material " + ChatColor.DARK_RED + arg3[0] + ChatColor.DARK_GRAY + " can't be found");
					return true;
				}

				try {
					Location Pos1 = ListSEPos.get(player).pos1;
					Location Pos2 = ListSEPos.get(player).pos2;
					List<Block> Selected = SpeedEdit.getBlocks(Pos1, Pos2);
					int Changed = 0;
					for(Block block : Selected) {
						Changed++;
						block.setType(Material.matchMaterial(arg3[0]));
					}
					player.sendMessage(ChatColor.DARK_GRAY + "You changed " + ChatColor.GREEN + Changed + ChatColor.DARK_GRAY + " blocks to " + ChatColor.GREEN + arg3[0]);
					for (Player online : server.getOnlinePlayers()) {
						if (online.isOp()) {
							online.sendMessage(ChatColor.GREEN + "" + player.getName() + ChatColor.DARK_GRAY + " changed " + ChatColor.GREEN + Changed + ChatColor.DARK_GRAY + " blocks to " + ChatColor.GREEN + arg3[0]);
						}
					}
					return true;
				} catch(Exception e) {
					player.sendMessage(ChatColor.DARK_GRAY + "Speed Edit fucked, here's the error : " + ChatColor.DARK_RED + e.getMessage());
				}
				return true;
			}
		}
		return false;
	}
}
