package com.fadedbytes.mixin;

import com.fadedbytes.chest.BiomedChestContent;
import com.fadedbytes.chest.UsableContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.fadedbytes.CofreTiquismiquis.LOGGER;


@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin {
    @Shadow public abstract BlockEntityType<? extends ChestBlockEntity> getExpectedEntityType();

    @Inject(
            at = @At("HEAD"),
            method = "onUse"
    )
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!world.isClient) {
            ChestBlockEntity chestEntity = this.getExpectedEntityType().get(world, pos);
            assert chestEntity != null;

            UsableContainer usableContainer = (UsableContainer) chestEntity;

            if (!usableContainer.isUsed()) {

                String biomeKey = world.getBiome(pos).getKey().orElseThrow().getValue().toString();
                LOGGER.debug("Biome key: " + biomeKey);

                Identifier lootTable = BiomedChestContent.getLootTableForBiome(biomeKey, world.getRandom());

                LootableContainerBlockEntity.setLootTable(world, world.getRandom(), pos, lootTable);
                usableContainer.setUsed(true);
            }
        }
    }

}
