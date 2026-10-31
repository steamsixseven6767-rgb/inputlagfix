package dev.zeroinputlag.mixin;

import dev.zeroinputlag.InputTuning;
import dev.zeroinputlag.Metrics;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void zeroinputlag$beforeUpdate(double timeDelta, CallbackInfo ci) {
        InputTuning.enforceRawMouse(MinecraftClient.getInstance());
        InputTuning.pollNow();
    }

    @Inject(method = "onCursorPos", at = @At("HEAD"))
    private void zeroinputlag$onCursor(long window, double x, double y, CallbackInfo ci) {
        Metrics.mouseEvents++;
    }
}
