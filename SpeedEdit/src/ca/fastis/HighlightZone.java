package ca.fastis;

import java.util.Map.Entry;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HighlightZone {
	int TaskID;
	
	public HighlightZone(Server server, Plugin plugin) {
		TaskID = server.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for(Entry<Player, UserData> entry : SpeedEdit.SEuserData.entrySet()) {
					Player player = entry.getKey();
					DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 255, 64), 1);
					for(Block block : UserData.getHighlightZone()) {
						player.spawnParticle(Particle.REDSTONE, block.getLocation().getX()+0.5, block.getLocation().getY()+0.5, block.getLocation().getZ()+0.5, 1, 0, 0, 0, 0, dustOptions);
					}
				}
			}
		}, 0L, 6L);
	}

	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
