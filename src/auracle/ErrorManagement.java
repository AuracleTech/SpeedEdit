package auracle;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ErrorManagement {
	Player player;

	public ErrorManagement(Player player) {
		this.player = player;
	}

	public boolean hasPermission(Player player, String permission, boolean showMessage){
		if(player.hasPermission(permission) || player.hasPermission("speededit.*") || player.isOp()) 
			return true;
		else {
			if(showMessage) player.sendMessage(ChatColor.RED + "No permission.");
			return false;
		}
	}

	public boolean isMaterial(String materialName) {
		Material material = Material.matchMaterial(materialName);
		if(material != null)
			if(material.isBlock())
				return true;
			else {
				player.sendMessage(ChatColor.DARK_GRAY + "The material " + ChatColor.DARK_RED + materialName + ChatColor.DARK_GRAY + " can't be used");
				return false;
			}
		else {
			player.sendMessage(ChatColor.DARK_GRAY + "The material " + ChatColor.DARK_RED + materialName + ChatColor.DARK_GRAY + " can't be found");
			return false;
		}
	}

	public boolean hasPositionReady() {
		if(SpeedEdit.getUser(player).isBothPosSet()) {
			if(SpeedEdit.getUser(player).isBothPosSameWorld())
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
		player.sendMessage(ChatColor.DARK_GRAY + "Speed Edit Error : " + ChatColor.DARK_RED + e.getMessage());
	}

	public boolean isArgsLength(String[] args, int length, String correct) { return isArgsLength(args, length, length, correct); }
	public boolean isArgsLength(String[] args, int length) { return isArgsLength(args, length, length, null); }
	public boolean isArgsLength(String[] args, int minLength, int maxLength) {	return isArgsLength(args, minLength, maxLength, null); }
	public boolean isArgsLength(String[] args, int minLength, int maxLength, String correct) {
		if(args.length >= minLength && args.length <= maxLength)
			return true;
		else {
			if(correct != null) player.sendMessage(ChatColor.DARK_GRAY + "Use the command like this " + ChatColor.DARK_RED + correct);
			return false;
		}
	}

	public boolean hasUndo(int undoQtt) {
		if(!SpeedEdit.getUser(player).undo.isEmpty() && SpeedEdit.getUser(player).undo.size() >= undoQtt)
			return true;
		else {
			player.sendMessage(ChatColor.DARK_GRAY + "You can only undo " + ChatColor.DARK_RED + SpeedEdit.getUser(player).undo.size() + ChatColor.DARK_GRAY + " more times");
			return false;
		}
	}

	public boolean hasRedo(int redoQtt) {
		if(!SpeedEdit.getUser(player).redo.isEmpty() && SpeedEdit.getUser(player).redo.size() >= redoQtt)
			return true;
		else {
			player.sendMessage(ChatColor.DARK_GRAY + "You can only redo " + ChatColor.DARK_RED + SpeedEdit.getUser(player).redo.size() + ChatColor.DARK_GRAY + " more times");
			return false;
		}
	}

	public boolean isNumber(String string) {
		try {
			int integer = Integer.parseInt(string);
			if(integer >= 1 && integer <= Integer.MAX_VALUE)
				return true;
		} catch(NumberFormatException e){}
		player.sendMessage(ChatColor.DARK_RED + string + ChatColor.DARK_GRAY +  " is not a valid number");
		return false;
	}

	public boolean isCPapi() {
		if(SpeedEdit.CPapi != null) return true; else {
			player.sendMessage(ChatColor.DARK_RED + "CoreProtect" + ChatColor.DARK_GRAY + " is not installed on this server");
			return false;
		}
	}

	public void unsafeTeleport() {
		player.sendMessage(ChatColor.DARK_GRAY + "Teleportation impossible due to " + ChatColor.DARK_RED + "unsafe result location");
	}
}
