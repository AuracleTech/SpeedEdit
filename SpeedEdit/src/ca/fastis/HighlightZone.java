package ca.fastis;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class HighlightZone {
	public HighlightZone() {
		repeatingHighlightTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for(Entry<Player, List<Block>> entry : HighlightZone.entrySet()) {
					Player player = entry.getKey();
					DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 255, 64), 1);
					for(Block block : entry.getValue()) {
						player.spawnParticle(Particle.REDSTONE, block.getLocation().getX()+0.5, block.getLocation().getY()+0.5, block.getLocation().getZ()+0.5, 1, 0, 0, 0, 0, dustOptions);
					}
				}
			}
		}, 0L, 6L);
	}
}
