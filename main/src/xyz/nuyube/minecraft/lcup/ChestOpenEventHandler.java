package xyz.nuyube.minecraft.lcup;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest; 
import org.bukkit.block.Sign; 
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.block.data.Directional;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.DoubleChestInventory;

public class ChestOpenEventHandler implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            Player p = event.getPlayer();
            if(clickedBlock.getState() instanceof Sign) {
                 Sign S = (Sign)clickedBlock.getState();
                Directional a = (Directional)S.getBlockData();
                Block Attached = clickedBlock.getRelative(a.getFacing().getOppositeFace());
                if(Attached.getType() == Material.CHEST) {
                    Chest c = (Chest)Attached.getState();
                    Inventory i = c.getInventory();
                    if(i instanceof DoubleChestInventory) {
                        DoubleChestInventory dci = (DoubleChestInventory)i;
                        ((Chest)dci.getHolder().getLeftSide()).close();
                        ((Chest)dci.getHolder().getRightSide()).close();
                    }
                    p.openInventory(i); 
                    c.close();
                    
                } 
            }
        }
    }
}