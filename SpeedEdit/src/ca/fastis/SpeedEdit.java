package ca.fastis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
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
	static Map<Player, Location> ListPosition1 = new HashMap<Player, Location>();
	static Map<Player, Location> ListPosition2 = new HashMap<Player, Location>();
	static Map<Player, List<Block>> SelectedBlocks = new HashMap<Player, List<Block>>();
	static Map<Player, List<Block>> HighlightZone = new HashMap<Player, List<Block>>();
	static Material Tool = Material.STICK;
	static int ToolID = 537469636;
	static String ToolName = ChatColor.GREEN + "Speed Edit Hammer";
	int TickingIDParticle;

	public void onEnable() {
		server = this.getServer();
		console = server.getConsoleSender();
		server.getPluginManager().registerEvents(this, this);
		initializePlugin();
		console.sendMessage(ChatColor.GREEN + "" + ChatColor.ITALIC + "SpeedEdit Loaded");
		startParticleShowTimer();
	}

	private void startParticleShowTimer() {
		TickingIDParticle = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for(Entry<Player, List<Block>> entry : HighlightZone.entrySet()) {
					Player player = entry.getKey();
					for(Block block : entry.getValue()) {
						player.spawnParticle(Particle.END_ROD, block.getLocation().getX()+0.5, block.getLocation().getY()+0.5, block.getLocation().getZ()+0.5, 0, 0, 0.02, 0);
					}
				}
			}
		}, 0L, 5L);
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
		Bukkit.getScheduler().cancelTask(TickingIDParticle);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreakEvent(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.getInventory().getItemInMainHand().getType() == Tool && player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == ToolID && player.hasPermission("speededit.use") && player.getGameMode() == GameMode.CREATIVE) { //TODO : SURVIVAL
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractEvent(PlayerInteractEvent event) { //TODO FUSION LEFT AND RIGHT
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(player.hasPermission("speededit.use")) {
			if (player.getInventory().getItemInMainHand().getType() == Tool && player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == ToolID && event.getAction().equals(Action.LEFT_CLICK_BLOCK) && player.getGameMode() == GameMode.CREATIVE && event.getHand().equals(EquipmentSlot.HAND)) { //TODO : SURVIVAL
				ListPosition1.put(player, block.getLocation());
				refreshSelectionZone(player);
				player.sendMessage(ChatColor.DARK_GRAY + "SpeedEdit " + ChatColor.GREEN + "Position 1" + ChatColor.DARK_GRAY + " set");
				event.setCancelled(true);
			} else if (player.getInventory().getItemInMainHand().getType() == Tool  && player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == ToolID && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && player.getGameMode() == GameMode.CREATIVE && event.getHand().equals(EquipmentSlot.HAND)) { //TODO : SURVIVAL
				ListPosition2.put(player, block.getLocation());
				refreshSelectionZone(player);
				player.sendMessage(ChatColor.DARK_GRAY + "SpeedEdit " + ChatColor.GREEN + "Position 2" + ChatColor.DARK_GRAY + " set");
				event.setCancelled(true);
			}
		}
	}

	static void refreshSelectionZone(Player player) {//TODO : MESSAGES FROM HERE AND SHOW BLOCK COUNT WITH MAP LENGTH
		if(ListPosition1.containsKey(player) && ListPosition2.containsKey(player)) SelectedBlocks.put(player, getSelectedZone(player, ListPosition1.get(player), ListPosition2.get(player)));
	}

	public static List<Block> getSelectedZone(Player player, Location pos1, Location pos2)
	{
		List<Block> hightlight = new ArrayList<Block>();
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
					if(block.getLocation().getX() == pos1.getX() && block.getLocation().getY() == pos1.getY()) hightlight.add(block);
					if(block.getLocation().getY() == pos1.getY() && block.getLocation().getZ() == pos1.getZ()) hightlight.add(block);
					if(block.getLocation().getX() == pos1.getX() && block.getLocation().getZ() == pos1.getZ()) hightlight.add(block);
					
					if(block.getLocation().getX() == pos2.getX() && block.getLocation().getY() == pos2.getY()) hightlight.add(block);
					if(block.getLocation().getY() == pos2.getY() && block.getLocation().getZ() == pos2.getZ()) hightlight.add(block);
					if(block.getLocation().getX() == pos2.getX() && block.getLocation().getZ() == pos2.getZ()) hightlight.add(block);
					
					
					if(block.getLocation().getX() == pos2.getX() && block.getLocation().getY() == pos1.getY()) hightlight.add(block);
					if(block.getLocation().getY() == pos2.getY() && block.getLocation().getZ() == pos1.getZ()) hightlight.add(block);
					if(block.getLocation().getX() == pos2.getX() && block.getLocation().getZ() == pos1.getZ()) hightlight.add(block);
					
					if(block.getLocation().getX() == pos2.getX() && block.getLocation().getY() == pos1.getY()) hightlight.add(block);
					if(block.getLocation().getY() == pos2.getY() && block.getLocation().getZ() == pos1.getZ()) hightlight.add(block);
					if(block.getLocation().getX() == pos2.getX() && block.getLocation().getZ() == pos1.getZ()) hightlight.add(block);
					
					if(block.getLocation().getX() == pos1.getX() && block.getLocation().getY() == pos2.getY()) hightlight.add(block);
					if(block.getLocation().getY() == pos1.getY() && block.getLocation().getZ() == pos2.getZ()) hightlight.add(block);
					if(block.getLocation().getX() == pos1.getX() && block.getLocation().getZ() == pos2.getZ()) hightlight.add(block);
					
					if(block.getLocation().getX() == pos1.getX() && block.getLocation().getY() == pos2.getY()) hightlight.add(block);
					if(block.getLocation().getY() == pos1.getY() && block.getLocation().getZ() == pos2.getZ()) hightlight.add(block);
					if(block.getLocation().getX() == pos1.getX() && block.getLocation().getZ() == pos2.getZ()) hightlight.add(block);
					blocks.add(block);
				}
			}
		}
		HighlightZone.put(player, hightlight);
		return blocks;
	}
}
