package dev.zeroinputlag.mixin;

import dev.zeroinputlag.SnapTap;
import dev.zeroinputlag.ZilConfig;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void zeroinputlag$snapTap(CallbackInfo ci) {
        ZilConfig cfg = ZilConfig.get();
        if (!cfg.enabled || !cfg.snapTap) return;
        KeyboardInput self = (KeyboardInput) (Object) this;
        PlayerInput in = self.playerInput;
        int fwd = SnapTap.VERTICAL.resolve(in.forward(), in.backward());
        int side = SnapTap.HORIZONTAL.resolve(in.left(), in.right());
        self.playerInput = new PlayerInput(fwd > 0, fwd < 0, side > 0, side < 0,
                in.jump(), in.sneak(), in.sprint());
        self.movementForward = fwd;
        self.movementSideways = side;
    }
}
