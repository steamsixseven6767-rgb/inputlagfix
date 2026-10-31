package dev.zeroinputlag;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

/** Shared helpers used by the mixins. */
public final class InputTuning {
    private static boolean polling;

    private InputTuning() {}

    /** Drain pending OS input events now so the game sees the freshest state. */
    public static void pollNow() {
        ZilConfig cfg = ZilConfig.get();
        if (!cfg.enabled || !cfg.latePolling) return;
        if (polling || !RenderSystem.isOnRenderThread()) return;
        polling = true;
        long before = Metrics.mouseEvents;
        try {
            GLFW.glfwPollEvents();
        } finally {
            polling = false;
        }
        if (Metrics.mouseEvents != before) Metrics.hits++;
    }

    /** Raw, unsmoothed mouse. */
    public static void enforceRawMouse(MinecraftClient client) {
        ZilConfig cfg = ZilConfig.get();
        if (!cfg.enabled || !cfg.rawMouse) return;
        if (client == null || client.options == null) return;
        client.options.smoothCameraEnabled = false;
        var raw = client.options.getRawMouseInput();
        if (!raw.getValue()) {
            raw.setValue(true);
        }
    }

    /**
     * Ask for no VSync: turns the vanilla option off and requests swap interval 0.
     * Whether this takes effect depends on the platform (on Android the surface/compositor may still force it).
     */
    public static void enforceVsyncOff(MinecraftClient client) {
        ZilConfig cfg = ZilConfig.get();
        if (!cfg.enabled || !cfg.disableVsync) return;
        if (client == null || client.options == null) return;
        var vsync = client.options.getEnableVsync();
        if (vsync.getValue()) {
            vsync.setValue(false);
        }
        try {
            GLFW.glfwSwapInterval(0);
        } catch (Throwable ignored) {
            // not supported on this platform
        }
    }
}
