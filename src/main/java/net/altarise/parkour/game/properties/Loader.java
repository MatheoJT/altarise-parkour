package net.altarise.parkour.game.properties;


import net.altarise.parkour.Parkour;

import java.io.File;

public class Loader {

    private void deserialize(String fileName) {
        File file = new File(Parkour.getInstance().getDataFolder() + File.separator + "maps", fileName + ".yml");
        if(!file.exists()) throw new IllegalArgumentException("File " + "maps/" + fileName + ".yml does not exist");

        GameProperties gameProperties = new GameProperties(file);

        Parkour.get.put(fileName, gameProperties);
    }

    public void make() {
        for(File file : new File(Parkour.getInstance().getDataFolder().getAbsolutePath() + File.separator + "maps").listFiles()) {
                    deserialize(file.getName().replace(".yml", ""));
        }
    }
}
