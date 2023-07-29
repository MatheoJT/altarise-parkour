package net.altarise.parkour.game.inventories;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.parkour.Parkour;
import net.altarise.parkour.game.GameDifficulty;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class PkChooser extends AbstractInventory {


        public PkChooser(Player player, GameDifficulty difficulty) {
            super("§eChoix du §6§lParkour", 9*6);
            final ItemStack grayBorder = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).displayname(" ").flag(ItemFlag.HIDE_ATTRIBUTES).build();
            final ItemStack limeBorder = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5)).displayname(" ").flag(ItemFlag.HIDE_ATTRIBUTES).build();
            final ItemStack orangeBorder = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1)).displayname(" ").flag(ItemFlag.HIDE_ATTRIBUTES).build();
            final ItemStack redBorder = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14)).displayname(" ").flag(ItemFlag.HIDE_ATTRIBUTES).build();

            final ItemBuilder filter = new ItemBuilder(new ItemStack(Material.HOPPER)).displayname("§6§lFiltrer par difficultée:").flag(ItemFlag.HIDE_ATTRIBUTES);
            filter.lore(Arrays.stream(GameDifficulty.values()).map(value -> (value == difficulty ? value.getChatColor() + "> " : "§7") + value.getChatColor() + value.getDisplayName()).toArray(String[]::new));

            setItems(0, 8 , grayBorder);
            setItem(1, limeBorder);
            setItem(2, orangeBorder);
            setItem(3, redBorder);
            setItem(5, redBorder);
            setItem(6, orangeBorder);
            setItem(7, limeBorder);
            setItems(45, 53, grayBorder);

            setItem(49, filter.build(), event -> {
                player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
                new PkChooser(player, difficulty == GameDifficulty.DEFAULT ? GameDifficulty.EASY : GameDifficulty.values()[difficulty.getId() + 1]);
                updateItem(49, filter.build());
                player.updateInventory();
            });

            int startIndex = 9;
            final int[] i = {startIndex};
            Parkour.get.keySet().stream().filter(map -> {
                if (difficulty == GameDifficulty.DEFAULT) {
                    return true;
                } else {
                    return Parkour.get.get(map).getDifficulty() == difficulty;
                }
            }).forEach(map -> {
                final ItemBuilder item = new ItemBuilder(new ItemStack(Material.PAPER)).displayname("§6§l" + map).flag(ItemFlag.HIDE_ATTRIBUTES);
                item.lore("§7Cliquez pour rejoindre ce §6§lParkour§7.");
                setItem(i[0], item.build(), event -> {
                    player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
                    player.closeInventory();
                });
                i[0]++;
            });



        }
}
