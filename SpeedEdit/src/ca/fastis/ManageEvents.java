package ca.fastis;

import org.bukkit.GameMode;
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
		if (player.getInventory().getItemInMainHand().getType() == Tool && player.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData() && player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == ToolID && player.hasPermission("speededit.use") && player.getGameMode() == GameMode.CREATIVE) { //TODO : SURVIVAL
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractEvent(PlayerInteractEvent event) { //TODO FUSION LEFT AND RIGHT
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(player.hasPermission("speededit.use")) {
			if (player.getInventory().getItemInMainHand().getType() == Tool && player.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData() && player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == ToolID && event.getAction().equals(Action.LEFT_CLICK_BLOCK) && player.getGameMode() == GameMode.CREATIVE && event.getHand().equals(EquipmentSlot.HAND)) { //TODO : SURVIVAL
				ListPosition1.put(player, block.getLocation());
				refreshSelectionZone(player, 1);
				event.setCancelled(true);
			} else if (player.getInventory().getItemInMainHand().getType() == Tool && player.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData() && player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == ToolID && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && player.getGameMode() == GameMode.CREATIVE && event.getHand().equals(EquipmentSlot.HAND)) { //TODO : SURVIVAL
				ListPosition2.put(player, block.getLocation());
				refreshSelectionZone(player, 2);
				event.setCancelled(true);
			}
		}
	}
	
	
}
