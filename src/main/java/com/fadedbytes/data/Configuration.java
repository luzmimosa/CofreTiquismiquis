package com.fadedbytes.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.fadedbytes.CofreTiquismiquis.MOD_ID;
import static com.fadedbytes.CofreTiquismiquis.LOGGER;

public class Configuration {

    private static final String LOOT_TABLE_SELECTOR_FILE = "chest_biome.json";

    public synchronized LootTableSelector readHotSwappedLootTableSelector() {
        createFileStructure();

        try (FileInputStream inputStream = new FileInputStream(getLootTableFile())) {
            String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return LootTableSelectorDeserializer.readLootTableSelector(json);
        } catch (IOException exception) {
            LOGGER.error("Error reading chest_biome.json hot file", exception);
            return null;
        }
    }

    private void createFileStructure() {
        File dataFolder = new File("./mods", MOD_ID);
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File lootTablesFolder = new File(dataFolder, LOOT_TABLE_SELECTOR_FILE);
        if (!lootTablesFolder.exists()) {
            try {
                lootTablesFolder.createNewFile();

                FileOutputStream outputStream = new FileOutputStream(lootTablesFolder);
                byte[] strToBytes = "{}".getBytes();
                outputStream.write(strToBytes);
                outputStream.close();
            } catch (Exception e) {
                LOGGER.error("Error creating chest_biome.json file: " + e.getMessage());
            }
        }
    }

    private File getLootTableFile() {
        return new File("./mods/" + MOD_ID + "/" + LOOT_TABLE_SELECTOR_FILE);
    }

}
