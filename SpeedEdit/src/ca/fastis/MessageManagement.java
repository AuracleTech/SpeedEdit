package ca.fastis;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class MessageManagement {

	public static void command(Player player, String string, String SpyMessage) {
		player.sendMessage(ChatColor.GREEN + "SE " + ChatColor.DARK_GRAY + " " + string);
		if(SpyMessage != null) for (Player online : SpeedEdit.server.getOnlinePlayers()) if (online.getUniqueId() != player.getUniqueId() && (online.isOp() || new ErrorManagement(online).hasPermission(online, "speededit.spy", false))) online.sendMessage(ChatColor.GREEN + "SE Spy " + ChatColor.DARK_GRAY + SpyMessage);
	}

}
