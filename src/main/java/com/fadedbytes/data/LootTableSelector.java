package com.fadedbytes.data;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import java.util.HashMap;
import java.util.List;

public class LootTableSelector {

    private final HashMap<String, List<Identifier>> lootTablesByBiome;
    private final Identifier defaultLootTable;

    public LootTableSelector(HashMap<String, List<Identifier>> lootTablesByBiome, Identifier defaultLootTable) {
        this.lootTablesByBiome = lootTablesByBiome;
        this.defaultLootTable = defaultLootTable;
    }

    public Identifier lootTableFromBiome(String biome, Random random) {
        if (lootTablesByBiome.containsKey(biome)) {
            List<Identifier> lootTables = lootTablesByBiome.get(biome);

            if (!lootTables.isEmpty()) {
                return lootTables.get(random.nextBetween(0, lootTables.size() - 1));
            } else {
                return new Identifier("minecraft", "empty");
            }
        }

        return defaultLootTable;
    }

}
