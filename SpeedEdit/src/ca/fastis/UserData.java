package ca.fastis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class UserData {
	Player player = null;
	Map<Integer, Location> positions = new HashMap<Integer, Location>();
	List<Location> Highlight = null;
	public boolean clearRedo = false;
	List<Location> SelectedZone = new ArrayList<Location>();
	List<HashMap<Location, Material>> undo = new ArrayList<HashMap<Location, Material>>();
	List<HashMap<Location, Material>> redo = new ArrayList<HashMap<Location, Material>>();
	HashMap<Vector, Material> clipboard = new HashMap<Vector, Material>();
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
		Double xMax = border.getCenter().getX() + borderRadius - 1;
		Double yMax = Double.valueOf(world.getMaxHeight()-1);
		Double yMin = 0.0;
		Double zMin = border.getCenter().getZ() - borderRadius;
		Double zMax = border.getCenter().getZ() + borderRadius - 1;
		Double x = location.getX();
		Double y = location.getY();
		Double z = location.getZ();
		if(x < xMin) location = new Location(world, xMin, y, z);
		if(x > xMax) location = new Location(world, xMax, y, z);
		if(y < yMin) location = new Location(world, x, yMin, z);
		if(y > yMax) location = new Location(world, x, yMax, z);
		if(z < zMin) location = new Location(world, x, y, zMin);
		if(z > zMax) location = new Location(world, x, y, zMax);
		boolean isNew = (positions.containsKey(position) && location == positions.get(position)) ? false : true;
		positions.put(position, location);
		int blockCount = 1;
		if(isBothPosSet() && isBothPosSameWorld()) {
			blockCount = Functions.zoneCount(positions.get(1), positions.get(2));
			SelectedZone = Functions.getLocationsInZone("", positions.get(1), positions.get(2)); //TODO : OPTIMISE BLOCK WORK
			if(isNew) if(blockCount <= 500000) Highlight = Functions.getLocationsInZone("skeleton", positions.get(1), positions.get(2)); else Highlight = null;
		}
		copyLocation = null;
		if(showMessage) MessageManagement.command(player, "Position §e" + position + "§7 set" + ((isBothPosSet() && isBothPosSameWorld()) ? " (§e" + blockCount + "§7 blocks)" : ""), null);
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

	public void addUndo(HashMap<Location, Material> memory) {
		undo.add(memory);
		if(clearRedo) {
			redo = new ArrayList<HashMap<Location, Material>>();
			clearRedo = false;
		}
	}

	public void addRedo(HashMap<Location, Material> memory) {
		redo.add(memory);
	}
}
