package dev.zeroinputlag;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

/** Shared helpers used by the mixins. */
public final class InputTuning {
    /** True only while the extra per-frame handleInputEvents() call is running. */
    public static boolean frameRun;
    private static boolean polling;

    private InputTuning() {}

    /** Drain pending OS input events now so the game sees the freshest state. */
    public static void pollNow() {
        ZilConfig cfg = ZilConfig.get();
        if (!cfg.enabled || !cfg.latePolling) return;
        if (polling || !RenderSystem.isOnRenderThread()) return;
        polling = true;
        try {
            GLFW.glfwPollEvents();
        } finally {
            polling = false;
        }
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
}
