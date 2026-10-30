package dev.zeroinputlag.mixin;

import dev.zeroinputlag.InputTuning;
import net.minecraft.client.Mouse;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    /** Poll right before the camera consumes the accumulated mouse delta; force raw/unsmoothed. */
    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void zeroinputlag$beforeUpdate(double timeDelta, CallbackInfo ci) {
        InputTuning.enforceRawMouse(MinecraftClient.getInstance());
        InputTuning.pollNow();
    }
}
