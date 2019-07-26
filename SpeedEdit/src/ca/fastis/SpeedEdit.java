package ca.fastis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

public class SpeedEdit extends JavaPlugin implements Listener  {
	static Server server;
	static ConsoleCommandSender console;
	static Map<Player, SEPos> ListSEPos = new HashMap<Player, SEPos>();

	public void onEnable() {
		server = this.getServer();
		console = server.getConsoleSender();
		server.getPluginManager().registerEvents(this, this);
		initializePlugin();
		console.sendMessage(ChatColor.GREEN + "SpeedEdit Loaded");
	}

	private void initializePlugin() {
		this.getCommand("set").setExecutor(new CommandSet());
	}

	@Override
	public void onDisable() {
		console.sendMessage(ChatColor.RED + "SpeedEdit Disabled");
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreakEvent(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.getInventory().getItemInMainHand().getType() == Material.BRICK && player.hasPermission("speededit.set") && player.getGameMode() == GameMode.CREATIVE) { //TODO : SURVIVAL
			event.setCancelled(true);
		}
	}

	/*Location l = event.getBlock().getLocation(); l.getWorld().getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ()).setType(Material.AIR);*/
	//player.sendMessage("POSITION 1 SET " + ListSEPos.get(player).getPos1() + " # " + ListSEPos.get(player).getPos2());

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(player.hasPermission("speededit.set")) {
			if (player.getInventory().getItemInMainHand().getType() == Material.BRICK && event.getAction().equals(Action.LEFT_CLICK_BLOCK) && player.getGameMode() == GameMode.CREATIVE && event.getHand().equals(EquipmentSlot.HAND)) { //TODO : SURVIVAL
				if(ListSEPos.containsKey(player)) {
					ListSEPos.put(player, new SEPos(player, block.getLocation(), ListSEPos.get(player).pos2));
				} else {
					ListSEPos.put(player, new SEPos(player, block.getLocation(), new Location(null, 0, 0, 0)));
				}
				player.sendMessage(ChatColor.DARK_GRAY + "SpeedEdit " + ChatColor.GREEN + "Position 1" + ChatColor.DARK_GRAY + " set");
			} else if (player.getInventory().getItemInMainHand().getType() == Material.BRICK && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && player.getGameMode() == GameMode.CREATIVE && event.getHand().equals(EquipmentSlot.HAND)) { //TODO : SURVIVAL
				if(ListSEPos.containsKey(player)) {
					ListSEPos.put(player, new SEPos(player, ListSEPos.get(player).pos1, block.getLocation()));
				} else {
					ListSEPos.put(player, new SEPos(player, new Location(null, 0, 0, 0), block.getLocation()));
				}
				player.sendMessage(ChatColor.DARK_GRAY + "SpeedEdit " + ChatColor.GREEN + "Position 2" + ChatColor.DARK_GRAY + " set");
			}
		}
	}

	public static List<Block> getBlocks(Location pos1, Location pos2)
	{
		try {
			if(pos1.getWorld() != pos2.getWorld())
				return null;
			World world = pos1.getWorld();
			List<Block> blocks = new ArrayList<>();
			int x1 = pos1.getBlockX();
			int y1 = pos1.getBlockY();
			int z1 = pos1.getBlockZ();

			int x2 = pos2.getBlockX();
			int y2 = pos2.getBlockY();
			int z2 = pos2.getBlockZ();

			int lowestX = Math.min(x1, x2);
			int lowestY = Math.min(y1, y2);
			int lowestZ = Math.min(z1, z2);

			int highestX = lowestX == x1 ? x2 : x1;
			int highestY = lowestX == y1 ? y2 : y1;
			int highestZ = lowestX == z1 ? z2 : z1;

			for(int x = lowestX; x <= highestX; x++)
				for(int y = lowestY; x <= highestY; y++)
					for(int z = lowestZ; x <= highestZ; z++)
						blocks.add(world.getBlockAt(x, y, z));
			return blocks;
		} catch(Exception e) {
			server.broadcastMessage("If you read this I fucked up again ¯_(ツ)_/¯" + e.getMessage());
			return null;
		}
	}
}
