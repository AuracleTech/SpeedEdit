package ca.fastis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class UserData {
	Player player = null;
	Map<Integer, Location> positions = new HashMap<Integer, Location>();
	List<Location> Highlight = null;
	public boolean clearRedo = false;
	List<Location> SelectedZone = new ArrayList<Location>();
	List<HashMap<Location, BlockData>> undo = new ArrayList<HashMap<Location, BlockData>>();
	List<HashMap<Location, BlockData>> redo = new ArrayList<HashMap<Location, BlockData>>();
	HashMap<Vector, BlockData> clipboard = new HashMap<Vector, BlockData>();
	Location copyLocation = null;

	public UserData(Player player){
		this.player = player;
	}

	public Location getPosition(int position) {
		return positions.get(position);
	}
	
	public void setPosition(int position, Location argLocation, boolean showMessage) {
		if(argLocation.getY() > 255) argLocation = new Location(argLocation.getWorld(), argLocation.getX(), 255, argLocation.getZ());
		if(argLocation.getY() < 0) argLocation = new Location(argLocation.getWorld(), argLocation.getX(), 0, argLocation.getZ());
		positions.put(position, argLocation);
		if(isBothPosSet() && isBothPosSameWorld()) {
			SelectedZone = Functions.getLocationsInZone("", positions.get(1), positions.get(2));
			if(SelectedZone.size() <= 500000) Highlight = Functions.getLocationsInZone("skeleton", positions.get(1), positions.get(2)); else Highlight = null;
		}
		copyLocation = null;
		if(showMessage) MessageManagement.command(player, "Position �e" + position + "�7 set" + ((isBothPosSet() && isBothPosSameWorld()) ? " - �e" + SelectedZone.size() + "�7 blocks" : ""), null);
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

	public void addUndo(HashMap<Location, BlockData> memory) {
		undo.add(memory);
	}

	public void addRedo(HashMap<Location, BlockData> memory) {
		redo.add(memory);
	}
}
