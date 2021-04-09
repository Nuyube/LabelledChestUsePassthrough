package xyz.nuyube.minecraft.lcup;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class LCUP extends JavaPlugin {
 
  static Logger PluginLogger = null; 


  @Override
  public void onEnable() {
    //Start our plugin logger
    PluginLogger = getLogger();
    //Check for updates
    new UpdateChecker(this, 91103)
      .getVersion(
          version -> {
            if (
              this.getDescription().getVersion().equalsIgnoreCase(version)
            ) {} else {
              Bukkit
                .getConsoleSender()
                .sendMessage(
                  ChatColor.GREEN +
                  "[Nuyube's LCUP] There is a new update available!"
                );
            }
          }
        ); 

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
