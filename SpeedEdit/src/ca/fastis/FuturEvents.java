package ca.fastis;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FuturEvents {

	public static void onPositionChangeEvent(Player player, Integer position, Location location) {
		if(SpeedEdit.SEuserData.get(player).isBothPosSet()) {
			FuturEvents.refreshHighlightEvent(player);
		}
		player.sendMessage(ChatColor.DARK_GRAY + "Speed Edit " + ChatColor.GREEN + "Position " + position + ChatColor.DARK_GRAY + " set");
	}

	private static void refreshHighlightEvent(Player player) {
		if(SpeedEdit.SEuserData.get(player).isBothPosSet()) {
			SelectedBlocks.put(player, getSelectedZone(player, ListPosition1.get(player), ListPosition2.get(player)));
			msg += " [" + SelectedBlocks.get(player).size() + " Blocks]";
		}
		player.sendMessage(ChatColor.DARK_GRAY + "Speed Edit " + ChatColor.GREEN + "Position " + Pos + ChatColor.DARK_GRAY + " set");
	}
}