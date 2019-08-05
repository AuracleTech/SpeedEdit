package ca.fastis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
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
		HashMap<Block, BlockData> memory = new HashMap<Block, BlockData>();
		for(Block block : blocks) {
			memory.put(block, block.getBlockData());
			if(CPapi != null) CPapi.logRemoval(player.getName(), block.getLocation(), block.getType(), block.getBlockData());
			block.getChunk().unload(); //TODO TO FIND UTILITY
			block.setType(material);
			block.getChunk().load();  //TODO TO FIND UTILITY
			if(CPapi != null) CPapi.logPlacement(player.getName(), block.getLocation(), block.getType(), block.getBlockData());
		}
		if(SpeedEdit.getUser(player).clearRedo) {
			SpeedEdit.getUser(player).redo = new ArrayList<HashMap<Block, BlockData>>();
			SpeedEdit.getUser(player).clearRedo = false;
		}
		SpeedEdit.getUser(player).addUndo(memory);
		spy(player, ChatColor.GREEN + "" + player.getName() + ChatColor.DARK_GRAY + " used " + cmdName + " " + ChatColor.GREEN + blocks.size() + ChatColor.DARK_GRAY + " blocks to " + ChatColor.GREEN + material.toString().toLowerCase());
	}

	public static void undo(Player player, int undoQtt) {
		for(int i = 1; i <= undoQtt; i++) {
			HashMap<Block, BlockData> memory = new HashMap<Block, BlockData>();
			HashMap<Block, BlockData> locAndBlockDatas = SpeedEdit.getUser(player).undo.get(SpeedEdit.getUser(player).undo.size() - 1);
			for(Entry<Block, BlockData> locAndBlockData : locAndBlockDatas.entrySet()) {
				memory.put(locAndBlockData.getKey(), locAndBlockData.getKey().getBlockData());
				locAndBlockData.getKey().setBlockData(locAndBlockData.getValue());
			}
			SpeedEdit.getUser(player).addRedo(memory);
			SpeedEdit.getUser(player).undo.remove(SpeedEdit.getUser(player).undo.size() - 1);
		}
		SpeedEdit.getUser(player).clearRedo = true;
		spy(player, ChatColor.GREEN + "" + player.getName() + ChatColor.DARK_GRAY + " undo " + ChatColor.GREEN + undoQtt + "x");
	}

	public static void redo(Player player, int redoQtt) {
		for(int i = 1; i <= redoQtt; i++) {
			HashMap<Block, BlockData> memory = new HashMap<Block, BlockData>();
			HashMap<Block, BlockData> locAndBlockDatas = SpeedEdit.getUser(player).redo.get(SpeedEdit.getUser(player).redo.size() - 1);
			for(Entry<Block, BlockData> locAndBlockData : locAndBlockDatas.entrySet()) {
				memory.put(locAndBlockData.getKey(), locAndBlockData.getKey().getBlockData());
				locAndBlockData.getKey().setBlockData(locAndBlockData.getValue());
			}
			SpeedEdit.getUser(player).addUndo(memory);
			SpeedEdit.getUser(player).redo.remove(SpeedEdit.getUser(player).redo.size() - 1);
		}
		spy(player, ChatColor.GREEN + "" + player.getName() + ChatColor.DARK_GRAY + " redo " + ChatColor.GREEN + redoQtt + "x");
	}

	public static void spy(Player player, String cmdName) {
		for (Player online : SpeedEdit.server.getOnlinePlayers()) {
			ErrorManagement EM = new ErrorManagement(player);
			if (online.getUniqueId() == player.getUniqueId() || online.isOp() || EM.hasPermission(online, "speededit.spy", false)) online.sendMessage(cmdName);
		}
	}
}