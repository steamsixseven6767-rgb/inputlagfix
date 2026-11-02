package dev.zeroinputlag.mixin;

import dev.zeroinputlag.SnapTap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin {
    @Inject(method = "isPressed", at = @At("RETURN"), cancellable = true)
    private void zeroinputlag$snapTap(CallbackInfoReturnable<Boolean> cir) {
        if (!SnapTap.active || SnapTap.raw) return;
        GameOptions o = MinecraftClient.getInstance().options;
        if (o == null) return;
        KeyBinding self = (KeyBinding) (Object) this;
        if (self == o.forwardKey) cir.setReturnValue(SnapTap.forward);
        else if (self == o.backKey) cir.setReturnValue(SnapTap.back);
        else if (self == o.leftKey) cir.setReturnValue(SnapTap.left);
        else if (self == o.rightKey) cir.setReturnValue(SnapTap.right);
    }
}
