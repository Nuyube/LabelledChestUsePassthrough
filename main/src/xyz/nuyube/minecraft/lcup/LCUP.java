package xyz.nuyube.minecraft.lcup;

import java.util.logging.Logger;

import org.bukkit.Bukkit; 
import org.bukkit.plugin.java.JavaPlugin;

import de.jeff_media.updatechecker.UpdateChecker;

public class LCUP extends JavaPlugin {
 
  static Logger PluginLogger = null; 


  @Override
  public void onEnable() {
    //Start our plugin logger
    PluginLogger = getLogger();
    UpdateChecker.init(this, 91103).checkNow();
    //Register our sellxp command and its alias 
    Bukkit.getPluginManager().registerEvents(new ChestOpenEventHandler(), this);
  }

  @Override
  //We don't actually do anything on disable.
  public void onDisable() {
    PluginLogger.info("[Nuyube's LCUP] Disabled."); 
    PluginLogger = null;
  }

}   