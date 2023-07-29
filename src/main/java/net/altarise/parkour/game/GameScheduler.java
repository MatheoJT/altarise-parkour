package net.altarise.parkour.game;

import net.altarise.api.AltariseAPI;
import net.altarise.api.utils.LocationUtils;
import net.altarise.api.utils.title.ActionBar;
import net.altarise.parkour.Parkour;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GameScheduler extends BukkitRunnable {

    private final List<Player> hiddenBossBar = new ArrayList<>();

    @Override
    public void run() {
        for(Player player : Parkour.getInstance().getGameManager().getPlayers()) {
           updateLocation(player);
           updateBlock(player);
          // updateBossBar(player);
        }

    }


    private void updateBossBar(Player player) {
        if (Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCheckpoint() == -1) {
            if (!hiddenBossBar.contains(player)) {
                AltariseAPI.get().getMinecraftProvider().getBossBarManager().hideBar(player);
                hiddenBossBar.add(player);
            }
            return;
        }
        hiddenBossBar.remove(player);
        String   checkEnd = Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCheckpoint()+1 == Parkour.getInstance().getGameProperties().getCheckpoints().size() ? "Arrivée" : "Prochain Checkpoint";
        Location locEnd   = Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCheckpoint()+1 == Parkour.getInstance().getGameProperties().getCheckpoints().size() ? Parkour.getInstance().getGameProperties().getEndLocation() : Parkour.getInstance().getGameProperties().getCheckpoints().get(Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCheckpoint()+1);
        AltariseAPI.get().getMinecraftProvider().getBossBarManager().setMessage(player, "§f" + checkEnd + ": §e" + LocationUtils.getDistance(player, locEnd) + " §6" + LocationUtils.getDirection(player, locEnd));
    }


    private void updateLocation(Player player) {
        if(player.getLocation().getBlockY() <= Parkour.getInstance().getGameProperties().getVoidY()) {
            Parkour.getInstance().getGameManager().teleport(player);
        }

        if(Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCheckpoint() == -1) {
            if(LocationUtils.getAroundLocations(Parkour.getInstance().getGameProperties().getStartLocation(), 2).contains(player.getLocation().getBlock().getLocation())) {
                Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).setCheckpoint(0);
                Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getStopWatch().start();
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                Parkour.getInstance().getGameManager().startGame(player);
            }
        }
        else if(Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCheckpoint() >= 0 && Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCheckpoint() < Parkour.getInstance().getGameProperties().getCheckpoints().size()) {
            if(LocationUtils.getAroundLocations(Parkour.getInstance().getGameProperties().getCheckpoints().get(Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCheckpoint() + 1), 2).contains(player.getLocation().getBlock().getLocation())) {
                Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).setCheckpoint(Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCheckpoint() + 1);
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                new ActionBar("§a Checkpoint ! ").sendToPlayer(player);
            }
        }
        else if(LocationUtils.getAroundLocations(Parkour.getInstance().getGameProperties().getEndLocation(), 2).contains(player.getLocation().getBlock().getLocation())) {

            Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getStopWatch().stop();
            final long time = Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getStopWatch().getTime();

            player.sendMessage(" ");
            player.sendMessage("       §fParkour §6" + Parkour.getInstance().getGameProperties().getName() + " §fterminé en §6" + Parkour.getInstance().getGameManager().getFormatedDuration(time) +"§f!");
            player.sendMessage(" ");
            player.sendMessage("         §eCarte §b» §6" + Parkour.getInstance().getGameProperties().getName());
            player.sendMessage("         §eDifficulté §b» §6" + Parkour.getInstance().getGameProperties().getDifficulty());
            player.sendMessage("         §eMeilleur §b» §6" + (time < Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getBestTimer() ? "§aNOUVEAU §6" + Parkour.getInstance().getGameManager().getFormatedDuration(time) : Parkour.getInstance().getGameManager().getFormatedDuration(Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getBestTimer())));
            player.sendMessage(" ");

            Parkour.getInstance().getGameManager().endGame(player, true);
        }


    }

    private void updateBlock(Player player) {
            if(Parkour.getInstance().getGameManager().getPlayer(player.getUniqueId()).getCheckpoint() == -1) return;
            if(LocationUtils.getAroundLocations(Parkour.getInstance().getGameProperties().getSpawn(), 5).contains(player.getLocation().getBlock().getLocation())) return;
            if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) return;
            if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.LADDER)) return;
            if(player.getLocation().getBlock().getRelative(BlockFace.UP).getType().equals(Material.LADDER)) return;
            if(player.getLocation().getBlock().getRelative(BlockFace.SELF).getType().equals(Material.LADDER)) return;
            if(player.getLocation().getBlock().getType().equals(Material.LADDER)) return;
            if(player.getLocation().subtract(0, 0.1, 0).getBlock().getType().equals(Material.VINE)) return;
            if(!player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.STAINED_CLAY)) {

                Parkour.getInstance().getGameManager().teleport(player);
                return;
            }


            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            if(!Parkour.getInstance().getGameProperties().getStandableClays().contains(block.getData())) {
                Parkour.getInstance().getGameManager().teleport(player);
            }




    }



}
