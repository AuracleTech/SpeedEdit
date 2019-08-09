package ca.fastis;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

public class CommandExpand implements CommandExecutor, TabCompleter {
	int maxArg = 1;
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if(!EM.hasPermission(player, "speededit.expand", true) || !EM.hasPositionReady() || !EM.isArgsLength(args, maxArg, "/Expand Distance") || !EM.isNumber(args[0])) return true;
			int ExpandQuantity = Integer.parseInt(args[0]);
			UserData userData = SpeedEdit.getUser(player);
			Location pos1 = userData.positions.get(1).clone();
			Location pos2 = userData.positions.get(2).clone();
			Location newPos = pos1.clone();
			int PosToSet = 1;
			switch(Functions.getCardinalDirection(player)) {
			case "SOUTH":
				if(pos1.getZ() > pos2.getZ()) {
					newPos = new Location(pos1.getWorld(), pos1.getX(), pos1.getY(), pos1.getZ()+ExpandQuantity);
					PosToSet = 1;
				} else {
					newPos = new Location(pos2.getWorld(), pos2.getX(), pos2.getY(), pos2.getZ()+ExpandQuantity);
					PosToSet = 2;
				}
				break;
			case "NORTH":
				if(pos1.getZ() < pos2.getZ()) {
					newPos = new Location(pos1.getWorld(), pos1.getX(), pos1.getY(), pos1.getZ()-ExpandQuantity);
					PosToSet = 1;
				} else {
					newPos = new Location(pos2.getWorld(), pos2.getX(), pos2.getY(), pos2.getZ()-ExpandQuantity);
					PosToSet = 2;
				}
				break;
			case "EAST":
				if(pos1.getX() > pos2.getX()) {
					newPos = new Location(pos1.getWorld(), pos1.getX()+ExpandQuantity, pos1.getY(), pos1.getZ());
					PosToSet = 1;
				} else {
					newPos = new Location(pos2.getWorld(), pos2.getX()+ExpandQuantity, pos2.getY(), pos2.getZ());
					PosToSet = 2;
				}
				break;
			case "WEST":
				if(pos1.getZ() < pos2.getZ()) {
					newPos = new Location(pos1.getWorld(), pos1.getX()-ExpandQuantity, pos1.getY(), pos1.getZ());
					PosToSet = 1;
				} else {
					newPos = new Location(pos2.getWorld(), pos2.getX()-ExpandQuantity, pos2.getY(), pos2.getZ());
					PosToSet = 2;
				}
				break;
			}
			switch(Functions.getPitch(player)) {
			case "UP":
				if(pos1.getY() > pos2.getY()) {
					newPos = new Location(pos1.getWorld(), pos1.getX(), pos1.getY()+ExpandQuantity, pos1.getZ());
					PosToSet = 1;
				} else {
					newPos = new Location(pos2.getWorld(), pos2.getX(), pos2.getY()+ExpandQuantity, pos2.getZ());
					PosToSet = 2;
				}
				break;
			case "DOWN":
				if(pos1.getY() < pos2.getY()) {
					newPos = new Location(pos1.getWorld(), pos1.getX(), pos1.getY()-ExpandQuantity, pos1.getZ());
					PosToSet = 1;
				} else {
					newPos = new Location(pos2.getWorld(), pos2.getX(), pos2.getY()-ExpandQuantity, pos2.getZ());
					PosToSet = 2;
				}
				break;
			}
			if(PosToSet == 1) userData.setPosition(1, newPos, false); else userData.setPosition(2, newPos, false);
			MessageManagement.command(player, "Position §e" + PosToSet + "§7 expanded now §e" + userData.SelectedZone.size() + "§7 blocks", null);
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
		List<String> returnList = Lists.newArrayList();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ErrorManagement EM = new ErrorManagement(player);
			if (EM.isArgsLength(args, maxArg)) {
				List<String> listNumbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");
				for (String material : listNumbers) {
					if (material.startsWith(args[args.length-1].toLowerCase())) returnList.add(material);
				}
			} else
				if(args.length == maxArg+1 && args[args.length-1].toLowerCase().isEmpty())
					EM.isArgsLength(args, maxArg, "/Expand Distance");
		}
		return returnList;
	}
}
