package ca.fastis;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FuturEvents {

	public static void onPositionChangeEvent(Player player, Integer position, Location location) {
		player.sendMessage(ChatColor.DARK_GRAY + "Speed Edit " + ChatColor.GREEN + "Position " + position + ChatColor.DARK_GRAY + " set");
		//ON POSITION CHANGE EVENT
	}

	private static void onHighlightRefreshEvent(Player player) {
		/*UserData userData = SpeedEdit.getUser(player);
		if(userData.isBothPosSetAndValid()) {
			SelectedBlocks.put(player, getSelectedZone(player, ListPosition1.get(player), ListPosition2.get(player)));
			msg += " [" + SelectedBlocks.get(player).size() + " Blocks]";
		}
		player.sendMessage(ChatColor.DARK_GRAY + "Speed Edit " + ChatColor.GREEN + "Position " + Pos + ChatColor.DARK_GRAY + " set");*/
		//onHighlightRefreshEvent
	}
}