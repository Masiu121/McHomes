package com.oxology.mchomes;

import org.bukkit.Bukkit;
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
            case "teleportTo" -> {
                if(!player.hasPermission("mchomes.home.teleport.others")) {
                    messageManager.sendMessage(6, sender);
                    return true;
                }

                if(args.length < 2) {
                    messageManager.sendMessage(9, sender);
                    return true;
                }

                if(args.length < 3) {
                    messageManager.sendMessage(0, sender);
                    return true;
                }

                Player otherPlayer = Bukkit.getPlayer(args[1]);
                if(otherPlayer == null) {
                    messageManager.sendMessage(10, sender);
                    return true;
                }

                Home home = homeManager.getHome(otherPlayer, args[2]);
                if(home == null) {
                    messageManager.sendMessage(3, sender);
                    return true;
                }

                otherPlayer.teleport(home.getLocation());
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
            case "list" -> {
                if(!player.hasPermission("mchomes.home.list")) {
                    messageManager.sendMessage(6, sender);
                    return true;
                }

                if(args.length < 2) {
                    List<Home> homes = homeManager.getHomes(player);

                    if (homes == null) {
                        messageManager.sendMessage(8, sender);
                        return true;
                    }

                    for (int i = 0; i < homes.size(); i++) {
                        messageManager.listHomes(sender, i + 1, homes.get(i));
                    }
                    return true;
                }

                Player otherPlayer = Bukkit.getPlayer(args[1]);
                if(otherPlayer == null) {
                    messageManager.sendMessage(10, sender);
                    return true;
                }

                List<Home> homes = homeManager.getHomes(otherPlayer);

                if (homes == null) {
                    messageManager.sendMessage(11, sender);
                    return true;
                }

                for (int i = 0; i < homes.size(); i++) {
                    messageManager.listHomes(sender, i + 1, homes.get(i));
                }
                return true;
            }
            default -> {
                messageManager.sendMessage(12, sender);
                return true;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tab = new ArrayList<>();

        if(args.length == 1) {
            if(sender.hasPermission("mchomes.home.set"))
                tab.add("set");
            if(sender.hasPermission("mchomes.home.teleport"))
                tab.add("teleport");
            if(sender.hasPermission("mchomes.home.teleport.others"))
                tab.add("teleportTo");
            if(sender.hasPermission("mchomes.home.remove"))
                tab.add("remove");
            if(sender.hasPermission("mchomes.home.list")) {
                tab.add("list");
            }
            return tab;
        }

        if(args.length == 2) {
            if(sender.hasPermission("mchomes.home.set") && args[0].equalsIgnoreCase("set")) return tab;

            if(sender.hasPermission("mchomes.home.teleport") && args[0].equalsIgnoreCase("teleport")) {
                List<String> homeNames = new ArrayList<>();
                for(Home home : homeManager.getHomes((Player) sender)) {
                    homeNames.add(home.getName());
                }

                return homeNames;
            }

            if(sender.hasPermission("mchomes.home.remove") && args[0].equalsIgnoreCase("remove")) {
                List<String> homeNames = new ArrayList<>();
                for(Home home : homeManager.getHomes((Player) sender)) {
                    homeNames.add(home.getName());
                }
                return homeNames;
            }

            if(sender.hasPermission("mchomes.home.list.others") && args[0].equalsIgnoreCase("list")) {
                return null;
            } else if(sender.hasPermission("mchomes.home.list") && args[0].equalsIgnoreCase("list")) {
                return tab;
            }

            if(sender.hasPermission("mchomes.home.teleport.others") && args[0].equalsIgnoreCase("teleportTo")) {
                return null;
            }
        }

        if(args.length == 3) {
            if(sender.hasPermission("mchomes.home.teleport.others") && args[0].equalsIgnoreCase("teleportTo")) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) return tab;

                List<String> homeNames = new ArrayList<>();
                for (Home home : homeManager.getHomes(player)) {
                    homeNames.add(home.getName());
                }

                return homeNames;
            }
        }

        return null;
    }
}
