package dev.zeroinputlag;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** ZeroInputLag: client-only input latency reduction. */
public final class ZeroInputLagClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ZeroInputLag");
    private static KeyBinding toggleKey;
    private static KeyBinding overlayKey;
    private static int tickCounter;

    @Override
    public void onInitializeClient() {
        ZilConfig.load();

        // F8 / F9 are unused by vanilla 1.21.4; rebindable in Controls.
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.zeroinputlag.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F8, "category.zeroinputlag"));
        overlayKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.zeroinputlag.overlay", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F9, "category.zeroinputlag"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ZilConfig cfg = ZilConfig.get();
            while (toggleKey.wasPressed()) {
                cfg.enabled = !cfg.enabled;
                ZilConfig.save();
                message(client, "ZeroInputLag: " + (cfg.enabled ? "ON" : "OFF"));
            }
            while (overlayKey.wasPressed()) {
                cfg.overlay = !cfg.overlay;
                ZilConfig.save();
            }
            if (++tickCounter >= 20) {
                tickCounter = 0;
                InputTuning.enforceVsyncOff(client);
            }
        });

        HudRenderCallback.EVENT.register((context, tickCounterRef) -> {
            Metrics.onFrame();
            ZilConfig cfg = ZilConfig.get();
            if (!cfg.enabled || !cfg.overlay) return;
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.options.hudHidden) return;
            int fps = Metrics.frameAvgMs > 0 ? (int) Math.round(1000.0 / Metrics.frameAvgMs) : 0;
            String[] lines = {
                    String.format("FPS %d | frame %.1f ms | worst %.1f ms", fps, Metrics.frameMs, Metrics.worstMs),
                    "Late-poll hits/s: " + Metrics.hitsPerSec,
                    "VSync option: " + (mc.options.getEnableVsync().getValue() ? "ON" : "OFF")
                            + " | Raw mouse: " + (mc.options.getRawMouseInput().getValue() ? "ON" : "OFF"),
                    "SnapTap: " + (cfg.snapTap ? "ON" : "OFF")
            };
            int y = 4;
            for (String line : lines) {
                context.drawTextWithShadow(mc.textRenderer, line, 4, y, 0xFFFFFF);
                y += 10;
            }
        });

        LOGGER.info("ZeroInputLag loaded.");
    }

    private static void message(MinecraftClient client, String text) {
        if (client.player != null) {
            client.player.sendMessage(Text.literal(text), true);
        }
    }
}
