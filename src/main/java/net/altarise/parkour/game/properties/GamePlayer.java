package net.altarise.parkour.game.properties;

import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GamePlayer {

    private final UUID uuid;
    private int checkpoint;
    private long currentTimer;
    private long bestTimer;
    private long lastTimer;
    private int showPlayers;
    private String currentMap;
    private List<String> finishedMaps = new ArrayList<>();

    private final StopWatch stopWatch = new StopWatch();

    public GamePlayer(UUID uuid) {
        this.uuid = uuid;
        this.checkpoint = -1;
        this.currentTimer = -1;
        this.bestTimer = 0;
        this.lastTimer = 0;
        this.showPlayers = 0;
    }

    public String getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(String currentMap) {
        this.currentMap = currentMap;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(int checkpoint) {
        this.checkpoint = checkpoint;
    }

    public long getCurrentTimer() {
        return currentTimer;
    }

    public void setCurrentTimer(long currentTimer) {
        this.currentTimer = currentTimer;
    }

    public long getBestTimer() {
        return bestTimer;
    }

    public void setBestTimer(long bestTimer) {
        this.bestTimer = bestTimer;
    }

    public long getLastTimer() {
        return lastTimer;
    }

    public void setLastTimer(long lastTimer) {
        this.lastTimer = lastTimer;
    }

    public int getShowPlayers() {
        return showPlayers;
    }

    public void setShowPlayers(int showPlayers) {
        this.showPlayers = showPlayers;
    }

    public StopWatch getStopWatch() {
        return stopWatch;
    }

    public List<String> getFinishedMaps() {
        return finishedMaps;
    }

    public void addFinishedMap(String map) {
        finishedMaps.add(map);
    }

    public void removeFinishedMap(String map) {
        finishedMaps.remove(map);
    }

    public boolean hasFinishedMap(String map) {
        return finishedMaps.contains(map);
    }




}
