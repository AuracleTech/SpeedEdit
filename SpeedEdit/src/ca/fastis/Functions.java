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

	public static int manipulateBlocks(Player player, Material fromMat, Material toMat, ErrorManagement EM) {
		UserData userData = SpeedEdit.getUser(player);
		List<Block> newList = new ArrayList<Block>();
		if(toMat == null) {
			for(Block block : userData.SelectedZone) if(block.getType() != Material.AIR) newList.add(block);
			toMat = fromMat;
		} else {
			for(Block block : userData.SelectedZone) if(block.getType() == fromMat) newList.add(block);
		}
		manipulateBlocks(player, newList, toMat, EM);
		return newList.size();
	}

	public static void manipulateBlocks(Player player, List<Block> blocks, Material material, ErrorManagement EM) {
		UserData userData = SpeedEdit.getUser(player);
		HashMap<Block, BlockData> memory = new HashMap<Block, BlockData>();
		if(CPapi != null) for(Block block : blocks) CPapi.logRemoval("#SE" + player.getName(), block.getLocation(), block.getType(), block.getBlockData());
		for(Block block : blocks) {
			memory.put(block, block.getBlockData());
			block.setType(material);
		}
		if(CPapi != null) for(Block block : blocks) CPapi.logPlacement("#SE" + player.getName(), block.getLocation(), block.getType(), block.getBlockData());
		if(userData.clearRedo) {
			userData.redo = new ArrayList<HashMap<Block, BlockData>>();
			userData.clearRedo = false;
		}
		userData.addUndo(memory);
	}

	public static void moveBlocks(Player player, Vector direction, String directionTexte, int Distance) {
		UserData userData = SpeedEdit.getUser(player);
		HashMap<Block, BlockData> memory = new HashMap<Block, BlockData>();
		if(CPapi != null) for(Block block : userData.SelectedZone) CPapi.logRemoval("#SE" + player.getName(), block.getLocation(), block.getType(), block.getBlockData());
		for(Block block : userData.SelectedZone) {
			memory.put(block, block.getBlockData());
			block.setType(Material.AIR);
		}
		if(CPapi != null) for(Block block : userData.SelectedZone) CPapi.logPlacement("#SE" + player.getName(), block.getLocation(), block.getType(), block.getBlockData());
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
		UserData userData = SpeedEdit.getUser(player);
		for(int i = 1; i <= undoQtt; i++) {
			HashMap<Block, BlockData> memory = new HashMap<Block, BlockData>();
			HashMap<Block, BlockData> locAndBlockDatas = userData.undo.get(userData.undo.size() - 1);
			for(Entry<Block, BlockData> locAndBlockData : locAndBlockDatas.entrySet()) {
				memory.put(locAndBlockData.getKey(), locAndBlockData.getKey().getBlockData());
				locAndBlockData.getKey().setBlockData(locAndBlockData.getValue());
			}
			userData.addRedo(memory);
			userData.undo.remove(userData.undo.size() - 1);
		}
		userData.clearRedo = true;
	}

	public static void redo(Player player, int redoQtt) {
		UserData userData = SpeedEdit.getUser(player);
		for(int i = 1; i <= redoQtt; i++) {
			HashMap<Block, BlockData> memory = new HashMap<Block, BlockData>();
			HashMap<Block, BlockData> locAndBlockDatas = userData.redo.get(userData.redo.size() - 1);
			for(Entry<Block, BlockData> locAndBlockData : locAndBlockDatas.entrySet()) {
				memory.put(locAndBlockData.getKey(), locAndBlockData.getKey().getBlockData());
				locAndBlockData.getKey().setBlockData(locAndBlockData.getValue());
			}
			userData.addUndo(memory);
			userData.redo.remove(userData.redo.size() - 1);
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