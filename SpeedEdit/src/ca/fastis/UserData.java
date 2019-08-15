package ca.fastis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
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
	
	public void setPosition(int position, Location location, boolean showMessage) {
		World world = location.getWorld();
		WorldBorder border = world.getWorldBorder();
		Double borderRadius = border.getSize() / 2;
		Double xMin = border.getCenter().getX() - borderRadius;
		Double xMax = border.getCenter().getX() + borderRadius;
		Double yMax = Double.valueOf(world.getMaxHeight());
		Double yMin = 0.0;
		Double zMin = border.getCenter().getZ() - borderRadius;
		Double zMax = border.getCenter().getZ() + borderRadius;
		Double x = location.getX();
		Double y = location.getY();
		Double z = location.getZ();
		if(location.getY() > 255) location = new Location(location.getWorld(), location.getX(), 255, location.getZ());
		if(location.getY() < 0) location = new Location(location.getWorld(), location.getX(), 0, location.getZ());
		if(x < zMin) location = new Location(world, x, y, z);
		 (x > xMin && x < xMax) && (z > zMin && z < zMax);
		
		positions.put(position, location);
		if(isBothPosSet() && isBothPosSameWorld()) {
			SelectedZone = Functions.getLocationsInZone("", positions.get(1), positions.get(2));
			if(SelectedZone.size() <= 500000) Highlight = Functions.getLocationsInZone("skeleton", positions.get(1), positions.get(2)); else Highlight = null;
		}
		copyLocation = null;
		if(showMessage) MessageManagement.command(player, "Position §e" + position + "§7 set" + ((isBothPosSet() && isBothPosSameWorld()) ? " - §e" + SelectedZone.size() + "§7 blocks" : ""), null);
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
		if(clearRedo) {
			redo = new ArrayList<HashMap<Location, BlockData>>();
			clearRedo = false;
		}
	}

	public void addRedo(HashMap<Location, BlockData> memory) {
		redo.add(memory);
	}
}
