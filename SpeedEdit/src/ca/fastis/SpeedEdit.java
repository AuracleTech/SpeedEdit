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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

public class SpeedEdit extends JavaPlugin {
	static Server server;
	static ConsoleCommandSender console;
	static Material Tool = Material.STICK; static int ToolID = 537469636; String ToolName = ChatColor.GREEN + "Speed Edit Hammer";
	int repeatingHighlightTaskID;
	static Map<Player, UserData> SEuserData = new HashMap<Player, UserData>();
	static CoreProtectAPI CPapi = null;
	HighlightZone HZ = new HighlightZone();

	public void onEnable() {
		CPapi = getCoreProtect();
		server = this.getServer();
		console = server.getConsoleSender();
		server.getPluginManager().registerEvents(new ManageEvents(), this);
		initializePlugin();
		console.sendMessage(ChatColor.GREEN + "Speed Edit v" + this.getDescription().getVersion() + " Enabled");
	}

	private void initializePlugin() {
		this.getCommand("pos1").setExecutor(new CommandPos(1));
		this.getCommand("pos2").setExecutor(new CommandPos(2));
		this.getCommand("set").setExecutor(new CommandSet());
		this.getCommand("replace").setExecutor(new CommandReplace());
		this.getCommand("hammer").setExecutor(new commandHammer());
		this.getCommand("undo").setExecutor(new CommandUndo());
		this.getCommand("redo").setExecutor(new CommandRedo());
		this.getCommand("copy").setExecutor(new CommandCopy());
		this.getCommand("paste").setExecutor(new CommandPaste());
		this.getCommand("expand").setExecutor(new CommandExpand());
		this.getCommand("move").setExecutor(new CommandMove());
		this.getCommand("walls").setExecutor(new CommandWalls());
	}

	private CoreProtectAPI getCoreProtect() {
		Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");
		if (plugin == null || !(plugin instanceof CoreProtect)) return null;
		CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
		if (CoreProtect.isEnabled() == false) return null; 
		if (CoreProtect.APIVersion() < 6) return null;
		return CoreProtect;
	}

	@Override
	public void onDisable() {
		console.sendMessage(ChatColor.RED + "Speed Edit v" + this.getDescription().getVersion() + " Disabled");
		Bukkit.getScheduler().cancelTask(repeatingHighlightTaskID);
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

	public static UserData getUser(Player player) {
		if(!SEuserData.containsKey(player)) SEuserData.put(player, new UserData(player));
		return SEuserData.get(player);
	}
}
