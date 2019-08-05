package ca.fastis;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

public class SpeedEdit extends JavaPlugin {
	static Server server;
	static ConsoleCommandSender console;
	
	static Material Tool = Material.STICK;
	static int ToolID = 537469636;
	static String ToolName = ChatColor.GREEN + "Speed Edit Hammer";
	
	static Map<Player, UserData> SEuserData = new HashMap<Player, UserData>();
	HighlightZone HZ;
	
	static CoreProtectAPI CPapi = null;

	public void onEnable() {
		CPapi = getCoreProtect();
		server = this.getServer();
		HZ = new HighlightZone(server, this);
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
		this.getCommand("outline").setExecutor(new CommandOutline());
	}

	@Override
	public void onDisable() {
		console.sendMessage(ChatColor.RED + "Speed Edit v" + this.getDescription().getVersion() + " Disabled");
		Bukkit.getScheduler().cancelTask(HZ.getID());
	}

	public static UserData getUser(Player player) {
		if(!SEuserData.containsKey(player)) SEuserData.put(player, new UserData(player));
		return SEuserData.get(player);
	}
	
	public static void deleteUser(Player player) {
		if(!SEuserData.containsKey(player)) SEuserData.remove(player);
	}
	
	private CoreProtectAPI getCoreProtect() {
		Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");
		if (plugin == null || !(plugin instanceof CoreProtect)) return null;
		CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
		if (CoreProtect.isEnabled() == false) return null; 
		if (CoreProtect.APIVersion() < 6) return null;
		return CoreProtect;
	}
}
