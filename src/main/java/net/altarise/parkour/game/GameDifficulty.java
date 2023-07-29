package net.altarise.parkour.game;

import org.bukkit.ChatColor;

public enum GameDifficulty {

    EASY("§aFacile", ChatColor.GREEN, 0),
    MEDIUM("§6Normal", ChatColor.GOLD, 1),
    HARD("§cDifficile", ChatColor.RED, 2),
    DEFAULT("§fAucun", ChatColor.WHITE, 3);

    private final String displayName;
    private final ChatColor chatColor;
    private final int id;


    GameDifficulty(String displayName, ChatColor chatColor, int id) {
        this.displayName = displayName;
        this.id = id;
        this.chatColor = chatColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getId() {
        return id;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }
}
