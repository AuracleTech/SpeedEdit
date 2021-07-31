package auracle;

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
	Player player;
	Map<Integer, Location> positions = new HashMap<>();
	List<Location> Highlight = null;
	public boolean clearRedo = false;
	List<Location> SelectedZone = new ArrayList<>();
	List<HashMap<Location, BlockData>> undo = new ArrayList<>();
	List<HashMap<Location, BlockData>> redo = new ArrayList<>();
	HashMap<Vector, BlockData> clipboard = new HashMap<>();
	Location copyLocation = null;

	public UserData(Player player){
		this.player = player;
	}

	public Location getPosition(int position) {
		return positions.get(position);
	}

	public void setPosition(int position, Location location, boolean showMessage) {
		World world = location.getWorld();
		assert world != null;
		WorldBorder border = world.getWorldBorder();
		double borderRadius = border.getSize() / 2;
		double xMin = border.getCenter().getX() - borderRadius;
		double xMax = border.getCenter().getX() + borderRadius - 1;
		double yMax = (double) world.getMaxHeight() - 1;
		double yMin = 0.0;
		double zMin = border.getCenter().getZ() - borderRadius;
		double zMax = border.getCenter().getZ() + borderRadius - 1;
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		if(x < xMin) location = new Location(world, xMin, y, z);
		if(x > xMax) location = new Location(world, xMax, y, z);
		if(y < yMin) location = new Location(world, x, yMin, z);
		if(y > yMax) location = new Location(world, x, yMax, z);
		if(z < zMin) location = new Location(world, x, y, zMin);
		if(z > zMax) location = new Location(world, x, y, zMax);
		boolean isNew = !(positions.containsKey(position) && location == positions.get(position));
		positions.put(position, location);
		int blockCount = 1;
		if(isBothPosSet() && isBothPosSameWorld()) {
			blockCount = Functions.zoneCount(positions.get(1), positions.get(2));
			SelectedZone = Functions.getLocationsInZone("", positions.get(1), positions.get(2)); // TODO : OPTIMISE BLOCK WORK
			if(isNew) if(blockCount <= 500000) Highlight = Functions.getLocationsInZone("skeleton", positions.get(1), positions.get(2)); else Highlight = null;
		}
		copyLocation = null;
		if(showMessage) MessageManagement.command(player, "Position §e" + position + "§7 set" + ((isBothPosSet() && isBothPosSameWorld()) ? " (§e" + blockCount + "§7 blocks)" : ""), null);
	}

	public boolean isBothPosSet(){
		return positions.containsKey(1) && positions.containsKey(2);
	}

	public boolean isBothPosSameWorld(){
		return positions.get(1).getWorld() == positions.get(2).getWorld();
	}

	public void addUndo(HashMap<Location, BlockData> memory) {
		undo.add(memory);
		if(clearRedo) {
			redo = new ArrayList<>();
			clearRedo = false;
		}
	}

	public void addRedo(HashMap<Location, BlockData> memory) {
		redo.add(memory);
	}
}
