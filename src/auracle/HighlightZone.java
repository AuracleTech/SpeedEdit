package auracle;

import java.util.Map.Entry;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HighlightZone {
	int TaskID;
	
	public HighlightZone(Server server, Plugin plugin) {
		TaskID = server.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for(Entry<Player, UserData> entry : SpeedEdit.SEuserData.entrySet()) {
					UserData userData = entry.getValue();
					Player player = entry.getKey();
					if(userData.Highlight == null) continue;
					DustOptions blueDust = new DustOptions(Color.fromRGB(0, 191, 255), 1);
					DustOptions orangeDust = new DustOptions(Color.fromRGB(255, 191, 0), 1);
					for(Location location : userData.Highlight) {
						player.spawnParticle(Particle.REDSTONE, location.getX()+0.5, location.getY()+0.5, location.getZ()+0.5, 1, 0, 0, 0, 0, orangeDust);
					}
					if(userData.copyLocation != null) player.spawnParticle(Particle.REDSTONE, userData.copyLocation.getX()+0.5, userData.copyLocation.getY()+0.5, userData.copyLocation.getZ()+0.5, 1, 0, 0, 0, 0, blueDust);
				}
			}
		}, 0L, 6L);
	}

	public int getID() {
		return TaskID;
	}
}
