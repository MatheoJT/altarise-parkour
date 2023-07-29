package net.altarise.parkour.game.properties;

import net.altarise.api.utils.LocationUtils;
import net.altarise.parkour.game.GameDifficulty;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameProperties {


    private final Location spawn;
    private final Location startLocation;
    private final Location endLocation;
    private final String name;
    private final GameDifficulty difficulty;
    private final List<Byte> standableClays = new ArrayList<>();
    private final Map<Integer, Location> checkpoints = new HashMap<>();
    private final boolean checkpointsNeeded;
    private final int voidY;


    public GameProperties(File configFile) {
        YamlConfiguration mapConfig = YamlConfiguration.loadConfiguration(configFile);

        this.name = mapConfig.getString("properties.name");
        this.difficulty = GameDifficulty.valueOf(mapConfig.getString("properties.difficulty").toUpperCase());
        this.checkpointsNeeded = mapConfig.getBoolean("properties.checkpoints-needed");
        this.standableClays.addAll(mapConfig.getByteList("properties.standable-clays"));

        this.spawn = LocationUtils.str2loc(mapConfig.getString("locations.spawn"));
        this.startLocation = LocationUtils.str2loc(mapConfig.getString("locations.start"));
        this.endLocation = LocationUtils.str2loc(mapConfig.getString("locations.end"));

        this.voidY = mapConfig.getInt("properties.void");

        int i = 1;
        for (String checkpoint : mapConfig.getStringList("locations.checkpoints")) {
            this.checkpoints.put(i++, LocationUtils.str2loc(checkpoint));
        }
    }

    public Location getSpawn() {
        return spawn;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public String getName() {
        return name;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public List<Byte> getStandableClays() {
        return standableClays;
    }

    public Map<Integer, Location> getCheckpoints() {
        return checkpoints;
    }

    public int getVoidY() {
        return voidY;
    }

    public boolean isCheckpointsNeeded() {
        return checkpointsNeeded;
    }
}
