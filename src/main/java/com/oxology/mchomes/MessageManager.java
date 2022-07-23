package com.oxology.mchomes;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageManager {
    private JavaPlugin plugin;

    public MessageManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(int messageId, CommandSender sender) {
        if(!plugin.getConfig().getBoolean("enable-messages")) return;
        String prefix = "";
        if(plugin.getConfig().getBoolean("enable-prefix")) {
            prefix = plugin.getConfig().getString("prefix");
        }

        String finalMessage = "" + prefix;

        switch (messageId) {
            case 0:
                finalMessage += plugin.getConfig().getString("no-home-name-msg");
                break;
            case 1:
                finalMessage += plugin.getConfig().getString("home-exists-msg");
                break;
            case 2:
                finalMessage += plugin.getConfig().getString("home-set-msg");
                break;
            case 3:
                finalMessage += plugin.getConfig().getString("home-not-exists-msg");
                break;
            case 4:
                finalMessage += plugin.getConfig().getString("teleporting-msg");
                break;
            case 5:
                finalMessage += plugin.getConfig().getString("home-removed-msg");
                break;
            case 6:
                finalMessage += plugin.getConfig().getString("no-permission-msg");
                break;
            case 7:
                finalMessage += plugin.getConfig().getString("max-homes-msg");
                break;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', finalMessage));
    }
}
