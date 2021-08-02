package auracle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CommandTop implements CommandExecutor, TabCompleter {
    int maxArg = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (sender instanceof Player player) {
            ErrorManagement EM = new ErrorManagement(player);
            if(!EM.hasPermission(player, "speededit.top", true) || !EM.isArgsLength(args, maxArg, "/Top") || !EM.isCPapi()) return true;
            World world = player.getWorld();
            Location teleportLocation = null;
            for (int Y = player.getWorld().getMaxHeight()-1; Y >= 0; Y--) {
                if (world.getBlockAt(player.getLocation().getBlockX(), Y, player.getLocation().getBlockZ()).getLocation().getBlock().getType() != Material.AIR) {
                    teleportLocation = world.getBlockAt(player.getLocation().getBlockX(), Y, player.getLocation().getBlockZ()).getLocation().add(0.5, 1, 0.5);
                    player.teleport(teleportLocation);
                    break;
                }
            }
            if (teleportLocation != null)
                MessageManagement.command(player, "You have been teleported to Y §e" + teleportLocation.getBlockY(), player.getName() + " has been teleported to Y §e" + teleportLocation.getBlockY());
            else
                EM.unsafeTeleport();
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
        if (sender instanceof Player player) {
            ErrorManagement EM = new ErrorManagement(player);
            if(args.length == maxArg+1 && args[args.length-1].toLowerCase().isEmpty())
                EM.isArgsLength(args, maxArg, "/Top");
        }
        return new ArrayList<>();
    }
}
