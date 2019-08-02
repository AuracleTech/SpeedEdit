package ca.fastis;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class FuturEvents {

	public static void onPositionChangeEvent(Player player, Integer position, Location location) {
		player.sendMessage(ChatColor.DARK_GRAY + "Speed Edit " + ChatColor.GREEN + "Position " + position + ChatColor.DARK_GRAY + " set");
		//ON POSITION CHANGE EVENT
	}

	public static void onHighlightChangeEvent(Player player, List<Block> highlight) {
		player.sendMessage(ChatColor.DARK_GRAY + "Speed Edit " + ChatColor.GREEN + "Position [" + highlight.size() + " Blocks] " + ChatColor.DARK_GRAY + " set");
		//onHighlightRefreshEvent
	}
}