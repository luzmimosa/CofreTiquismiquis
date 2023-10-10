package com.fadedbytes.chest;

import com.fadedbytes.data.LootTableSelector;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import static com.fadedbytes.CofreTiquismiquis.LOGGER;

public class BiomedChestContent {

    private static LootTableSelector selector = null;
    private static LootTableSelector hotSwapSelector = null;
    public static Identifier getLootTableForBiome(String biomeKey, Random random) {
        Identifier lootTable = null;

        if (selector != null) {
            lootTable = selector.lootTableFromBiome(biomeKey, random);
        }

        if (hotSwapSelector != null) {
            Identifier hotTable = hotSwapSelector.lootTableFromBiome(biomeKey, random);
            if (!hotTable.toString().equals("minecraft:undefined")) {
                lootTable = hotTable;
            }
        }

        return lootTable;
    }

    public static void setSelector(LootTableSelector selector) {
        BiomedChestContent.selector = selector;
    }

    public static void processChest(
            ChestBlockEntity chestEntity,
            BlockPos position,
            World world
    ) {
        UsableContainer usableContainer = (UsableContainer) chestEntity;

        if (!usableContainer.isUsed()) {

            String biomeKey = world.getBiome(position).getKey().orElseThrow().getValue().toString();
            LOGGER.debug("Biome key: " + biomeKey);

            Identifier lootTable = BiomedChestContent.getLootTableForBiome(biomeKey, world.getRandom());

            LootableContainerBlockEntity.setLootTable(world, world.getRandom(), position, lootTable);
            usableContainer.setUsed(true);
        }
    }

    public static void setHotSwapSelector(LootTableSelector hotSwapSelector) {
        BiomedChestContent.hotSwapSelector = hotSwapSelector;
    }

}
