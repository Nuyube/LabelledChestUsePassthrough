package xyz.nuyube.minecraft.lcup;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.jeff_media.updatechecker.UpdateChecker;

public class LCUP extends JavaPlugin {

    static Logger PluginLogger = null;
    private Messages messages;
    private boolean floorSignWarning = false;

    public boolean isFloorSignWarning() {
        return floorSignWarning;
    }

    public void setFloorSignWarning(boolean b) {
        floorSignWarning = b;
    }

    private static LCUP instance = null;

    public static LCUP getInstance() {
        if (instance == null)
            instance = new LCUP();

        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        messages = Messages.getInstance();
        messages.init();
        messages.emitConsole("enabling");
        // Start our plugin logge
        PluginLogger = getLogger();
        UpdateChecker.init(this, 91103).checkNow();
        // Register our sellxp command and its alias
        Bukkit.getPluginManager().registerEvents(new ChestOpenEventHandler(), this);
    }

    @Override
    // We don't actually do anything on disable.
    public void onDisable() {
        messages.emitConsole("disabling");
        PluginLogger = null;
    }

}