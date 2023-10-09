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
    public static Identifier getLootTableForBiome(String biomeKey, Random random) {
        if (selector == null) return null;

        return selector.lootTableFromBiome(biomeKey, random);
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

}
