package xyz.nuyube.minecraft.lcup;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.EnderChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
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

        Configuration config = Configuration.getInstance();
        // If we're right-clicking a block

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();

            // And that block is a Sign
            if (clickedBlock.getState() instanceof Sign) {
                Player player = event.getPlayer();
                // Get info about the sign
                Sign sign = (Sign) clickedBlock.getState();

                BlockData data = sign.getBlockData();
                // Get the block attached to the sign
                BlockFace backFace = null;

                // The sign is a wall sign
                if (data instanceof Directional) {
                    backFace = ((Directional) data).getFacing().getOppositeFace();
                }
                // The sign is a floor sign
                else if (data instanceof Rotatable) {
                    // Check if this behavior is enabled
                    if (config.isFloorSignEnabled()) {
                        // Get the Rotatable data for the sign
                        Rotatable floorSign = (Rotatable) data;
                        backFace = floorSign.getRotation().getOppositeFace();

                        // Switch on its facing direction. Take the direction it's facing most toward
                        switch (backFace) {
                        case EAST:
                        case EAST_NORTH_EAST:
                        case EAST_SOUTH_EAST:
                            backFace = BlockFace.EAST;
                            break;
                        case WEST:
                        case WEST_NORTH_WEST:
                        case WEST_SOUTH_WEST:
                            backFace = BlockFace.WEST;
                            break;
                        case NORTH:
                        case NORTH_NORTH_EAST:
                        case NORTH_NORTH_WEST:
                            backFace = BlockFace.NORTH;
                            break;
                        case SOUTH:
                        case SOUTH_SOUTH_EAST:
                        case SOUTH_SOUTH_WEST:
                            backFace = BlockFace.SOUTH;
                            break;
                        // Should be a dead default if this is a sign as signs cannot face UP or DOWN.
                        default:
                            return;
                        }
                    } else {
                        // If this behavior was disabled, warn only once.
                        LCUP lcup = LCUP.getInstance();
                        if (!lcup.isFloorSignWarning()) {
                            LCUP.PluginLogger
                                    .info("Sign interact event ignored (sign is not a Directional (wall) sign)."
                                            + " Set enable-floor-signs: true in the config to enable this behavior."
                                            + " This message will not appear again.");
                            lcup.setFloorSignWarning(true);
                        }
                        return;
                    }
                }
                Block Attached = clickedBlock.getRelative(backFace);
                handleBlockState(Attached, player);
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