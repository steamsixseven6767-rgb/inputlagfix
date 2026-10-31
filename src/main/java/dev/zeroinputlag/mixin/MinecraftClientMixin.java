package dev.zeroinputlag.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.zeroinputlag.InputTuning;
import dev.zeroinputlag.ZilConfig;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    protected abstract void handleInputEvents();

    /** Poll immediately before key/button presses are consumed. */
    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void zeroinputlag$pollBeforeInput(CallbackInfo ci) {
        InputTuning.pollNow();
    }

    /**
     * Render-loop input: once per frame (in-game, no screen) consume discrete input
     * (clicks, hotbar, swap, drop, ...) instead of waiting for the next 20 TPS tick.
     * Continuous block breaking stays tick-driven (see the wrapper below).
     */
    @Inject(method = "render", at = @At("HEAD"))
    private void zeroinputlag$frameInput(boolean tick, CallbackInfo ci) {
        ZilConfig cfg = ZilConfig.get();
        if (!cfg.enabled || !cfg.renderLoopInput) return;
        MinecraftClient self = (MinecraftClient) (Object) this;
        if (self.world == null || self.player == null || self.interactionManager == null
                || self.currentScreen != null || self.getOverlay() != null || !self.isWindowFocused()) {
            return;
        }
        InputTuning.pollNow();
        InputTuning.frameRun = true;
        try {
            handleInputEvents();
        } finally {
            InputTuning.frameRun = false;
        }
    }

    /** Block-break progress must advance per tick, not per frame. */
    @WrapWithCondition(method = "handleInputEvents",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;handleBlockBreaking(Z)V"))
    private boolean zeroinputlag$skipBreakingInFrameRun(MinecraftClient instance, boolean breaking) {
        return !InputTuning.frameRun;
    }
}
