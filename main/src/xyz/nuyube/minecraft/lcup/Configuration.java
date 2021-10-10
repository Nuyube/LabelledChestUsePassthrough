package xyz.nuyube.minecraft.lcup;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.io.IOException;

import com.github.nuyube.javayamlreader.JavaYAMLHelper;

import org.bukkit.plugin.java.JavaPlugin;

final class Configuration {
    private boolean itemFrameEnabled = true;

    public boolean isItemFrameEnabled() {
        return itemFrameEnabled;
    }

    private Messages messages = Messages.getInstance();
    private static Configuration instance = null;

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    private Configuration() {
        init();
    }

    public static void reload() {
        getInstance().init();
    }

    private final void readConfigFile() {
        Logger errorLogger = JavaPlugin.getPlugin(LCUP.class).getLogger();
        messages.emitConsole("reading-config");
        // Get files
        File DataDirectory = JavaPlugin.getPlugin(LCUP.class).getDataFolder();
        File ConfigFile = new File(DataDirectory.getPath() + "/config.yml");
        String fileContents;
        try {
            fileContents = Files.readString(Path.of(ConfigFile.getAbsolutePath()));
        } catch (IOException e) {
            messages.emitConsoleSevere("config-not-found");
            return;
        }
        try {
            itemFrameEnabled = Boolean
                    .valueOf(JavaYAMLHelper.getValueFromKey("enable-item-frame-handler", fileContents));
        } catch (Exception e) {
            errorLogger.severe(e.getMessage());
        }
    }

    private final void init() {
        // Read our configuration
        messages.emitConsole("initializing-config");
        File DataDirectory;
        File ConfigFile;

        DataDirectory = JavaPlugin.getPlugin(LCUP.class).getDataFolder();
        ConfigFile = new File(DataDirectory + "/config.yml");

        // If our file or directory doesn't exist,
        if (!DataDirectory.exists() || !ConfigFile.exists()) {
            messages.emitConsole("config-not-exist-writing");
            // Write it
            writeNewConfigFile();
        }
        // Read the file (which might be new)
        readConfigFile();
    }

    private final void writeNewConfigFile() {
        // Save the bundled config.yml resource
        JavaPlugin.getPlugin(LCUP.class).saveResource("config.yml", true);
    }
}
