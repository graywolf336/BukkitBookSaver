package com.graywolf336.BukkitBookSaver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitBookSaverMain extends JavaPlugin {
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(p.getItemInHand() != null && p.getItemInHand().getType() == Material.WRITTEN_BOOK && p.getItemInHand().hasItemMeta()) {
				BookMeta bm = (BookMeta)p.getItemInHand().getItemMeta();
				
				if(bm.hasPages()) {
					File f = new File(this.getDataFolder(), ChatColor.stripColor(bm.getDisplayName()) + ".txt");
					
					try {
						BufferedWriter b = new BufferedWriter(new FileWriter(f));
						
						b.newLine();
						for(String s : bm.getPages()) {
							b.write(s);
							b.newLine();
						}
						b.close();
						
						this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Successfully wrote " + ChatColor.stripColor(bm.getDisplayName()) + " to file.");
						p.sendMessage(ChatColor.GREEN + "Successfully wrote " + ChatColor.stripColor(bm.getDisplayName()) + " to file.");
					}catch(Exception e) {
						e.printStackTrace();
						p.sendMessage(ChatColor.RED + "Failure! " + e.getClass().getSimpleName());
					}
				}
			}else {
				p.sendMessage(ChatColor.RED + "Written book must be in hand.");
			}
			
			return true;
		}
		
		return false;
	}
}
