package net.altarise.parkour.game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.Consumer;

public class ItemFactory {

    ItemStack itemStack;


    public ItemFactory(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemFactory handle(Consumer<PlayerInteractEvent> handler) {
        FactoryListener.factoryConsumers.put(itemStack, handler);
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }


    public static class FactoryListener implements Listener {

        public static HashMap<ItemStack, Consumer<PlayerInteractEvent>> factoryConsumers = new HashMap<>();


        @EventHandler
        public void onInteract(PlayerInteractEvent event) {
            if (factoryConsumers.containsKey(event.getItem())) {
                factoryConsumers.get(event.getItem()).accept(event);
            }
        }


    }

}
