package ca.fastis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class UserData {
	Player player = null;
	Map<Integer, Location> positions = new HashMap<Integer, Location>();
	List<Block> getSelectedZone = null;
	static List<Block> Highlight = null;
	Map<Player, Map<UUID, Block>> undo = new LinkedHashMap<Player, Map<UUID, Block>>();
	Map<Player, Map<UUID, Block>> redo = new LinkedHashMap<Player, Map<UUID, Block>>();

	public UserData(Player player){
		this.player = player;
	}

	public void setPosition(int position, Location argLocation) {
		positions.put(position, argLocation);
		FuturEvents.onPositionChangeEvent(this.player, position, argLocation);
		if(isBothPosSet() && isBothPosSameWorld()) refreshSelection();
	}

	public Location getPosition(int position) {
		return positions.get(position);
	}

	public List<Block> getSelectedZone(){
		return Highlight;
	}

	public static List<Block> getHighlightZone(){
		return Highlight;
	}

	private void refreshSelection() {
		Location pos1 = positions.get(1);
		Location pos2 = positions.get(2);
		List<Block> hightlight = new ArrayList<Block>();
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
					Block block = pos1.getWorld().getBlockAt(x, y, z);
					if(block.getLocation().getX() == pos1.getX() && block.getLocation().getY() == pos1.getY()) hightlight.add(block);
					if(block.getLocation().getY() == pos1.getY() && block.getLocation().getZ() == pos1.getZ()) hightlight.add(block);
					if(block.getLocation().getX() == pos1.getX() && block.getLocation().getZ() == pos1.getZ()) hightlight.add(block);

					if(block.getLocation().getX() == pos2.getX() && block.getLocation().getY() == pos2.getY()) hightlight.add(block);
					if(block.getLocation().getY() == pos2.getY() && block.getLocation().getZ() == pos2.getZ()) hightlight.add(block);
					if(block.getLocation().getX() == pos2.getX() && block.getLocation().getZ() == pos2.getZ()) hightlight.add(block);


					if(block.getLocation().getX() == pos2.getX() && block.getLocation().getY() == pos1.getY()) hightlight.add(block);
					if(block.getLocation().getY() == pos2.getY() && block.getLocation().getZ() == pos1.getZ()) hightlight.add(block);
					if(block.getLocation().getX() == pos2.getX() && block.getLocation().getZ() == pos1.getZ()) hightlight.add(block);

					if(block.getLocation().getX() == pos2.getX() && block.getLocation().getY() == pos1.getY()) hightlight.add(block);
					if(block.getLocation().getY() == pos2.getY() && block.getLocation().getZ() == pos1.getZ()) hightlight.add(block);
					if(block.getLocation().getX() == pos2.getX() && block.getLocation().getZ() == pos1.getZ()) hightlight.add(block);

					if(block.getLocation().getX() == pos1.getX() && block.getLocation().getY() == pos2.getY()) hightlight.add(block);
					if(block.getLocation().getY() == pos1.getY() && block.getLocation().getZ() == pos2.getZ()) hightlight.add(block);
					if(block.getLocation().getX() == pos1.getX() && block.getLocation().getZ() == pos2.getZ()) hightlight.add(block);

					if(block.getLocation().getX() == pos1.getX() && block.getLocation().getY() == pos2.getY()) hightlight.add(block);
					if(block.getLocation().getY() == pos1.getY() && block.getLocation().getZ() == pos2.getZ()) hightlight.add(block);
					if(block.getLocation().getX() == pos1.getX() && block.getLocation().getZ() == pos2.getZ()) hightlight.add(block);
					blocks.add(block);
				}
			}
		}
		Highlight = hightlight;
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
}
