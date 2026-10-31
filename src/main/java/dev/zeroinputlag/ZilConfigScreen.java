package dev.zeroinputlag;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

/** Simple toggle screen opened from Mod Menu. */
public class ZilConfigScreen extends Screen {
    private final Screen parent;

    public ZilConfigScreen(Screen parent) {
        super(Text.literal("ZeroInputLag"));
        this.parent = parent;
    }

    private static Text label(String name, boolean on) {
        return Text.literal(name + ": " + (on ? "ON" : "OFF"));
    }

    private void toggle(int y, String name, Supplier<Boolean> get, Consumer<Boolean> set) {
        int w = 220;
        addDrawableChild(ButtonWidget.builder(label(name, get.get()), b -> {
            set.accept(!get.get());
            b.setMessage(label(name, get.get()));
            ZilConfig.save();
        }).dimensions(this.width / 2 - w / 2, y, w, 20).build());
    }

    @Override
    protected void init() {
        ZilConfig c = ZilConfig.get();
        int y = 36;
        int step = 22;
        toggle(y, "Mod enabled", () -> c.enabled, v -> c.enabled = v);
        toggle(y + step, "Late input polling", () -> c.latePolling, v -> c.latePolling = v);
        toggle(y + step * 2, "Raw mouse (no smoothing)", () -> c.rawMouse, v -> c.rawMouse = v);
        toggle(y + step * 3, "Force VSync off", () -> c.disableVsync, v -> c.disableVsync = v);
        toggle(y + step * 4, "Latency overlay", () -> c.overlay, v -> c.overlay = v);
        toggle(y + step * 5, "SnapTap (risky!)", () -> c.snapTap, v -> c.snapTap = v);
        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
                .dimensions(this.width / 2 - 100, y + step * 6 + 6, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 16, 0xFFFFFF);
    }

    @Override
    public void close() {
        ZilConfig.save();
        if (this.client != null) this.client.setScreen(parent);
    }
}
