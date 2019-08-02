package ca.fastis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.coreprotect.CoreProtectAPI;

public class Events {
	static CoreProtectAPI cpapi = SpeedEdit.cpapi;
	static Map<Player, Map<UUID, Block>> undo = new HashMap<Player, Map<UUID, Block>>();
	Map<Player, Map<UUID, Block>> redo = new HashMap<Player, Map<UUID, Block>>();
	
	public static void manipulateBlocks(Player player, List<Block> blocks, Material fromMat, Material whereMat, Material toMat) {
		for(Block block : blocks) {
			cpapi.logRemoval(player.getName(), block.getLocation(), block.getType(), block.getBlockData());
			block.setType(toMat);
			cpapi.logPlacement(player.getName(), block.getLocation(), toMat, block.getBlockData());
		}
	}

	public static void manipulateBlocks(Player player, String cmdName, List<Block> blocks, Material material, ErrorManagement EM) { //SET BLOCKS TO
		for(Block block : blocks) {
			cpapi.logRemoval(player.getName(), block.getLocation(), block.getType(), block.getBlockData());
			block.setType(material);
			cpapi.logPlacement(player.getName(), block.getLocation(), block.getType(), block.getBlockData());
		}
		for (Player online : SpeedEdit.server.getOnlinePlayers()) {
			if (online.getUniqueId() == player.getUniqueId() || online.isOp() || EM.hasPermission(online, "speededit.chatlog")) {
				online.sendMessage(ChatColor.GREEN + "" + player.getName() + ChatColor.DARK_GRAY + " " + cmdName + " " + ChatColor.GREEN + blocks.size() + ChatColor.DARK_GRAY + " blocks to " + ChatColor.GREEN + material.toString());
			}
		}
	}
}