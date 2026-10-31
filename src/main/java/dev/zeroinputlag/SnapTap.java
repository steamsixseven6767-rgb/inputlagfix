package dev.zeroinputlag;

/** SOCD "last pressed wins" resolver for opposite movement keys. */
public final class SnapTap {
    public static final Axis VERTICAL = new Axis();
    public static final Axis HORIZONTAL = new Axis();

    private SnapTap() {}

    public static final class Axis {
        private boolean prevA;
        private boolean prevB;
        private boolean winA;

        /** Returns 1 if A wins, -1 if B wins, 0 if neither is held. */
        public int resolve(boolean a, boolean b) {
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
