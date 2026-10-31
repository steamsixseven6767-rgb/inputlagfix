package dev.zeroinputlag.mixin;

import dev.zeroinputlag.InputTuning;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    /** Poll immediately before the tick consumes key/button presses. */
    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void zeroinputlag$pollBeforeInput(CallbackInfo ci) {
        InputTuning.pollNow();
    }
}
