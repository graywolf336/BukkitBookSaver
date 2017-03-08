package com.graywolf336.BukkitBookSaver.enums;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

/**
 * Represents the settings for the Mailbox plugin.
 *
 * @author graywolf336
 * @since 2.0.0
 * @version 1.0.0
 */
public enum Settings {
    /** Whether we should be outputting debugging information. */
    DEBUG("system.debug"),
    /** Whether we should save as json or not, aka json or flatfile. */
    JSON("system.save-as-json");

    private static Plugin pl;
    private String path;

    private Settings(String path) {
        this.path = path;
    }

    public boolean asBoolean() {
        return pl.getConfig().getBoolean(path);
    }

    public int asInt() {
        return pl.getConfig().getInt(path);
    }

    public byte asByte() {
        return Byte.valueOf(pl.getConfig().getString(path)).byteValue();
    }

    public String asString() {
        return pl.getConfig().getString(path);
    }

    public List<String> asStringList() {
        return pl.getConfig().getStringList(path);
    }

    public Location asLocation() {
        return (Location) pl.getConfig().get(path);
    }

    public void setAndSave(Object obj) {
        pl.getConfig().set(path, obj);
        pl.saveConfig();
    }

    public static void setPlugin(Plugin plugin) {
        pl = plugin;
    }
}
