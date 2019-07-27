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
	static Material Tool = Material.STICK;
	static int ToolID = 537469636;
	static String ToolName = ChatColor.GREEN + "Speed Edit Hammer";
	
	public void onEnable() {
		server = this.getServer();
		console = server.getConsoleSender();
		server.getPluginManager().registerEvents(this, this);
		initializePlugin();
		console.sendMessage(ChatColor.GREEN + "" + ChatColor.ITALIC + "SpeedEdit Loaded");
	}

	private void initializePlugin() {
		this.getCommand("pos1").setExecutor(new CommandPos(1));
		this.getCommand("pos2").setExecutor(new CommandPos(2));
		this.getCommand("set").setExecutor(new CommandSet());
		this.getCommand("hammer").setExecutor(new commandHammer());
	}

	@Override
	public void onDisable() {
		console.sendMessage(ChatColor.RED + "SpeedEdit Disabled");
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreakEvent(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.getInventory().getItemInMainHand().getType() == Tool && player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == ToolID && player.hasPermission("speededit.use") && player.getGameMode() == GameMode.CREATIVE) { //TODO : SURVIVAL
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(player.hasPermission("speededit.use")) {
			if (player.getInventory().getItemInMainHand().getType() == Tool && player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == ToolID && event.getAction().equals(Action.LEFT_CLICK_BLOCK) && player.getGameMode() == GameMode.CREATIVE && event.getHand().equals(EquipmentSlot.HAND)) { //TODO : SURVIVAL
				if(ListSEPos.containsKey(player)) {
					ListSEPos.put(player, new SEPos(player, block.getLocation(), ListSEPos.get(player).pos2));
				} else {
					ListSEPos.put(player, new SEPos(player, block.getLocation(), null));
				}
				player.sendMessage(ChatColor.DARK_GRAY + "SpeedEdit " + ChatColor.GREEN + "Position 1" + ChatColor.DARK_GRAY + " set");
				event.setCancelled(true);
			} else if (player.getInventory().getItemInMainHand().getType() == Tool  && player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == ToolID && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && player.getGameMode() == GameMode.CREATIVE && event.getHand().equals(EquipmentSlot.HAND)) { //TODO : SURVIVAL
				if(ListSEPos.containsKey(player)) {
					ListSEPos.put(player, new SEPos(player, ListSEPos.get(player).pos1, block.getLocation()));
				} else {
					ListSEPos.put(player, new SEPos(player, null, block.getLocation()));
				}
				player.sendMessage(ChatColor.DARK_GRAY + "SpeedEdit " + ChatColor.GREEN + "Position 2" + ChatColor.DARK_GRAY + " set");
				event.setCancelled(true);
			}
		}
	}

	public static List<Block> getBlocks(Location pos1, Location pos2)
	{
		List<Block> blocks = new ArrayList<Block>();
        int topBlockX = (pos1.getBlockX() < pos2.getBlockX() ? pos2.getBlockX() : pos1.getBlockX());
        int bottomBlockX = (pos1.getBlockX() > pos2.getBlockX() ? pos2.getBlockX() : pos1.getBlockX());
        int topBlockY = (pos1.getBlockY() < pos2.getBlockY() ? pos2.getBlockY() : pos1.getBlockY());
        int bottomBlockY = (pos1.getBlockY() > pos2.getBlockY() ? pos2.getBlockY() : pos1.getBlockY());
        int topBlockZ = (pos1.getBlockZ() < pos2.getBlockZ() ? pos2.getBlockZ() : pos1.getBlockZ());
        int bottomBlockZ = (pos1.getBlockZ() > pos2.getBlockZ() ? pos2.getBlockZ() : pos1.getBlockZ());
        for(int x = bottomBlockX; x <= topBlockX; x++) {
            for(int z = bottomBlockZ; z <= topBlockZ; z++) {
                for(int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = pos1.getWorld().getBlockAt(x, y, z);
                    blocks.add(block);
                }
            }
        }
        return blocks;
	}
}
