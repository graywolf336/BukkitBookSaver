package com.graywolf336.BukkitBookSaver.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.graywolf336.BukkitBookSaver.BukkitBookSaverMain;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LoadSerializedBookCommand implements CommandExecutor {
	private BukkitBookSaverMain pl;
	
	public LoadSerializedBookCommand(BukkitBookSaverMain plugin) {
		this.pl = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("book-load") || !(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You are not allowed to do this action of which you have committed to doing!");
            return true;
		}
		
		if (args.length == 0) {
			sender.sendMessage(ChatColor.GREEN + "List of Seralized Books:");
			for(String s : this.pl.getSaver().getSeralizedBooks()) {
				TextComponent msg = new TextComponent();
				msg.setColor(ChatColor.DARK_AQUA);
				msg.setText(" - " + s);
				msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/books-load-serialized " + s));
				msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Loads the book!").create()));

				sender.spigot().sendMessage(msg);
			}
			return true;
		}
		
		try {
			Player p = (Player) sender;
			ItemStack i = this.pl.getSaver().getSeralizedBook(args[0]);
			
			p.getInventory().addItem(i);
			sender.sendMessage(ChatColor.DARK_GREEN + "Successfully loaded " + args[0] + "!");
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage(ChatColor.RED + "Failure! " + e.getClass().getSimpleName());
		}
		
		return true;
	}

}
