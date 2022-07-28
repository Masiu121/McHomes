package com.oxology.mchomes;

import org.bukkit.plugin.java.JavaPlugin;

public final class McHomes extends JavaPlugin {

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
        saveDefaultConfig();
    }
}
