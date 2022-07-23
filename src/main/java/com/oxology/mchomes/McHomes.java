package com.oxology.mchomes;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class McHomes extends JavaPlugin {
    public static final String MSG_PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "Mc" + ChatColor.GOLD + "Homes" + ChatColor.DARK_GRAY + "]";

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        loadConfig();

        this.getCommand("home").setExecutor(new HomeCmd(this));
        this.getCommand("home").setTabCompleter(new HomeCmd(this));

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void loadConfig() {
        config.options().copyDefaults(true);
        saveConfig();
    }
}
