package net.altarise.parkour.game;

import net.altarise.api.AltariseAPI;
import net.altarise.api.utils.ItemBuilder;
import net.altarise.api.utils.PlayerUtils;
import net.altarise.parkour.Parkour;
import net.altarise.parkour.game.inventories.PkChooser;
import net.altarise.parkour.game.properties.GamePlayer;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameManager {

    private final HashMap<UUID, GamePlayer> registeredPlayers = new HashMap<>();
    private final List<Player> delay = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();

    private BukkitTask task;

    public void registerPlayer(UUID uuid) {
        if (!registeredPlayers.containsKey(uuid)) {
            registeredPlayers.put(uuid, new GamePlayer(uuid));
            registeredPlayers.get(uuid).setCurrentMap("a");
            PlayerUtils.cleanPlayer(Bukkit.getPlayer(uuid), GameMode.ADVENTURE);
            loadHolograms(Bukkit.getPlayer(uuid));
            giveItems(Bukkit.getPlayer(uuid));
        }
    }

    public GamePlayer getPlayer(UUID uuid) {
        if (registeredPlayers.containsKey(uuid)) {
            return registeredPlayers.get(uuid);
        }
        return null;
    }

    public void giveItems(Player player) {
        player.getInventory().setItem(4, new ItemFactory(new ItemBuilder(Material.BED).displayname("§eClique §f❯ §cRetour au §fHub").build()).handle(event -> {
            event.getPlayer().performCommand("lobby");
        }).build());

        player.getInventory().setItem(0, new ItemFactory(new ItemBuilder(Material.COMPASS).displayname("§eClique §f❯ §cChanger de §fParkour").build()).handle(event -> {
            new PkChooser(player, GameDifficulty.DEFAULT).open(event.getPlayer());
        }).build());

        player.getInventory().setItem(8, new ItemFactory(new ItemBuilder(new ItemStack(Material.INK_SACK, 1, (byte) 10)).displayname("§eVisibilité §f❯ §aVisibles").build()).handle(event -> {
            registeredPlayers.get(event.getPlayer().getUniqueId()).setShowPlayers(1);
            reloadVisibility(event.getPlayer());
        }).build());


    }

    private void loadHolograms(Player player) {

        AltariseAPI.get().getMinecraftProvider().getHologramManager().createHologram(Parkour.getInstance().getGameProperties().getStartLocation().clone().add(0,0.5,0), "",  " §b❖ §f§lDébut §b❖", "§eParkour §6" + Parkour.getInstance().getGameProperties().getName()).addReceiver(player);
        AltariseAPI.get().getMinecraftProvider().getHologramManager().createHologram(Parkour.getInstance().getGameProperties().getEndLocation().clone().add(0,0.5,0), "",  " §b❖ §f§lArrivée §b❖", "§eParkour §6" + Parkour.getInstance().getGameProperties().getName()).addReceiver(player);

        for(Integer key : Parkour.getInstance().getGameProperties().getCheckpoints().keySet()) {
            AltariseAPI.get().getMinecraftProvider().getHologramManager().createHologram(Parkour.getInstance().getGameProperties().getCheckpoints().get(key), "", "   §7[§b#" + key + "§7]" , " §b❖ §fPoint de Controle §b❖ ").addReceiver(player);
        }

    }

    public void reloadVisibility(Player player) {

        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);

        switch (registeredPlayers.get(player.getUniqueId()).getShowPlayers()) {
            case 0:
                Bukkit.getOnlinePlayers().forEach(players -> {
                    if(!player.getUniqueId().equals(players.getUniqueId())) {
                        player.showPlayer(players);
                    }
                });
                player.getInventory().setItem(8, new ItemFactory(new ItemBuilder(new ItemStack(Material.INK_SACK, 1, (byte) 10)).displayname("§eVisibilité §f❯ §aVisibles").build()).handle(event -> {
                    registeredPlayers.get(player.getUniqueId()).setShowPlayers(1);
                    reloadVisibility(player);
                }).build());
                break;

                case 1:

                    Bukkit.getOnlinePlayers().forEach(players -> {
                        if(!player.getUniqueId().equals(players.getUniqueId())) {
                            if(AltariseAPI.get().getSystemProvider().getGuildManager().hasGuild(player.getUniqueId().toString())
                                    && !AltariseAPI.get().getSystemProvider().getGuildManager().getPlayerGuild(player.getUniqueId().toString()).getMembersId().contains(players.getUniqueId().toString())) player.hidePlayer(players);

                    }
                    });

                    player.getInventory().setItem(8, new ItemFactory(new ItemBuilder(new ItemStack(Material.INK_SACK, 1, (byte) 5)).displayname("§eVisibilité §f❯ §2Guilde").build()).handle(event -> {
                        registeredPlayers.get(player.getUniqueId()).setShowPlayers(2);
                        reloadVisibility(player);
                    }).build());
                    break;

            case 2:
                Bukkit.getOnlinePlayers().forEach(players ->{
                    if(!players.getUniqueId().equals(player.getUniqueId())) {
                        if(!AltariseAPI.get().getSystemProvider().getFriendManager().getFriends(player.getUniqueId().toString()).contains(players.getUniqueId().toString())) player.hidePlayer(players);
                    }
                });

                player.getInventory().setItem(8, new ItemFactory(new ItemBuilder(new ItemStack(Material.INK_SACK, 1, (byte) 9)).displayname("§eVisibilité §f❯ §dAmis").build()).handle(event -> {
                    registeredPlayers.get(player.getUniqueId()).setShowPlayers(3);
                    reloadVisibility(player);
                }).build());
                break;

            case 3:
                Bukkit.getOnlinePlayers().forEach(players ->{
                    if(!players.getUniqueId().equals(player.getUniqueId())) {
                        if(!AltariseAPI.get().getSystemProvider().getFriendManager().getFriends(player.getUniqueId().toString()).contains(players.getUniqueId().toString())
                        || (AltariseAPI.get().getSystemProvider().getGuildManager().hasGuild(player.getUniqueId().toString())
                                && !AltariseAPI.get().getSystemProvider().getGuildManager().getPlayerGuild(player.getUniqueId().toString()).getMembersId().contains(players.getUniqueId().toString()))) player.hidePlayer(players);
                    }
                });

                player.getInventory().setItem(8, new ItemFactory(new ItemBuilder(new ItemStack(Material.INK_SACK, 1, (byte) 13)).displayname("§eVisibilité §f❯ §dAmis §f& §2Guilde").build()).handle(event -> {
                    registeredPlayers.get(player.getUniqueId()).setShowPlayers(4);
                    reloadVisibility(player);
                }).build());
                break;

            case 4:
                Bukkit.getOnlinePlayers().forEach(players -> {
                    if(players.getUniqueId().equals(player.getUniqueId())) {
                        player.hidePlayer(players);
                    }

                });

                player.getInventory().setItem(8, new ItemFactory(new ItemBuilder(new ItemStack(Material.INK_SACK, 1, (byte) 8)).displayname("§eVisibilité §f❯ §cInvisible").build()).handle(event -> {
                    registeredPlayers.get(player.getUniqueId()).setShowPlayers(0);
                    reloadVisibility(player);
                }).build());
                break;

        }
    }




    public void startGame(Player player) {
        PlayerUtils.cleanPlayer(player, GameMode.ADVENTURE);

        player.getInventory().setItem(3, new ItemFactory(new ItemBuilder(Material.ARROW).displayname("§eClique §f❯ §cRetour au dernier §fCheckpoint").build()).handle(event -> teleport(event.getPlayer())).build());

        player.getInventory().setItem(5, new ItemFactory(new ItemBuilder(Material.WATCH).displayname("§eClique §f❯ §cRecommencer §fle Parkour").build()).handle(event -> {
            if(!delay.contains(player)) {
                event.getPlayer().sendMessage("§cEtes vous sûr de vouloir recommencer ?");
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_BASS, 1f, 1f);
                delay.add(event.getPlayer());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        delay.remove(event.getPlayer());
                    }
                }.runTaskLater(Parkour.getInstance(), 20*10);
            } else {
                delay.remove(event.getPlayer());
                endGame(event.getPlayer(), false);
            }

        }).build());

    }


    public void teleport(Player player) {
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1f, 1f);
        if(registeredPlayers.get(player.getUniqueId()).getCheckpoint() == -1) player.teleport(Parkour.getInstance().getGameProperties().getSpawn());
        if(registeredPlayers.get(player.getUniqueId()).getCheckpoint() == 0) player.teleport(Parkour.getInstance().getGameProperties().getStartLocation().clone().add(0, 3, 0));
        if(registeredPlayers.get(player.getUniqueId()).getCheckpoint() >= 1) player.teleport(Parkour.getInstance().getGameProperties().getCheckpoints().get(registeredPlayers.get(player.getUniqueId()).getCheckpoint()).clone().add(0, 3, 0));

    }


    public void endGame(Player player, boolean val) {
        final long timer = registeredPlayers.get(player.getUniqueId()).getCurrentTimer();
        player.teleport(Parkour.getInstance().getGameProperties().getSpawn());
        registeredPlayers.get(player.getUniqueId()).setCheckpoint(-1);
        registeredPlayers.get(player.getUniqueId()).setCurrentTimer(-1);
        registeredPlayers.get(player.getUniqueId()).getStopWatch().reset();
        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1f, 1f);
        PlayerUtils.cleanPlayer(player, GameMode.ADVENTURE);
        giveItems(player);

        if(val) {
            registeredPlayers.get(player.getUniqueId()).setLastTimer(timer);
            if(registeredPlayers.get(player.getUniqueId()).getBestTimer() >= 0 || timer < registeredPlayers.get(player.getUniqueId()).getBestTimer()) registeredPlayers.get(player.getUniqueId()).setBestTimer(timer);
        }
    }





    public String getFormatedDuration(long millis) {
        return DurationFormatUtils.formatDuration(millis, ChatColor.GREEN + "mm" + ChatColor.GRAY + "'min '" + ChatColor.GREEN + "ss" + ChatColor.GRAY + "'s '" + ChatColor.GREEN +"SSS");
    }

    public void startTask() {
        if(task != null) stopTask();
       task = new GameScheduler().runTaskTimer(Parkour.getInstance(), 0, 1);
    }

    public BukkitTask getTask() {
        return task;
    }

    public void stopTask() {
        task.cancel();
        task = null;
    }


    public List<Player> getPlayers() {
        return players;
    }
}
