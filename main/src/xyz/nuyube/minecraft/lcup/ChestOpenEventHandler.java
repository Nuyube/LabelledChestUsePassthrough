package xyz.nuyube.minecraft.lcup;

import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.EnderChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.block.data.Directional;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

class ChestOpenEventHandler implements Listener {
    // returns true if an inventory was opened
    private boolean handleBlockState(Block Attached, Player p) {
        if (Attached.getState() instanceof EnderChest) {
            Inventory i = p.getEnderChest();
            p.openInventory(i);
            return true;
        }
        // Otherwise handle it like a container
        else if (Attached.getState() instanceof Container) {
            Container c = (Container) Attached.getState();
            Inventory i = c.getInventory();
            p.openInventory(i);
            return true;
        }
        return false;
    }

    @EventHandler
    // Hook the PlayerInteract event
    public void onPlayerInteract(PlayerInteractEvent event) {
        // If we're right-clicking a block

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();

            // And that block is a Sign
            if (clickedBlock.getState() instanceof Sign) {
                Player p = event.getPlayer();
                // Get info about the sign
                Sign S = (Sign) clickedBlock.getState();
                // Get the block attached to the sign
                Directional a = null;
                try {
                    a = (Directional) S.getBlockData();
                } catch (ClassCastException ex) {
                    if (!LCUP.getInstance().isFloorSignWarning()) {
                        LCUP.PluginLogger.info(
                                "Sign interact event ignored (sign is not a Directional (wall) sign). This message will not appear again.");
                        LCUP.getInstance().setFloorSignWarning(true);
                    }
                    return;
                }
                Block Attached = clickedBlock.getRelative(a.getFacing().getOppositeFace());
                handleBlockState(Attached, p);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (Configuration.getInstance().isItemFrameEnabled()) {
            if (event.getRightClicked() instanceof ItemFrame) {
                ItemFrame frame = (ItemFrame) event.getRightClicked();
                if (event.getPlayer().isSneaking())
                    return;
                else {
                    Block frameblock = frame.getLocation().getBlock();
                    Block Attached = frameblock.getRelative(frame.getAttachedFace());
                    Player p = event.getPlayer();
                    if (Attached.getState() instanceof Container || Attached.getState() instanceof EnderChest) {
                        event.setCancelled(true);
                        handleBlockState(Attached, p);
                    }
                }
            }
        }
    }
}