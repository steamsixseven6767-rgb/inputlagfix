package dev.zeroinputlag;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

/** Shared helpers used by the mixins. */
public final class InputTuning {
    private static boolean polling;

    private InputTuning() {}

    /**
     * Drain pending OS input events right now, so the game consumes the freshest
     * mouse/keyboard state instead of data queued since the last end-of-frame poll.
     */
    public static void pollNow() {
        if (polling || !RenderSystem.isOnRenderThread()) return;
        polling = true;
        try {
            GLFW.glfwPollEvents();
        } finally {
            polling = false;
        }
    }

    /** Make the mouse raw and 1:1: no cinematic smoothing, OS-level raw motion on. */
    public static void enforceRawMouse(MinecraftClient client) {
        if (client == null || client.options == null) return;
        client.options.smoothCameraEnabled = false;
        var raw = client.options.getRawMouseInput();
        if (!raw.getValue()) {
            raw.setValue(true);
        }
    }
}
