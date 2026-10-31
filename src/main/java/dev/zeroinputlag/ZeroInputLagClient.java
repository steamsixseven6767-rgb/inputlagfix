package dev.zeroinputlag;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** ZeroInputLag: client-only input latency reduction. No gameplay assists. */
public final class ZeroInputLagClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ZeroInputLag");
    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        ZilConfig.load();

        // F8 is unused by vanilla 1.21.4; rebindable in Controls.
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.zeroinputlag.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F8, "category.zeroinputlag"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                ZilConfig cfg = ZilConfig.get();
                cfg.enabled = !cfg.enabled;
                ZilConfig.save();
                if (client.player != null) {
                    client.player.sendMessage(
                            Text.literal("ZeroInputLag: " + (cfg.enabled ? "ON" : "OFF")), true);
                }
            }
        });

        LOGGER.info("ZeroInputLag loaded.");
    }
}
