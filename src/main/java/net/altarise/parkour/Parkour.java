package net.altarise.parkour;

import net.altarise.parkour.game.GameListener;
import net.altarise.parkour.game.GameManager;
import net.altarise.parkour.game.ItemFactory;
import net.altarise.parkour.game.inventories.AbstractInventory;
import net.altarise.parkour.game.properties.GameProperties;
import net.altarise.parkour.game.properties.Loader;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Parkour extends JavaPlugin {

    private static Parkour instance;
    private GameManager gameManager;

    public static HashMap<String, GameProperties> get = new HashMap<>();


    @Override
        public void onEnable() {
            super.onEnable();

            saveDefaultConfig();
            instance = this;
            new Loader().make();
            gameManager = new GameManager();
            getServer().getPluginManager().registerEvents(new ItemFactory.FactoryListener(), this);
            getServer().getPluginManager().registerEvents(new GameListener(), this);
            getServer().getPluginManager().registerEvents(new AbstractInventory(), this);

            Bukkit.getWorld("world").setDifficulty(Difficulty.PEACEFUL);
            Bukkit.getWorld("world").setGameRuleValue("randomtickspeed", "0");

        }


        public static Parkour getInstance() {
            return instance;
        }

        public GameManager getGameManager() {
        return gameManager;
    }

        public GameProperties getGameProperties() {
        return get.get("a");
    }


}
