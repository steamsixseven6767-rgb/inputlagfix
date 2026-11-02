package dev.zeroinputlag;

import net.minecraft.client.MinecraftClient;

/** SOCD "last pressed wins" resolver for opposite movement keys. */
public final class SnapTap {
    private static final Axis VERTICAL = new Axis();
    private static final Axis HORIZONTAL = new Axis();

    /** True while resolved values below should replace raw key state. */
    public static boolean active;
    /** True while we read the raw key state ourselves. */
    public static boolean raw;
    public static boolean forward, back, left, right;

    private SnapTap() {}

    /** Called once at the start of every client tick, before the player reads input. */
    public static void update(MinecraftClient client) {
        ZilConfig cfg = ZilConfig.get();
        if (!cfg.enabled || !cfg.snapTap || client.options == null) {
            active = false;
            return;
        }
        boolean f, b, l, r;
        raw = true;
        try {
            f = client.options.forwardKey.isPressed();
            b = client.options.backKey.isPressed();
            l = client.options.leftKey.isPressed();
            r = client.options.rightKey.isPressed();
        } finally {
            raw = false;
        }
        int v = VERTICAL.resolve(f, b);
        int h = HORIZONTAL.resolve(l, r);
        forward = v > 0;
        back = v < 0;
        left = h > 0;
        right = h < 0;
        active = true;
    }

    private static final class Axis {
        private boolean prevA;
        private boolean prevB;
        private boolean winA;

        /** Returns 1 if A wins, -1 if B wins, 0 if neither is held. */
        int resolve(boolean a, boolean b) {
            if (a && !prevA) winA = true;
            if (b && !prevB) winA = false;
            prevA = a;
            prevB = b;
            if (a && b) return winA ? 1 : -1;
            if (a) return 1;
            if (b) return -1;
            return 0;
        }
    }
}
