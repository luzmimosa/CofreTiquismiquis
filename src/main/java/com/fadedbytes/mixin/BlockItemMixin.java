package com.fadedbytes.mixin;

import com.fadedbytes.chest.UsableContainer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.fadedbytes.CofreTiquismiquis.LOGGER;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;decrement(I)V"
            ),
            method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;"
    )
    private void place(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());

        // No comprobamos si el jugador está en supervivencia porque el método decrement() solo se llama tras haberlo comprobado

        if (blockEntity == null) return;

        if (blockEntity instanceof ChestBlockEntity chest) {
            ((UsableContainer) chest).setUsed(true);
        }
    }
}
