package com.graywolf336.BukkitBookSaver.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.graywolf336.BukkitBookSaver.BukkitBookSaverMain;

public class ReloadBookSaverCommand implements CommandExecutor {
    private BukkitBookSaverMain pl;

    public ReloadBookSaverCommand(BukkitBookSaverMain plugin) {
        this.pl = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You are not allowed to do this action of which you have committed to doing!");
            return true;
        }
        
        this.pl.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Book Saver configuration reloaded.");
        
        return true;
    }
}
