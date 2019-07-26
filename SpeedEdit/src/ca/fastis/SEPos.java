package ca.fastis;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SEPos {
	Player player = null;
	Location pos1 = null; 
	Location pos2 = null;
	
	public SEPos(Player player2, Location location1, Location location2) {
		this.player = player2;
		this.pos1 = location1;
		this.pos2 = location2;
	}
}
