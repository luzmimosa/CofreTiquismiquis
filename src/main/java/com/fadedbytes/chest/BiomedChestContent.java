package com.fadedbytes.chest;

import com.fadedbytes.data.LootTableSelector;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class BiomedChestContent {

    private static LootTableSelector selector = null;
    public static Identifier getLootTableForBiome(String biomeKey, Random random) {
        if (selector == null) return null;

        return selector.lootTableFromBiome(biomeKey, random);
    }

    public static void setSelector(LootTableSelector selector) {
        BiomedChestContent.selector = selector;
    }

}
