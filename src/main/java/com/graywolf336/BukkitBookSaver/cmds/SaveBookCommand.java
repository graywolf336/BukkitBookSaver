package com.graywolf336.BukkitBookSaver.cmds;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.graywolf336.BukkitBookSaver.BukkitBookSaverMain;

public class SaveBookCommand implements CommandExecutor, TabCompleter {
    private BukkitBookSaverMain pl;

    public SaveBookCommand(BukkitBookSaverMain plugin) {
        this.pl = plugin;
        
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a Player since you don't have an inventory for me to use, sorry bud!");
            return true;
        }
        
        if (sender.hasPermission("book-save")) {
        	sender.sendMessage(ChatColor.RED + "You are not allowed to do this action of which you have committed to doing!");
        	return true;
        }
        
        Player p = (Player) sender;
        ItemStack i = p.getInventory().getItemInMainHand();
        
        if(!this.pl.getSaver().isValidBook(i)) {
            p.sendMessage(ChatColor.RED + "You must have a written book in hand.");
            return true;
        }
        
        pl.getSaver().saveBook(p, i);

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
