package dev.zeroinputlag;

/** Lightweight counters for the overlay. Not a true end-to-end latency measurement. */
public final class Metrics {
    /** Incremented from the mouse callback. */
    public static long mouseEvents;
    /** Late polls that delivered fresh mouse events since the last second boundary. */
    public static int hits;

    public static int hitsPerSec;
    public static double frameMs;
    public static double frameAvgMs;
    public static double worstMs;

    private static long lastFrame;
    private static long windowStart;
    private static double windowWorst;

    private Metrics() {}

    public static void onFrame() {
        long now = System.nanoTime();
        if (lastFrame != 0) {
            frameMs = (now - lastFrame) / 1_000_000.0;
            frameAvgMs = frameAvgMs == 0 ? frameMs : frameAvgMs * 0.95 + frameMs * 0.05;
            windowWorst = Math.max(windowWorst, frameMs);
        }
        lastFrame = now;
        if (now - windowStart >= 1_000_000_000L) {
            worstMs = windowWorst;
            windowWorst = 0;
            hitsPerSec = hits;
            hits = 0;
            windowStart = now;
        }
    }
}
