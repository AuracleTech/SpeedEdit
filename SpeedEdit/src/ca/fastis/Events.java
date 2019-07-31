package ca.fastis;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import net.coreprotect.CoreProtectAPI;

public class Events {
	static CoreProtectAPI cpapi = SpeedEdit.cpapi;
	
	public static void blockChange(String user, Location location, Material type, BlockData blockData) {

	}

	public static void blockChange(Player player, Block block, Material matchMaterial) {
		cpapi.logRemoval(player.getName(), block.getLocation(), block.getType(), block.getBlockData());
		block.setType(matchMaterial);
		cpapi.logPlacement(player.getName(), block.getLocation(), matchMaterial, block.getBlockData());
	}
}
