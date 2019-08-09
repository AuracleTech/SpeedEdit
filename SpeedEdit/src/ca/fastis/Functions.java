package ca.fastis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.coreprotect.CoreProtectAPI;

public class Functions {
	static CoreProtectAPI CPapi = SpeedEdit.CPapi;

	public static void manipulateBlocks(Player player, Material fromMat, Material toMat, ErrorManagement EM) {
		UserData userData = SpeedEdit.getUser(player);
		List<Block> newList = new ArrayList<Block>();
		if(toMat == null) {
			for(Block block : userData.SelectedZone) if(block.getType() != Material.AIR) newList.add(block);
			toMat = fromMat;
		} else {
			for(Block block : userData.SelectedZone) if(block.getType() == fromMat) newList.add(block);
		}
		manipulateBlocks(player, newList, toMat, EM);
	}

	public static void manipulateBlocks(Player player, List<Block> blocks, Material material, ErrorManagement EM) {
		UserData userData = SpeedEdit.getUser(player);
		HashMap<Block, BlockData> memory = new HashMap<Block, BlockData>();
		if(CPapi != null) for(Block block : blocks) CPapi.logRemoval("#SE-" + player.getName(), block.getLocation(), block.getType(), block.getBlockData());
		for(Block block : blocks) {
			memory.put(block, block.getBlockData());
			block.setType(material);
		}
		if(CPapi != null) for(Block block : blocks) CPapi.logPlacement("#SE-" + player.getName(), block.getLocation(), block.getType(), block.getBlockData());
		if(userData.clearRedo) {
			userData.redo = new ArrayList<HashMap<Block, BlockData>>();
			userData.clearRedo = false;
		}
		userData.addUndo(memory);
	}

	public static void moveBlocks(Player player, Vector direction, String directionTexte, int Distance) {
		UserData userData = SpeedEdit.getUser(player);
		HashMap<Block, BlockData> memory = new HashMap<Block, BlockData>();
		if(CPapi != null) for(Block block : userData.SelectedZone) CPapi.logRemoval("#SE-" + player.getName(), block.getLocation(), block.getType(), block.getBlockData());
		for(Block block : userData.SelectedZone) {
			memory.put(block, block.getBlockData());
			block.setType(Material.AIR);
		}
		if(CPapi != null) for(Block block : userData.SelectedZone) CPapi.logPlacement("#SE-" + player.getName(), block.getLocation(), block.getType(), block.getBlockData());
		HashMap<Block, BlockData> memClone = new HashMap<Block, BlockData>(memory);
		for(Entry<Block, BlockData> entry : memClone.entrySet()) {
			Block block = entry.getKey().getLocation().add(direction).getBlock();
			if(!memory.containsKey(block)) memory.put(block, block.getBlockData());
			block.setBlockData(entry.getValue());
		}		
		if(userData.clearRedo) {
			userData.redo = new ArrayList<HashMap<Block, BlockData>>();
			userData.clearRedo = false;
		}
		userData.addUndo(memory);
	}

	public static void undo(Player player, int undoQtt) {
		for(int i = 1; i <= undoQtt; i++) {
			HashMap<Block, BlockData> memory = new HashMap<Block, BlockData>();
			HashMap<Block, BlockData> lastUndoList = SpeedEdit.getUser(player).undo.get(SpeedEdit.getUser(player).undo.size() - 1);
			if(CPapi != null) for(Entry<Block, BlockData> blockAndData : lastUndoList.entrySet()) CPapi.logRemoval("#SE-" + player.getName(), blockAndData.getKey().getLocation(), blockAndData.getKey().getType(), blockAndData.getValue());
			for(Entry<Block, BlockData> blockAndData : lastUndoList.entrySet()) {
				Block block = blockAndData.getKey();
				BlockData blockData = blockAndData.getValue();
				memory.put(block, blockData);
				block.setBlockData(blockData);
			}
			if(CPapi != null) for(Entry<Block, BlockData> blockAndData : lastUndoList.entrySet()) CPapi.logPlacement("#SE-" + player.getName(), blockAndData.getKey().getLocation(), blockAndData.getKey().getType(), blockAndData.getValue());
			SpeedEdit.getUser(player).addRedo(memory);
			SpeedEdit.getUser(player).undo.remove(SpeedEdit.getUser(player).undo.size() - 1);
		}
		SpeedEdit.getUser(player).clearRedo = true;
	}

	public static void redo(Player player, int redoQtt) {
		for(int i = 1; i <= redoQtt; i++) {
			HashMap<Block, BlockData> memory = new HashMap<Block, BlockData>();
			HashMap<Block, BlockData> lastRedoList = SpeedEdit.getUser(player).redo.get(SpeedEdit.getUser(player).redo.size() - 1);
			if(CPapi != null) for(Entry<Block, BlockData> blockAndData : lastRedoList.entrySet()) CPapi.logRemoval("#SE-" + player.getName(), blockAndData.getKey().getLocation(), blockAndData.getKey().getType(), blockAndData.getValue());
			for(Entry<Block, BlockData> blockAndData : lastRedoList.entrySet()) {
				Block block = blockAndData.getKey();
				BlockData blockData = blockAndData.getValue();
				memory.put(block, blockData);
				block.setBlockData(blockData);
			}
			if(CPapi != null) for(Entry<Block, BlockData> blockAndData : lastRedoList.entrySet()) CPapi.logPlacement("#SE-" + player.getName(), blockAndData.getKey().getLocation(), blockAndData.getKey().getType(), blockAndData.getValue());
			SpeedEdit.getUser(player).addUndo(memory);
			SpeedEdit.getUser(player).redo.remove(SpeedEdit.getUser(player).redo.size() - 1);
		}
	}

	public static void spy(Player player, String cmdName) {
		for (Player online : SpeedEdit.server.getOnlinePlayers()) {
			ErrorManagement EM = new ErrorManagement(player);
			if (online.getUniqueId() == player.getUniqueId() || online.isOp() || EM.hasPermission(online, "speededit.spy", false)) online.sendMessage(cmdName);
		}
	}

	public static String getCardinalDirection(Player player) {
		float yaw = player.getLocation().getYaw();
		if (yaw < 0) yaw += 360;
		if (yaw >= 315 || yaw < 45)
			return "SOUTH";
		else if (yaw < 135)
			return "WEST";
		else if (yaw < 225)
			return "NORTH";
		else if (yaw < 315)
			return "EAST";
		return "NORTH";
	}

	public static String getPitch(Player player) {
		float pitch = player.getLocation().getPitch();
		if (pitch >= 45)
			return "DOWN";
		else if (pitch <= -45)
			return "UP";
		return "";
	}
}