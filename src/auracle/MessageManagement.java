package auracle;

import org.bukkit.entity.Player;

public class MessageManagement {

	public static void command(Player player, String string, String SpyMessage) {
		player.sendMessage("§6§lSE §r§7»§7 " + string);
		if(SpyMessage != null) for (Player online : SpeedEdit.server.getOnlinePlayers()) if (online.getUniqueId() != player.getUniqueId() && (online.isOp() || new ErrorManagement(online).hasPermission(online, "speededit.spy", false))) online.sendMessage("§7§lSE §r§7»§7 " + SpyMessage);
	}

}
