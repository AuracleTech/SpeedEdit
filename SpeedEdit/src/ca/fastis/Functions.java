package ca.fastis;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.coreprotect.CoreProtectAPI;

public class Functions {
	static CoreProtectAPI CPapi = SpeedEdit.CPapi;

	public static void manipulateBlocks(Player player, String cmdName, List<Block> blocks, Material fromMat, Material toMat, ErrorManagement EM) {
		List<Block> newList = new ArrayList<Block>();
		if(toMat == null) {
			for(Block block : blocks) {
				if(block.getType() != Material.AIR) newList.add(block);
			}
			toMat = fromMat;
		} else {
			for(Block block : blocks) {
				if(block.getType() == fromMat) newList.add(block);
			}
		}
		manipulateBlocks(player, cmdName, newList, toMat, EM);
	}

	public static void manipulateBlocks(Player player, String cmdName, List<Block> blocks, Material material, ErrorManagement EM) {
		if(CPapi != null) {
			for(Block block : blocks) {
				CPapi.logRemoval(player.getName(), block.getLocation(), block.getType(), block.getBlockData());
				block.setType(material);
				CPapi.logPlacement(player.getName(), block.getLocation(), block.getType(), block.getBlockData());
			}
		} else {
			for(Block block : blocks) block.setType(material);
		}
		for (Player online : SpeedEdit.server.getOnlinePlayers()) {
			if (online.getUniqueId() == player.getUniqueId() || online.isOp() || EM.hasPermission(online, "speededit.spy")) {
				online.sendMessage(ChatColor.GREEN + "" + player.getName() + ChatColor.DARK_GRAY + " " + cmdName + " " + ChatColor.GREEN + blocks.size() + ChatColor.DARK_GRAY + " blocks to " + ChatColor.GREEN + material.toString().toLowerCase());
			}
		}
	}
}