package ca.fastis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.coreprotect.CoreProtectAPI;

public class Functions {
	static CoreProtectAPI CPapi = SpeedEdit.CPapi;
	//if(CPapi != null) for(Location location : locations) CPapi.logRemoval("#SE" + player.getName(), location, location.getBlock().getType(), location.getBlock().getBlockData());
	public static int manipulateBlocks(Player player, Material fromMat, Material toMat, ErrorManagement EM) {
		UserData userData = SpeedEdit.getUser(player);
		List<Location> newList = new ArrayList<Location>();
		if(toMat == null) {
			for(Location location : userData.SelectedZone) if(location.getBlock().getType() != Material.AIR) newList.add(location);
			toMat = fromMat;
		} else {
			for(Location location : userData.SelectedZone) if(location.getBlock().getType() == fromMat) newList.add(location);
		}
		manipulateBlocks(player, newList, toMat, EM);
		return newList.size();
	}

	public static void manipulateBlocks(Player player, List<Location> locations, Material material, ErrorManagement EM) {
		UserData userData = SpeedEdit.getUser(player);
		HashMap<Location, BlockData> memory = new HashMap<Location, BlockData>();
		for(Location location : locations) {
			memory.put(location, location.getBlock().getBlockData());
			location.getBlock().setType(material);
		}
		userData.clearRedo = true;
		userData.addUndo(memory);
	}

	public static void pasteBlocks(Player player, UserData userData) {
		HashMap<Location, BlockData> memory = new HashMap<Location, BlockData>();
		for(Entry<Vector, BlockData> entry : userData.clipboard.entrySet()) {
			Block block = player.getWorld().getBlockAt(player.getLocation().add(entry.getKey()));
			memory.put(block.getLocation(), block.getBlockData());
			block.setBlockData(entry.getValue());
		}
		userData.clearRedo = true;
		userData.addUndo(memory);
	}

	public static void moveBlocks(Player player, Vector vector, String directionTexte, int Distance) {
		UserData userData = SpeedEdit.getUser(player);
		HashMap<Location, BlockData> memory = new HashMap<Location, BlockData>(); 
		for(Location location : userData.SelectedZone) {
			memory.put(location, location.getBlock().getBlockData());
			location.getBlock().setType(Material.AIR);
		}
		HashMap<Location, BlockData> memoryClone = new HashMap<Location, BlockData>(memory); 
		for(Entry<Location, BlockData> entry : memoryClone.entrySet()) {
			if(!memory.containsKey(entry.getKey().clone().add(vector).getBlock().getLocation())) memory.put(entry.getKey().clone().add(vector).getBlock().getLocation(), entry.getKey().clone().add(vector).getBlock().getBlockData());
			entry.getKey().clone().add(vector).getBlock().setBlockData(entry.getValue());
		}
		userData.clearRedo = true;
		userData.addUndo(memory);
	}

	public static void undo(Player player, int undoQtt) {
		UserData userData = SpeedEdit.getUser(player);
		for(int i = 1; i <= undoQtt; i++) {
			HashMap<Location, BlockData> memory = new HashMap<Location, BlockData>();
			HashMap<Location, BlockData> locAndBlockDatas = userData.undo.get(userData.undo.size() - 1);
			for(Entry<Location, BlockData> locBlockData : locAndBlockDatas.entrySet()) {
				memory.put(locBlockData.getKey(), locBlockData.getKey().getBlock().getBlockData());
				locBlockData.getKey().getBlock().setBlockData(locBlockData.getValue());
			}
			userData.addRedo(memory);
			userData.undo.remove(userData.undo.size() - 1);
		}
	}

	public static void redo(Player player, int redoQtt) {
		UserData userData = SpeedEdit.getUser(player);
		for(int i = 1; i <= redoQtt; i++) {
			HashMap<Location, BlockData> memory = new HashMap<Location, BlockData>();
			HashMap<Location, BlockData> locAndBlockDatas = userData.redo.get(userData.redo.size() - 1);
			for(Entry<Location, BlockData> locBlockData : locAndBlockDatas.entrySet()) {
				memory.put(locBlockData.getKey(), locBlockData.getKey().getBlock().getBlockData());
				locBlockData.getKey().getBlock().setBlockData(locBlockData.getValue());
			}
			userData.addUndo(memory);
			userData.redo.remove(userData.redo.size() - 1);
		}
	}

	static List<Block> getBlocksInZone(List<Location> locations) {
		List<Block> blocks = new ArrayList<Block>();
		for(Location location : locations) blocks.add(location.getBlock());
		return blocks;
	}

	static List<Location> getLocationsInZone(String pattern, Location pos1, Location pos2) {
		World world = pos1.getWorld();
		List<Location> locations = new ArrayList<Location>();
		int topBlockX = (pos1.getBlockX() < pos2.getBlockX() ? pos2.getBlockX() : pos1.getBlockX());
		int bottomBlockX = (pos1.getBlockX() > pos2.getBlockX() ? pos2.getBlockX() : pos1.getBlockX());
		int topBlockY = (pos1.getBlockY() < pos2.getBlockY() ? pos2.getBlockY() : pos1.getBlockY());
		int bottomBlockY = (pos1.getBlockY() > pos2.getBlockY() ? pos2.getBlockY() : pos1.getBlockY());
		int topBlockZ = (pos1.getBlockZ() < pos2.getBlockZ() ? pos2.getBlockZ() : pos1.getBlockZ());
		int bottomBlockZ = (pos1.getBlockZ() > pos2.getBlockZ() ? pos2.getBlockZ() : pos1.getBlockZ());
		if(pattern.equals("walls")) {
			for(int x = bottomBlockX; x <= topBlockX; x++)
				for(int z = bottomBlockZ; z <= topBlockZ; z++)
					for(int y = bottomBlockY; y <= topBlockY; y++)
						if(x == pos1.getX() || z == pos1.getZ() || x == pos2.getX() || z == pos2.getZ()) locations.add(new Location(world, x, y, z));
		} else if(pattern.equals("outline")) {
			for(int x = bottomBlockX; x <= topBlockX; x++)
				for(int z = bottomBlockZ; z <= topBlockZ; z++)
					for(int y = bottomBlockY; y <= topBlockY; y++)
						if(x == pos1.getX() || z == pos1.getZ() || y == pos1.getY() || x == pos2.getX() || z == pos2.getZ() || y == pos2.getY() ) locations.add(new Location(world, x, y, z));
		} else if(pattern.equals("skeleton")) {
			for(int x = bottomBlockX; x <= topBlockX; x++)
				for(int z = bottomBlockZ; z <= topBlockZ; z++)
					for(int y = bottomBlockY; y <= topBlockY; y++)
						if((x == pos1.getX() && y == pos1.getY()) || (y == pos1.getY() && z == pos1.getZ()) || (x == pos1.getX() && z == pos1.getZ()) || (x == pos2.getX() && y == pos2.getY()) || (y == pos2.getY() && z == pos2.getZ()) || (x == pos2.getX() && z == pos2.getZ()) || (x == pos2.getX() && y == pos1.getY()) || (y == pos2.getY() && z == pos1.getZ()) || (x == pos2.getX() && z == pos1.getZ()) || (x == pos1.getX() && y == pos2.getY()) || (y == pos1.getY() && z == pos2.getZ()) || (x == pos1.getX() && z == pos2.getZ())) locations.add(new Location(world, x, y, z));
		} else
			for(int x = bottomBlockX; x <= topBlockX; x++)
				for(int z = bottomBlockZ; z <= topBlockZ; z++)
					for(int y = bottomBlockY; y <= topBlockY; y++)
						locations.add(new Location(world, x, y, z));
		return locations;
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

	public static int zoneCount(Location pos1, Location pos2) {
		int topBlockX = (pos1.getBlockX() < pos2.getBlockX() ? pos2.getBlockX() : pos1.getBlockX());
		int bottomBlockX = (pos1.getBlockX() > pos2.getBlockX() ? pos2.getBlockX() : pos1.getBlockX());
		int topBlockY = (pos1.getBlockY() < pos2.getBlockY() ? pos2.getBlockY() : pos1.getBlockY());
		int bottomBlockY = (pos1.getBlockY() > pos2.getBlockY() ? pos2.getBlockY() : pos1.getBlockY());
		int topBlockZ = (pos1.getBlockZ() < pos2.getBlockZ() ? pos2.getBlockZ() : pos1.getBlockZ());
		int bottomBlockZ = (pos1.getBlockZ() > pos2.getBlockZ() ? pos2.getBlockZ() : pos1.getBlockZ());
		return ((1 + topBlockX - bottomBlockX) * (1 + topBlockY - bottomBlockY) * (1 + topBlockZ - bottomBlockZ));
	}
}