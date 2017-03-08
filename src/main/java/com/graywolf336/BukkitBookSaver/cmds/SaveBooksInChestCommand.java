package com.graywolf336.BukkitBookSaver.cmds;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

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
        
        Player p = (Player) sender;
        Block target = p.getTargetBlock((HashSet<Byte>) null, 100);
        
        if (target.getType() != Material.CHEST || target.getType() != Material.TRAPPED_CHEST) {
            sender.sendMessage(ChatColor.RED + "The block you're looking at must be a Chest or Trapped Chest.");
            return true;
        }
        
        Chest chest = (Chest) target.getState();
        
        for(ItemStack i : chest.getInventory()) {
            if (!this.pl.getSaver().isValidBook(i)) {
                continue;
            }
            
            this.pl.getSaver().saveBook(p, (BookMeta) i.getItemMeta());
        }
        
        return true;
    }
}
