package com.graywolf336.BukkitBookSaver.cmds;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.graywolf336.BukkitBookSaver.BukkitBookSaverMain;

public class SaveBooksInChestCommand implements CommandExecutor {
    private BukkitBookSaverMain pl;
    
    public SaveBooksInChestCommand(BukkitBookSaverMain plugin) {
        this.pl = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a Player since we can't see what you're look at, sorry bud!");
            return true;
        }
        
        if (sender.hasPermission("book-save")) {
        	sender.sendMessage(ChatColor.RED + "You are not allowed to do this action of which you have committed to doing!");
        	return true;
        }
        
        Player p = (Player) sender;
        Block target = p.getTargetBlock(null, 10);
        
        if (target.getType() != Material.CHEST && target.getType() != Material.TRAPPED_CHEST) {
            sender.sendMessage(ChatColor.RED + "The block you're looking at must be a Chest or Trapped Chest.");
            return true;
        }
        
        Chest chest = (Chest) target.getState();
        
        for(ItemStack i : chest.getInventory()) {
            if (!this.pl.getSaver().isValidBook(i)) {
                continue;
            }
            
            this.pl.getSaver().saveBook(p, i);
        }
        
        return true;
    }
}
