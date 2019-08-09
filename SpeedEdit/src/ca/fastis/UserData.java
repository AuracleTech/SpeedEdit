package ca.fastis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class UserData {
	Player player = null;
	Map<Integer, Location> positions = new HashMap<Integer, Location>();
	List<Block> SelectedZone = null;
	List<Block> Highlight = null;
	public boolean clearRedo = false;
	List<HashMap<Block, BlockData>> undo = new ArrayList<HashMap<Block, BlockData>>();
	List<HashMap<Block, BlockData>> redo = new ArrayList<HashMap<Block, BlockData>>();
	List<HashMap<Block, BlockData>> clipboard = new ArrayList<HashMap<Block, BlockData>>();

	public UserData(Player player){
		this.player = player;
	}

	public void setPosition(int position, Location argLocation, boolean showMessage) {
		if(argLocation.getY() > 255) argLocation = new Location(argLocation.getWorld(), argLocation.getX(), 255, argLocation.getZ());
		if(argLocation.getY() < 0) argLocation = new Location(argLocation.getWorld(), argLocation.getX(), 0, argLocation.getZ());
		positions.put(position, argLocation);
		if(isBothPosSet() && isBothPosSameWorld()) {
			SelectedZone = getSelectedZone(positions.get(1), positions.get(2));
			Highlight = setPattern("skeleton", SelectedZone);
		}
		if(showMessage) MessageManagement.command(player, "Position " + position + " set" + ((isBothPosSet() && isBothPosSameWorld()) ? " §a" + SelectedZone.size() + " Blocks" : ""), null);
	}

	public Location getPosition(int position) {
		return positions.get(position);
	}

	private List<Block> getSelectedZone(Location pos1, Location pos2) {
		List<Block> blocks = new ArrayList<Block>();
		int topBlockX = (pos1.getBlockX() < pos2.getBlockX() ? pos2.getBlockX() : pos1.getBlockX());
		int bottomBlockX = (pos1.getBlockX() > pos2.getBlockX() ? pos2.getBlockX() : pos1.getBlockX());
		int topBlockY = (pos1.getBlockY() < pos2.getBlockY() ? pos2.getBlockY() : pos1.getBlockY());
		int bottomBlockY = (pos1.getBlockY() > pos2.getBlockY() ? pos2.getBlockY() : pos1.getBlockY());
		int topBlockZ = (pos1.getBlockZ() < pos2.getBlockZ() ? pos2.getBlockZ() : pos1.getBlockZ());
		int bottomBlockZ = (pos1.getBlockZ() > pos2.getBlockZ() ? pos2.getBlockZ() : pos1.getBlockZ());
		for(int x = bottomBlockX; x <= topBlockX; x++) {
			for(int z = bottomBlockZ; z <= topBlockZ; z++) {
				for(int y = bottomBlockY; y <= topBlockY; y++) {
					blocks.add(pos1.getWorld().getBlockAt(x, y, z));
				}
			}
		}
		return blocks;
	}

	public List<Block> setPattern(String pattern, List<Block> blocks) {
		Location pos1 = SpeedEdit.getUser(player).positions.get(1);
		Location pos2 = SpeedEdit.getUser(player).positions.get(2);
		List<Block> blockPattern = new ArrayList<Block>();
		if(pattern.equals("walls")) {
			for(Block block : blocks) if(block.getLocation().getX() == pos1.getX() || block.getLocation().getZ() == pos1.getZ() || block.getLocation().getX() == pos2.getX() || block.getLocation().getZ() == pos2.getZ()) blockPattern.add(block);
		} else if(pattern.equals("outline")) {
			for(Block block : blocks) if(block.getLocation().getX() == pos1.getX() || block.getLocation().getZ() == pos1.getZ() || block.getLocation().getY() == pos1.getY() || block.getLocation().getX() == pos2.getX() || block.getLocation().getZ() == pos2.getZ() || block.getLocation().getY() == pos2.getY() ) blockPattern.add(block);
		} else if(pattern.equals("skeleton")) {
			for(Block block : blocks) if((block.getLocation().getX() == pos1.getX() && block.getLocation().getY() == pos1.getY()) || (block.getLocation().getY() == pos1.getY() && block.getLocation().getZ() == pos1.getZ()) || (block.getLocation().getX() == pos1.getX() && block.getLocation().getZ() == pos1.getZ()) || (block.getLocation().getX() == pos2.getX() && block.getLocation().getY() == pos2.getY()) || (block.getLocation().getY() == pos2.getY() && block.getLocation().getZ() == pos2.getZ()) || (block.getLocation().getX() == pos2.getX() && block.getLocation().getZ() == pos2.getZ()) || (block.getLocation().getX() == pos2.getX() && block.getLocation().getY() == pos1.getY()) || (block.getLocation().getY() == pos2.getY() && block.getLocation().getZ() == pos1.getZ()) || (block.getLocation().getX() == pos2.getX() && block.getLocation().getZ() == pos1.getZ()) || (block.getLocation().getX() == pos1.getX() && block.getLocation().getY() == pos2.getY()) || (block.getLocation().getY() == pos1.getY() && block.getLocation().getZ() == pos2.getZ()) || (block.getLocation().getX() == pos1.getX() && block.getLocation().getZ() == pos2.getZ())) blockPattern.add(block);
		}
		return blockPattern;
	}

	public boolean isBothPosSet(){
		if(positions.containsKey(1) && positions.containsKey(2))
			return true;
		else
			return false;
	}

	public boolean isBothPosSameWorld(){
		if(positions.get(1).getWorld() == positions.get(2).getWorld())
			return true;
		else
			return false;
	}

	public void addUndo(HashMap<Block, BlockData> data) {
		undo.add(data);
	}

	public void addRedo(HashMap<Block, BlockData> data) {
		redo.add(data);
	}
}
