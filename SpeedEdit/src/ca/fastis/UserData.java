package ca.fastis;

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
	List<Block> Selection = null;
	List<Block> Highlight = null;
	Map<Player, Map<UUID, Block>> undo = new LinkedHashMap<Player, Map<UUID, Block>>();
	Map<Player, Map<UUID, Block>> redo = new LinkedHashMap<Player, Map<UUID, Block>>();

	public UserData(Player player){
		this.player = player;
	}

	public void setPosition(int position, Location argLocation) {
		this.positions.put(position, argLocation);
		FuturEvents.onPositionChangeEvent(this.player, position, argLocation);
	}

	public boolean isBothPosSet(){
		if(positions.containsKey(1) && positions.containsKey(2))
			return true;
		else
			return false;
	}
}
