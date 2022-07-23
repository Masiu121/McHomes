package com.oxology.mchomes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class HomeCmd implements CommandExecutor, TabCompleter {
    private HomeManager homeManager;
    private MessageManager messageManager;

    public HomeCmd(JavaPlugin plugin) {
        this.homeManager = new HomeManager(plugin);
        this.messageManager = new MessageManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) return false;

        Player player = (Player) sender;

        switch (args[0]) {
            case "set" -> {
                if(!player.hasPermission("mchomes.home.set")) {
                    messageManager.sendMessage(6, sender);
                    return true;
                }

                if (args.length < 2) {
                    messageManager.sendMessage(0, sender);
                    return true;
                }
                int result = homeManager.addHome(player, new Home(player.getLocation(), args[1]));
                if(result == -1) {
                    messageManager.sendMessage(1, sender);
                    return true;
                }
                if(result == -2) {
                    messageManager.sendMessage(7, sender);
                    return true;
                }
                messageManager.sendMessage(2, sender);
                return true;
            }
            case "teleport" -> {
                if(!player.hasPermission("mchomes.home.teleport")) {
                    messageManager.sendMessage(6, sender);
                    return true;
                }

                if (args.length < 2) {
                    messageManager.sendMessage(0, sender);
                    return true;
                }
                Home home = homeManager.getHome(player, args[1]);
                if (home == null) {
                    messageManager.sendMessage(3, sender);
                    return true;
                }

                player.teleport(home.getLocation());
                messageManager.sendMessage(4, sender);
                return true;
            }
            case "remove" -> {
                if(!player.hasPermission("mchomes.home.remove")) {
                    messageManager.sendMessage(6, sender);
                    return true;
                }

                if (args.length < 2) {
                    messageManager.sendMessage(0, sender);
                    return true;
                }

                int result = homeManager.removeHome(player, args[1]);
                if(result == -1) {
                    messageManager.sendMessage(3, sender);
                    return true;
                }

                messageManager.sendMessage(5, sender);
                return true;
            }
            case "data" -> {
                if (args.length < 2) return false;
                if(args[1].equalsIgnoreCase("getHomeString")) {
                    if(args.length < 3) {
                        sender.sendMessage(homeManager.getHomeString((Player) sender));
                        return true;
                    }

                    Player target = Bukkit.getPlayer(args[2]);
                    if(target == null) {
                        sender.sendMessage(McHomes.MSG_PREFIX + ": " + ChatColor.DARK_RED + "Player is offline does not exists!");
                        return true;
                    }
                    sender.sendMessage(homeManager.getHomeString(target));
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tab = new ArrayList<>();

        if(args.length == 1) {
            if(sender.hasPermission("mchomes.home.set"))
                tab.add("set");
            if(sender.hasPermission("mchomes.home.teleport"))
                tab.add("teleport");
            if(sender.hasPermission("mchomes.home.remove"))
                tab.add("remove");
            if(sender.hasPermission("mchomes.admin.data")) {
                tab.add("data");
            }
            return tab;
        }

        if(args.length == 2) {
            if(sender.hasPermission("mchomes.home.set") && args[0].equalsIgnoreCase("set")) return tab;

            if(sender.hasPermission("mchomes.home.teleport") && args[0].equalsIgnoreCase("teleport")) {
                return homeManager.getHomeNames((Player) sender);
            }

            if(sender.hasPermission("mchomes.home.remove") && args[0].equalsIgnoreCase("remove")) {
                return homeManager.getHomeNames((Player) sender);
            }

            if(sender.hasPermission("mchomes.admin.data") && args[0].equalsIgnoreCase("data")) {
                tab.add("getHomeString");
                return tab;
            }
        }

        if(args.length == 3) {
            if(args[0].equalsIgnoreCase("data")) {
                if(args[1].equalsIgnoreCase("getHomeString")) {
                    return null;
                }
            }
        }

        return null;
    }
}
