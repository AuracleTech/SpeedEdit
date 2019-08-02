package ca.fastis;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ErrorManagement {
	Player player;

	public ErrorManagement(Player player) {
		this.player = player;
	}

	public boolean hasPermission(Player player, String permission){
		if(player.hasPermission(permission) || player.hasPermission("speededit.*") || player.isOp()) 
			return true;
		else {
			player.sendMessage(ChatColor.RED + "No permission.");
			return false;
		}
	}

	public boolean isMaterial(String material) {
		if(Material.matchMaterial(material) != null)
			return true;
		else {
			player.sendMessage(ChatColor.DARK_GRAY + "The material " + ChatColor.DARK_RED + material + ChatColor.DARK_GRAY + " can't be found");
			return false;
		}
	}

	public boolean hasPositionReady() {
		if(SpeedEdit.ListPosition1.containsKey(player) && SpeedEdit.ListPosition2.containsKey(player)) {
			if(SpeedEdit.ListPosition1.get(player).getWorld() != SpeedEdit.ListPosition2.get(player).getWorld())
				return true;
			else {
				player.sendMessage(ChatColor.DARK_GRAY + "Your 2 positions must be in the " + ChatColor.DARK_RED + "same world");
				return false;
			}
		} else {
			player.sendMessage(ChatColor.DARK_GRAY + "You need to select " + ChatColor.DARK_RED + "2 positions" + ChatColor.DARK_GRAY + ", use right and left click with a brick on blocks");
			return false;
		}
	}

	public void throwException(Exception e) {
		player.sendMessage(ChatColor.DARK_GRAY + "Speed Edit error : " + ChatColor.DARK_RED + e.getMessage());
	}

	public boolean isArgsCorrect(String[] args, int length, String correct) { return isArgsCorrect(args, length, length, correct); }
	public boolean isArgsCorrect(String[] args, int minLength, int maxLength, String correct) {
		if(args.length <= minLength && args.length >= maxLength)
			return true;
		else {
			player.sendMessage(ChatColor.DARK_GRAY + "Use the command like this " + ChatColor.DARK_RED + correct);
			return false;
		}
	}
}
