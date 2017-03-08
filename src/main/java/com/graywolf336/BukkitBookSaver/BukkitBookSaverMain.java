package com.graywolf336.BukkitBookSaver;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.graywolf336.BukkitBookSaver.cmds.ReloadBookSaverCommand;
import com.graywolf336.BukkitBookSaver.cmds.SaveBookCommand;
import com.graywolf336.BukkitBookSaver.enums.Settings;

public class BukkitBookSaverMain extends JavaPlugin {
    private File savesFolder;
	private int count;
	
	public void onEnable() {
	    this.loadConfig();
	    Settings.setPlugin(this);

		this.count = 0;
		this.getDataFolder().mkdirs();
		
		this.savesFolder = new File(this.getDataFolder(), "books");
		this.savesFolder.mkdirs();
		
		SaveBookCommand cmd = new SaveBookCommand(this);
		this.getCommand("save-book").setExecutor(cmd);
		this.getCommand("save-book").setTabCompleter(cmd);
		this.getCommand("reload-book-saver").setExecutor(new ReloadBookSaverCommand(this));

		this.getLogger().info("Saving all the books, one book at a time!");
	}
	
	public void onDisable() {
		this.getLogger().info("Saved " + this.count + " books this go around! " + (this.count > 0 ? "Yay!!" : "Awww :("));
		this.count = 0;
	}
	
    private void loadConfig() {
        //Only create the default config if it doesn't exist
        saveDefaultConfig();

        //Append new key-value pairs to the config
        getConfig().options().copyDefaults(true);

        //Now save it since we've potentationally changed things
        saveConfig();
    }
	
	public File getSavesFolder() {
	    return this.savesFolder;
	}
	
	public int getSavedCount() {
	    return this.count;
	}
	
	public int incrementCount() {
	    return this.count++;
	}
}
