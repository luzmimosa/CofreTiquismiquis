package com.fadedbytes.data;

import com.fadedbytes.data.model.BiomeLootTables;
import com.fadedbytes.data.model.LootTableSelectorModel;
import com.google.gson.Gson;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;

public class LootTableSelectorDeserializer {

    public static LootTableSelector readLootTableSelector(String json) {
        return fromModel(new Gson().fromJson(json, LootTableSelectorModel.class));
    }

    private static LootTableSelector fromModel(LootTableSelectorModel model) {

        HashMap<String, List<Identifier>> biomeLootTables = new HashMap<>();

        for (BiomeLootTables biomeLootTable : model.biomes) {
            biomeLootTables.put(
                    biomeLootTable.biome,
                    biomeLootTable.lootTables
                            .stream()
                            .map(Identifier::new)
                            .toList()
            );
        }

        Identifier defaultLootTable = new Identifier(model.defaultLootTable);

        return new LootTableSelector(
            biomeLootTables,
            defaultLootTable
        );
    }

}
