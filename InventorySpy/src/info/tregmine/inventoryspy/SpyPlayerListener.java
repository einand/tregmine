package info.tregmine.inventoryspy;


import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;


public class SpyPlayerListener extends PlayerListener {
	private final Main plugin;

	public SpyPlayerListener(Main instance) {
		this.plugin = instance;
		plugin.getServer();
	}

	public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
		event.getPlayer().getInventory().clear();
	}

	public void onPlayerInteract(PlayerInteractEvent event) {


		if (event.getClickedBlock().getType() != null) {
			if (event.getClickedBlock().getType() == Material.CHEST && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
				event.setCancelled(true);
				return;
			}
		}
	}


	public void onInventoryOpen (PlayerInventoryEvent event) {
		event.getPlayer().sendMessage("INV");
	}

	public void onPlayerDropItem (PlayerDropItemEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}



		this.plugin.whoDropedItem.put(event.getItemDrop().hashCode(), event.getPlayer().getName());
	}

	public void onPlayerPickupItem (PlayerPickupItemEvent event){


		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}


		if (event.getItem().getItemStack().getType() == Material.MOB_SPAWNER) {
			event.setCancelled(true);
			return;
		}


		if (this.plugin.whoDropedItem.containsKey(event.getItem().hashCode())) {

			if (event.isCancelled()) {
				return;
			}

			String from = this.plugin.whoDropedItem.get(event.getItem().hashCode());

			if (from != null && !event.getPlayer().getName().matches(from)) {

				info.tregmine.api.TregminePlayer tregminePlayerFrom = this.plugin.tregmine.tregminePlayer.get(from);
				info.tregmine.api.TregminePlayer tregminePlayerTo =this.plugin.tregmine.tregminePlayer.get(event.getPlayer().getName());

				this.plugin.log.info (event.getItem().getItemStack().getAmount() + ":" + event.getItem().getItemStack().getType().toString() + " " + from + " ==> " + event.getPlayer().getName() );
				if	 (!tregminePlayerFrom.getMetaBoolean("invis") && ! tregminePlayerTo.getMetaBoolean("invis")) {
					event.getPlayer().sendMessage("You got " + event.getItem().getItemStack().getAmount() + " " + event.getItem().getItemStack().getType().toString().toLowerCase() + " from " + from );
					this.plugin.getServer().getPlayer(from).sendMessage("You gave " + event.getItem().getItemStack().getAmount() + " " + event.getItem().getItemStack().getType().toString().toLowerCase() + " to " + event.getPlayer().getName()  );
				}
			}
			this.plugin.whoDropedItem.put(event.getItem().hashCode(), null);

		}


	}

}
