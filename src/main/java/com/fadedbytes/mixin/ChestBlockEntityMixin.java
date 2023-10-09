package com.fadedbytes.mixin;

import com.fadedbytes.chest.UsableContainer;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin implements UsableContainer {

    @Unique
    private boolean used;

    @Override
    public boolean isUsed() {
        return this.used;
    }

    @Override
    public void setUsed(boolean used) {
        this.used = used;
    }

    @Inject(at = @At("TAIL"), method = "writeNbt")
    public void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("used", this.used);
    }

    @Inject(at = @At("TAIL"), method = "readNbt")
    public void readNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("used")) {
            this.used = nbt.getBoolean("used");
        } else {
            this.used = false;
        }
    }

}
