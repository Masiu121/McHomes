package com.oxology.mchomes;

import org.bukkit.ChatColor;
import org.bukkit.Location;
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
            case 0 -> finalMessage += plugin.getConfig().getString("no-home-name-msg");
            case 1 -> finalMessage += plugin.getConfig().getString("home-exists-msg");
            case 2 -> finalMessage += plugin.getConfig().getString("home-set-msg");
            case 3 -> finalMessage += plugin.getConfig().getString("home-not-exists-msg");
            case 4 -> finalMessage += plugin.getConfig().getString("teleporting-msg");
            case 5 -> finalMessage += plugin.getConfig().getString("home-removed-msg");
            case 6 -> finalMessage += plugin.getConfig().getString("no-permission-msg");
            case 7 -> finalMessage += plugin.getConfig().getString("max-homes-msg");
            case 8 -> finalMessage += plugin.getConfig().getString("no-homes-msg");
            case 9 -> finalMessage += plugin.getConfig().getString("no-player-name-msg");
            case 10 -> finalMessage += plugin.getConfig().getString("player-not-exists-msg");
            case 11 -> finalMessage += plugin.getConfig().getString("player-has-no-homes-msg");
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', finalMessage));
    }

    public void listHomes(CommandSender sender, int homeNumber, Home home) {
        if(!plugin.getConfig().getBoolean("enable-messages")) return;
        String prefix = "";
        if(plugin.getConfig().getBoolean("enable-prefix-for-list")) {
            prefix = plugin.getConfig().getString("prefix");
        }

        String numberRegex = "\\{number}";
        String worldRegex = "\\{world}";
        String nameRegex = "\\{name}";
        String xRegex = "\\{x}";
        String yRegex = "\\{y}";
        String zRegex = "\\{z}";

        String finalMessage = "" + prefix + plugin.getConfig().getString("list-homes-msg");
        finalMessage = finalMessage.replaceAll(numberRegex, homeNumber + "");
        finalMessage = finalMessage.replaceAll(worldRegex, home.getLocation().getWorld().getName());
        finalMessage = finalMessage.replaceAll(nameRegex, home.getName());
        finalMessage = finalMessage.replaceAll(xRegex, home.getLocation().getBlockX() + "");
        finalMessage = finalMessage.replaceAll(yRegex, home.getLocation().getBlockY() + "");
        finalMessage = finalMessage.replaceAll(zRegex, home.getLocation().getBlockZ() + "");

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', finalMessage));
    }
}
