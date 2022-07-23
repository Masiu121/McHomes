package com.oxology.mchomes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class HomeManager {
    private final NamespacedKey homesPlayerKey;
    private final JavaPlugin plugin;
    
    public HomeManager(JavaPlugin plugin) {
        homesPlayerKey = new NamespacedKey(plugin, "homes");
        this.plugin = plugin;
    }
    
    public String encrypt(List<Home> homes) {
        StringBuilder encryptedHomes = new StringBuilder();
        for(Home home : homes) {
            String encryptedHome = encrypt(home);
            if(encryptedHomes.toString().equals("")) {
                encryptedHomes.append(encryptedHome);
            } else {
                encryptedHomes.append(",").append(encryptedHome);
            }
        }
        return encryptedHomes.toString();
    }

    public String encrypt(Home home) {
        return home.getName() + " " + home.getLocation().getWorld().getName() + " " +
                (home.getLocation().getBlockX() + 0.5d) + " " +
                (home.getLocation().getBlockY()) + " " +
                (home.getLocation().getBlockZ() + 0.5d);
    }

    public List<Home> decrypt(String homes) {
        List<Home> decryptedHomes = new ArrayList<>();
        String[] encryptedHomes = homes.split(",");
        for(String encryptedHome : encryptedHomes) {
            String[] encryptedHomeDetails = encryptedHome.split(" ");
            Home home = new Home(new Location(Bukkit.getWorld(encryptedHomeDetails[1]),
                    Double.parseDouble(encryptedHomeDetails[2]),
                    Double.parseDouble(encryptedHomeDetails[3]),
                    Double.parseDouble(encryptedHomeDetails[4])),
                    encryptedHomeDetails[0]);
            decryptedHomes.add(home);
        }

        return decryptedHomes;
    }

    public int addHome(Player player, Home home) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        if(!playerData.has(homesPlayerKey, PersistentDataType.STRING)) {
            playerData.set(homesPlayerKey, PersistentDataType.STRING, encrypt(home));
            return 0;
        }

        String encryptedPlayerHomes = playerData.get(homesPlayerKey, PersistentDataType.STRING);
        if(encryptedPlayerHomes == null || encryptedPlayerHomes.equals("")) {
            playerData.set(homesPlayerKey, PersistentDataType.STRING, encrypt(home));
            return 0;
        }

        for(Home playerHome : decrypt(encryptedPlayerHomes)) {
            if(playerHome.getName().equalsIgnoreCase(home.getName())) {
                return -1;
            }
        }

        if(plugin.getConfig().getInt("homes-limit") <= decrypt(encryptedPlayerHomes).size()) {
            return -2;
        }

        playerData.set(homesPlayerKey, PersistentDataType.STRING, encryptedPlayerHomes + "," + encrypt(home));
        return 0;
    }
    
    public int removeHome(Player player, String homeName) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if(!playerData.has(homesPlayerKey, PersistentDataType.STRING)) return -1;

        String encryptedPlayerHomes = playerData.get(homesPlayerKey, PersistentDataType.STRING);
        if(encryptedPlayerHomes == null || encryptedPlayerHomes.equals("")) return -1;
        
        List<Home> decryptedPlayerHomes = decrypt(encryptedPlayerHomes);

        boolean homeFound = false;
        for(Home home : decryptedPlayerHomes) {
            if (home.getName().equalsIgnoreCase(homeName)) {
                homeFound = true;
                break;
            }
        }

        decryptedPlayerHomes.removeIf(home -> home.getName().equalsIgnoreCase(homeName));

        if(!homeFound) {
            return -1;
        }
        
        playerData.set(homesPlayerKey, PersistentDataType.STRING, encrypt(decryptedPlayerHomes));
        return 0;
    }
    
    public List<String> getHomeNames(Player player) {
        List<String> homeNames = new ArrayList<>();

        PersistentDataContainer playerData = player.getPersistentDataContainer();
        if(!playerData.has(homesPlayerKey, PersistentDataType.STRING)) return null;
        
        String encryptedPlayerHomes = playerData.get(homesPlayerKey, PersistentDataType.STRING);
        if(encryptedPlayerHomes == null || encryptedPlayerHomes.equals("")) return null;
        
        for(Home home : decrypt(encryptedPlayerHomes)) {
            homeNames.add(home.getName());
        }
        
        return homeNames;
    }

    public Home getHome(Player player, String name) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if(!playerData.has(homesPlayerKey, PersistentDataType.STRING)) return null;

        String encryptedPlayerHomes = playerData.get(homesPlayerKey, PersistentDataType.STRING);
        if(encryptedPlayerHomes == null || encryptedPlayerHomes.equals("")) return null;

        List<Home> decryptedPlayerHomes = decrypt(encryptedPlayerHomes);
        for(Home home : decryptedPlayerHomes) {
            if(home.getName().equalsIgnoreCase(name)) return home;
        }

        return null;
    }

    public String getHomeString(Player player) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();

        if(!playerData.has(homesPlayerKey, PersistentDataType.STRING)) return null;

        return playerData.get(homesPlayerKey, PersistentDataType.STRING);
    }
}
