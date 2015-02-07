package cz.kricraft.ic2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import cz.kricraft.ic2.utils.MaterialData;

public class DataStorage {

    private static final String path = "plugins/CraftujIC2/config.yml";
    public List<MaterialData> lockableBlocks;
    public List<MaterialData> bannedBlocks;
    public List<MaterialData> bannedItems;

    public void loadConfig() {
        File file = new File(path);
        YamlConfiguration config = loadFile(file);
        lockableBlocks = loadList(config, "lockableBlocks");
        bannedBlocks = loadList(config, "bannedBlocks");
        bannedItems = loadList(config, "bannedItems");
    }

    public List<MaterialData> loadList(YamlConfiguration config, String path) {
        List<String> rawList = config.getStringList(path);
        List<MaterialData> list = new ArrayList<>();
        rawList.stream().filter((entry) -> !(entry.equals("id:subid"))).forEach((entry) -> {
            try {
                MaterialData md;
                String[] data = entry.split("\\:");
                if (data.length >= 2) {
                    md = new MaterialData(Integer.parseInt(data[0]),
                            Integer.parseInt(data[1]));
                } else if (data.length >= 1) {
                    md = new MaterialData(Integer.parseInt(data[0]));
                } else {
                    throw new IllegalArgumentException("Nespravne ID veci '" + entry + "'!");
                }
                list.add(md);

            } catch (Exception e) {
                Main.info("Error when loading list '" + path
                        + "' from config. (" + entry + ")");
            }
        });
        return list;
    }

    public YamlConfiguration loadFile(File file) {
        boolean create = !file.exists();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (create) {
            List<String> rawList = new ArrayList<>();
            rawList.add("id:subid");
            config.set("lockableBlocks", rawList);
            config.set("bannedBlocks", rawList);
            config.set("bannedItems", rawList);
            trySaveYaml(config, file);
        }
        return config;
    }

    public static void trySaveYaml(YamlConfiguration yaml, File file) {
        try {
            yaml.save(file);
        } catch (IOException e) {
            Main.warn("Error when saving file '" + file.getName() + "'.");
        }
    }
}
