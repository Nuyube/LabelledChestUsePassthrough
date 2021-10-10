package xyz.nuyube.minecraft.lcup;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Messages {
    private static Messages instance;

    public static Messages getInstance() {
        if (instance == null) {
            instance = new Messages();
        }
        return instance;
    }

    private Messages() {
        messages = new HashMap<String, String>();
    }

    private Map<String, String> messages;

    public String get(String Key) {
        return messages.get(Key);
    }

    public void init() {
        // Read our configuration
        log("Initializing messages...", 1);
        messages = new HashMap<String, String>();
        File DataDirectory = JavaPlugin.getPlugin(LCUP.class).getDataFolder();
        File ConfigFile = new File(DataDirectory + "/messages.yml");
        // Log file paths
        log("Data Directory is " + DataDirectory.getAbsolutePath(), 3);
        log("Messages File is " + ConfigFile.getAbsolutePath(), 3);
        // If our file or directory doesn't exist,
        if (!DataDirectory.exists() || !ConfigFile.exists()) {
            log("Messages do not exist... Writing.", 3);
            // Write it
            JavaPlugin.getPlugin(LCUP.class).saveResource("messages.yml", true);
        }
        // Read the file (which might be new)
        readMessages();
    }

    public void readMessages() {
        log("Reading configuration file", 3);
        // Get files
        File DataDirectory = JavaPlugin.getPlugin(LCUP.class).getDataFolder();
        File ConfigFile = new File(DataDirectory.getPath() + "/messages.yml");
        Scanner fr = null;
        try {
            // Open our file
            fr = new Scanner(ConfigFile);
        } catch (FileNotFoundException e) {
            log("Failed to read messages file: Not found", 0);
            return;
        }
        while (fr.hasNextLine()) {
            String s = fr.nextLine();
            if (s.contains(":") && !s.trim().startsWith("#")) {
                s = s.trim();
                try {
                    String key = s.substring(0, s.indexOf(':')).trim();
                    String value = s.substring(s.indexOf(':') + 1).trim();
                    messages.put(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        fr.close();

    }

    public void emitConsole(String key) {
        emitConsole(key, null);
    }

    public void emitConsoleSevere(String key) {
        emitConsole(key, null);
    }

    public void emitPlayer(Player player, String key) {
        emitPlayer(player, key, null);
    }

    public void emitConsole(String key, HashMap<String, String> replacements) {
        String value = get(key);
        value = replaceDictionary(replacements, value);
        if (!value.isBlank())
            logger.info(value);
    }

    public void emitConsoleSevere(String key, HashMap<String, String> replacements) {
        String value = get(key);
        value = replaceDictionary(replacements, value);
        if (!value.isBlank())
            logger.severe(value);
    }

    public void emitPlayer(Player player, String key, HashMap<String, String> replacements) {
        String value = get(key);
        value = replaceDictionary(replacements, value);
        if (!value.isBlank())
            player.sendMessage(value);
    }

    private String replaceDictionary(HashMap<String, String> replacements, String value) {
        if (value == null)
            return "";
        value = value.replace("&&", String.valueOf(ChatColor.COLOR_CHAR));
        if (replacements != null) {
            Set<String> keys = replacements.keySet();
            Iterator<String> keyIterator = keys.iterator();
            for (; keyIterator.hasNext();) {
                String s = keyIterator.next();
                value = value.replace(s, replacements.get(s).toString());
            }
        }
        return value;
    }

    private static final int LOGGING_LEVEL = 4;
    private static final Logger logger = JavaPlugin.getPlugin(LCUP.class).getLogger();

    private final void log(String message, int logLevel) {
        if (LOGGING_LEVEL >= logLevel) {
            logger.info(message);
        }
    }
}
