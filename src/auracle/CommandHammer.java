package auracle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandHammer implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		Player player;
		if (sender instanceof Player) {
			player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(EM.hasPermission(player, "speededit.hammer", true)) {
				ItemStack hammer = new ItemStack(SpeedEdit.Tool, 1);
				ItemMeta hammerMeta = hammer.getItemMeta();
				assert hammerMeta != null;
				hammerMeta.setCustomModelData(SpeedEdit.ToolID);
				hammerMeta.setDisplayName(SpeedEdit.ToolName);
				hammer.setItemMeta(hammerMeta);
				player.getInventory().addItem(hammer);
				MessageManagement.command(player, "Here, take the " + SpeedEdit.ToolName, null);
			}
		}
		return true;
	}

}
