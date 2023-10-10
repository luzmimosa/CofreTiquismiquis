package com.fadedbytes;

import com.fadedbytes.chest.BiomedChestContent;
import com.fadedbytes.data.Configuration;
import com.fadedbytes.data.LootTableSelector;
import com.fadedbytes.data.LootTableSelectorDeserializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static net.minecraft.server.command.CommandManager.literal;

public class CofreTiquismiquis implements ModInitializer {
	public static final String MOD_ID = "cofretiquismiquis";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private Configuration config;

	@Override
	public void onInitialize() {

		this.config = new Configuration();

		this.registerChestlootCommand();
		this.registerDataReloader();

		this.config = new Configuration();
		this.reloadHotSwapData();

	}

	private void registerChestlootCommand() {
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register(
				literal("chestloot")
						.executes(context -> {
							context.getSource().sendMessage(Text.literal("§cUsa /chestloot reload para recargar las loot tables de cofres."));

							return 0;
						})
						.requires(source -> source.hasPermissionLevel(3))
						.then(literal("reload")
								.executes(context -> {
									context.getSource().sendMessage(Text.literal("§6Recargando loot tables de cofres..."));
									this.reloadHotSwapData();

									return 1;
								})
						)
		)));
	}

	private void registerDataReloader() {
		/*
		 "By all laws of programming, you should not be able to instantiate an interface in java,
		 its methods are too abstract to call.
		 However, modders don't care about what actual programmers think, so we will do it anyway."

		  -- Fabric documentation
		 */

		SimpleSynchronousResourceReloadListener lootTableSelectorLoader = new SimpleSynchronousResourceReloadListener() {
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

					try (InputStream stream = resource.getInputStream()) {
						String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
						LootTableSelector lootTableSelector = LootTableSelectorDeserializer.readLootTableSelector(json);

						BiomedChestContent.setSelector(lootTableSelector);
					} catch (IOException e) {
						LOGGER.error("Error reading loot table selector: " + identifier);
					}
				});
			}
		};

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(lootTableSelectorLoader);
	}

	public void reloadHotSwapData() {
		LootTableSelector lootTableSelector = this.config.readHotSwappedLootTableSelector();
		BiomedChestContent.setHotSwapSelector(lootTableSelector);
	}
}