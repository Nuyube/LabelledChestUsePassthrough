package xyz.nuyube.minecraft.lcup;
 
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

  class ChestOpenEventHandler implements Listener {
    @EventHandler
    //Hook the PlayerInteract event
    public void onPlayerInteract(PlayerInteractEvent event){
        //If we're right-clicking a block
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            
            //And that block is a Sign
            if(clickedBlock.getState() instanceof Sign) {
                Player p = event.getPlayer();
                //Get info about the sign
                Sign S = (Sign)clickedBlock.getState();
                //Get the block attached to the sign
                Directional a = (Directional)S.getBlockData();
                Block Attached = clickedBlock.getRelative(a.getFacing().getOppositeFace());
                //If that block is a Chest
                if(Attached.getState() instanceof Chest) {
                    //Open the inventory
                    Chest c = (Chest)Attached.getState();
                    Inventory i = c.getInventory(); 
                    p.openInventory(i);  
                } 
            }
        }
    }
}