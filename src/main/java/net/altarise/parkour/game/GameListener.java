package net.altarise.parkour.game;

import net.altarise.api.AltariseAPI;
import net.altarise.api.utils.SymbolBank;
import net.altarise.bean.model.data.account.Rank;
import net.altarise.parkour.Parkour;
import net.altarise.parkour.game.properties.GameScoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Parkour.getInstance().getGameManager().registerPlayer(event.getPlayer().getUniqueId());
        Parkour.getInstance().getGameManager().reloadVisibility(event.getPlayer());
        event.getPlayer().teleport(Parkour.getInstance().getGameProperties().getSpawn());
        AltariseAPI.get().getMinecraftProvider().getScoreboardManager().setPlayerScoreboard(event.getPlayer(), new GameScoreboard(event.getPlayer()));
        Parkour.getInstance().getGameManager().getPlayers().add(event.getPlayer());
        if(Parkour.getInstance().getGameManager().getTask() == null) {
            Parkour.getInstance().getGameManager().startTask();
        }

        event.setJoinMessage("§a" + SymbolBank.ROUND + " §f" + event.getPlayer().getName() + " §7à rejoint le parkour.");

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Rank rank = AltariseAPI.get().getDataProvider().getAccountManager().getRank(event.getPlayer().getUniqueId().toString());
        String prefix = AltariseAPI.get().getSystemProvider().getGuildManager().hasGuild(event.getPlayer().getUniqueId().toString()) ? "§7[" + AltariseAPI.get().getSystemProvider().getGuildManager().getPlayerGuild(event.getPlayer().getUniqueId().toString()).getTag() + "] " : "";
        prefix += "§" + rank.getRankColor() + rank.getPrefix() + " §8" + SymbolBank.VERTICAL_SEPARATOR + " §" + rank.getRankColor() + event.getPlayer().getName();
        event.setFormat(prefix + " §" + rank.getChatColor() + SymbolBank.FRENCH_QUOTATION + " " + event.getMessage());
    }


    @EventHandler
    public void onDamage(EntityDamageEvent event) {event.setCancelled(true);}

    @EventHandler
    public void onBreak(BlockDamageEvent event) {event.setCancelled(true);}

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {event.setCancelled(true);}

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {event.setCancelled(true);}

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {event.setCancelled(true);}



    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if(Parkour.getInstance().getGameManager().getPlayer(event.getPlayer().getUniqueId()).getCheckpoint() != -1) {
            Parkour.getInstance().getGameManager().endGame(event.getPlayer(), false);
        }
        Parkour.getInstance().getGameManager().getPlayers().remove(event.getPlayer());
        if(Parkour.getInstance().getGameManager().getPlayers().size() == 0) {
            Parkour.getInstance().getGameManager().stopTask();
        }
        event.setQuitMessage("§c" + SymbolBank.ROUND + " §f" + event.getPlayer().getName() + " §7à quitter le parkour.");
    }
}
