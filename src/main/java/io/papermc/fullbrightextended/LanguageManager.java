package io.papermc.fullbrightextended;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class LanguageManager {

    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public LanguageManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setupConfig() {
        configFile = new File(plugin.getDataFolder(), "languages.yml");
        if (!configFile.exists()) {
            plugin.saveResource("languages.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            setupConfig();
        }
        return config;
    }

    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            config.save(configFile);
            reloadConfig();
        } catch (IOException ex) {
            plugin.getLogger().warning("Could not save config to " + configFile.getName());
        }
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "languages.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}