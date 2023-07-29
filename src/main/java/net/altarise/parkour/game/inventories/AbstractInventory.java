package net.altarise.parkour.game.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class AbstractInventory implements InventoryHolder, Listener {


    private final String name;
    private  int size = 9*6;
    private final Inventory inventory;
    private final Map<Integer, Consumer<InventoryClickEvent>> itemHandlers  = new HashMap<>();
    private final List<Consumer<InventoryCloseEvent>> closeHandlers = new ArrayList<>();

    private final List<Integer> silentSlots = new ArrayList<>();



    public AbstractInventory(String name) {
        this.name = name;
        this.inventory = Bukkit.createInventory(this, size, name);

    }

    public AbstractInventory(String name, int size) {
        this.name = name;
        this.size = size;
        this.inventory = Bukkit.createInventory(this, size, name);

    }
    public AbstractInventory() {
        this.name = "default";
        this.inventory = Bukkit.createInventory(this, size, name);
    }


    public void updateItem(int slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }


    public void addCloseHandler(Consumer<InventoryCloseEvent> handler) {
        closeHandlers.add(handler);
    }

    public void setItem(int slot, ItemStack it, boolean isSilent) {
        setItem(slot, it);
        if(isSilent) {
            silentSlots.add(slot);
        }
    }


    public void setItem(int slot, ItemStack it) {
        setItem(slot, it, null);
    }


    public void setItem(int slot, ItemStack it, Consumer<InventoryClickEvent> handler, boolean isSilent) {
        setItem(slot, it, handler);
        if(isSilent) {
            silentSlots.add(slot);
        }
    }
    public void setItem(int slot, ItemStack it, Consumer<InventoryClickEvent> eventConsumer) {
        this.inventory.setItem(slot, it);
        if(eventConsumer != null) {
            itemHandlers.put(slot, eventConsumer);
        }
    }

    public void setItems(int slot1, int slot2, ItemStack it) {
        setItems(slot1, slot2, it, null);
    }

    public void setItems(int slot1, int slot2, ItemStack it, Consumer<InventoryClickEvent> eventConsumer) {
        for(int i = slot1; i <= slot2; i++) {
            setItem(i, it, eventConsumer);
        }
    }

    public void setSilent(int slot) {
        silentSlots.add(slot);
    }



    void handle(InventoryClickEvent event) {
        Consumer<InventoryClickEvent> consumer = itemHandlers.get(event.getRawSlot());

        if(consumer != null) {
            consumer.accept(event);
        }
    }




    void handleClose(InventoryCloseEvent event) {
        this.closeHandlers.forEach(handler -> handler.accept(event));
    }



    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }


        @EventHandler
        public void inventoryHandle(InventoryClickEvent event) {
            if(event.getClickedInventory() == null) return;
            if(!(event.getClickedInventory().getHolder() instanceof AbstractInventory)) return;
            if(event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                event.setCancelled(true);
            }
            Player player = (Player) event.getWhoClicked();

            event.setCancelled(true);
            AbstractInventory inventory = (AbstractInventory) event.getInventory().getHolder();
            inventory.handle(event);
            if(!inventory.silentSlots.contains(event.getRawSlot()) && event.getCurrentItem().getType() != null && event.getCurrentItem().getType() != Material.AIR) {
                player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
            }
        }

        @EventHandler
        public void inventoryClose(InventoryCloseEvent event) {
            if(event.getInventory() == null) return;
            if(!(event.getInventory().getHolder() instanceof AbstractInventory)) return;
            Player player = (Player) event.getPlayer();
            AbstractInventory inventory = (AbstractInventory) event.getInventory().getHolder();
            inventory.handleClose(event);
        }



}
