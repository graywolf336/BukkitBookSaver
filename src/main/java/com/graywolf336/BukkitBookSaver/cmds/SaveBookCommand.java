package com.graywolf336.BukkitBookSaver.cmds;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

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
        
        Player p = (Player) sender;
        
        if(this.pl.getSaver().isValidBook(p.getItemInHand())) {
            p.sendMessage(ChatColor.RED + "You must have a written book in hand.");
            return true;
        }
        
        pl.getSaver().saveBook(p, (BookMeta)p.getItemInHand().getItemMeta());

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
