package com.fadedbytes;

import com.fadedbytes.chest.BiomedChestContent;
import com.fadedbytes.data.LootTableSelector;
import com.fadedbytes.data.LootTableSelectorDeserializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CofreTiquismiquis implements ModInitializer {
	public static final String MOD_ID = "cofretiquismiquis";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		/*
		 "By all laws of programming, you should not be able to instantiate an interface in java,
		 its methods are too abstract to call.
		 However, modders don't care about what actual programmers think so we will do it anyway."

		  -- Fabric documentation
		 */
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return new Identifier(MOD_ID, "loot_table_selector");
			}

			@Override
			public void reload(ResourceManager manager) {
				manager.findResources(
						"loot_table_selector",
						identifier -> identifier.getPath().endsWith("chest_biome.json")
				).forEach((identifier, resource) -> {
					LOGGER.debug("Found loot table selector: " + identifier.toString());

					try {
						String json = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
						LootTableSelector lootTableSelector = LootTableSelectorDeserializer.readLootTableSelector(json);

						BiomedChestContent.setSelector(lootTableSelector);
					} catch (IOException e) {
						LOGGER.error("Error reading loot table selector: " + identifier.toString());
					}
				});
			}
		});

	}
}