package ca.fastis;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ManageEvents implements Listener{
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getSlotType() == SlotType.ARMOR && e.getSlot() == 39 && e.getCursor().getType() == Material.POTION && e.getCursor().getItemMeta().hasCustomModelData()) {
			ItemStack itemInHand = e.getCursor().clone();
			p.setItemOnCursor(null);
			p.getInventory().setHelmet(itemInHand);
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreakEvent(BlockBreakEvent event) {
		Player player = event.getPlayer();
		ErrorManagement EM = new ErrorManagement(player);
		if (player.getInventory().getItemInMainHand().getType() == SpeedEdit.Tool && player.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData() && player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == SpeedEdit.ToolID && EM.hasPermission(player, "speededit.use", true)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		ErrorManagement EM = new ErrorManagement(player);
		if(EM.hasPermission(player, "speededit.use", true)) {
			if ((event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && player.getInventory().getItemInMainHand().getType() == SpeedEdit.Tool && player.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData() && player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == SpeedEdit.ToolID && event.getHand().equals(EquipmentSlot.HAND)) {
				int PosToSet = event.getAction().equals(Action.LEFT_CLICK_BLOCK) ? 1 : event.getAction().equals(Action.RIGHT_CLICK_BLOCK) ? 2 : 1;
				SpeedEdit.getUser(player).setPosition(PosToSet, block.getLocation(), true);
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuitEvent(PlayerQuitEvent event){
		SpeedEdit.deleteUser(event.getPlayer());
	}
}