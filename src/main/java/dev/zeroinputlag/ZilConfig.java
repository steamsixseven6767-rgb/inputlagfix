package dev.zeroinputlag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Tiny JSON config: .minecraft/config/zeroinputlag.json */
public final class ZilConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ZilConfig instance;

    public boolean enabled = true;
    public boolean latePolling = true;
    public boolean rawMouse = true;
    public boolean renderLoopInput = true;

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("zeroinputlag.json");
    }

    public static ZilConfig get() {
        if (instance == null) load();
        return instance;
    }

    public static void load() {
        try {
            Path p = path();
            if (Files.exists(p)) {
                instance = GSON.fromJson(Files.readString(p), ZilConfig.class);
            }
        } catch (Exception e) {
            ZeroInputLagClient.LOGGER.warn("Could not read config, using defaults", e);
        }
        if (instance == null) instance = new ZilConfig();
    }

    public static void save() {
        try {
            Files.writeString(path(), GSON.toJson(get()));
        } catch (IOException e) {
            ZeroInputLagClient.LOGGER.warn("Could not save config", e);
        }
    }
}
