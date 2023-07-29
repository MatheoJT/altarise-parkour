package net.altarise.parkour.game.properties;

import net.altarise.api.minecraft.scoreboard.Scoreboard;
import net.altarise.api.text.Component;
import net.altarise.parkour.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GameScoreboard implements Scoreboard {

    private final Player player;
    private final GameProperties currentMap;

    public GameScoreboard(Player player) {
        this.player = player;
        this.currentMap = Parkour.get.get(Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCurrentMap());
    }

    @Override
    public Component getDisplayName() {
        return Component.text(" §f• §6§lPARKOUR §f•");
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public String[] setLines(String s) {


        final long timer = Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCheckpoint() == -1 ? Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getLastTimer() : Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getStopWatch().getTime();
        final String display = Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCheckpoint() == -1 ? "Dernier Temps" : "En Jeu";

        return new String[] {
                "",
                "§3❯§f❯ §3§lInformations",
                " §b▪ §fCarte §4"+ currentMap.getName(),
                " §b▪ "+ Parkour.get.get(Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCurrentMap()).getDifficulty().getDisplayName(),
                " §b▪ §fJoueur(s): §e"+ Bukkit.getWorld("world").getPlayers().size(),
                "",
                "§3❯§f❯ §3§lMeilleur Temps",
                " §b▪ "+ Parkour.getInstance().getGameManager().getFormatedDuration(Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getBestTimer()),
                "",
                "§3❯§f❯ §3§l" + display,
                " §b▪ "+ Parkour.getInstance().getGameManager().getFormatedDuration(timer),
                "",
                s


        };
    }
}
