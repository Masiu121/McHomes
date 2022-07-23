package com.oxology.mchomes;

import org.bukkit.Location;

public class Home {
    private final Location location;
    private final String name;

    public Home(Location location, String name) {
        this.location = location;
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
