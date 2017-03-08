package com.graywolf336.BukkitBookSaver;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.graywolf336.BukkitBookSaver.cmds.SaveBookCommand;
import com.graywolf336.BukkitBookSaver.enums.Settings;

public class BukkitBookSaverMain extends JavaPlugin {
    private File savesFolder;
	private int count;
	
	public void onEnable() {
	    Settings.setPlugin(this);
		this.getLogger().info("Saving all the books, one book at a time!");
		this.count = 0;
		this.getDataFolder().mkdirs();
		
		this.savesFolder = new File(this.getDataFolder(), "books");
		this.savesFolder.mkdirs();
		
		SaveBookCommand cmd = new SaveBookCommand(this);
		this.getCommand("save-book").setExecutor(cmd);
		this.getCommand("save-book").setTabCompleter(cmd);
	}
	
	public void onDisable() {
		this.getLogger().info("Saved " + this.count + " books this go around! " + (this.count > 0 ? "Yay!!" : "Awww :("));
		this.count = 0;
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
