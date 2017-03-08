package com.graywolf336.BukkitBookSaver.cmds;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graywolf336.BukkitBookSaver.BukkitBookSaverMain;
import com.graywolf336.BukkitBookSaver.classes.Book;
import com.graywolf336.BukkitBookSaver.enums.Settings;

public class SaveBookCommand implements CommandExecutor, TabCompleter {
    private BukkitBookSaverMain pl;
    private ObjectMapper objectMapper;

    public SaveBookCommand(BukkitBookSaverMain plugin) {
        this.pl = plugin;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a Player since you don't have an inventory for me to use, sorry bud!");
            return true;
        }
        
        Player p = (Player) sender;
        
        if(p.getItemInHand() == null || p.getItemInHand().getType() != Material.WRITTEN_BOOK || !p.getItemInHand().hasItemMeta() || !(p.getItemInHand().getItemMeta() instanceof BookMeta)) {
            p.sendMessage(ChatColor.RED + "You must have a written book in hand.");
            return true;
        }

        Book book;
        
        try {
            BookMeta bm = (BookMeta)p.getItemInHand().getItemMeta();
            book = Book.fromBookMetadata(bm);
        } catch (Exception e) {
            p.sendMessage(ChatColor.RED + "The book in your hand isn't a valid book for some reason.");
            return true;
        }
        
        if (Settings.JSON.asBoolean()) {
            File f = new File(this.pl.getSavesFolder(), book.getAuthor() + "-" + book.getTitle() + ".json");
            
            try {
                f.createNewFile();
                BufferedWriter b = new BufferedWriter(new FileWriter(f));
                b.write(this.objectMapper.writeValueAsString(book));
                b.close();
            } catch (Exception e) {
                e.printStackTrace();
                p.sendMessage(ChatColor.RED + "Failure! " + e.getClass().getSimpleName());
                return true;
            }
        } else {
            File f = new File(this.pl.getSavesFolder(), book.getAuthor() + "-" + book.getTitle() + ".txt");

            try {
                f.createNewFile();
                BufferedWriter b = new BufferedWriter(new FileWriter(f));
                
                b.write("-------------- INFO --------------");
                b.newLine();
                b.write("Author: " + book.getAuthor());
                b.write("Title: " + book.getTitle());
                b.newLine();

                int page = 1;
                for(String s : book.getPages()) {
                    b.write("-------------- PAGE " + page + " --------------");
                    b.newLine();
                    b.write(s);
                    b.newLine();
                    page++;
                }
                b.close();
            } catch(Exception e) {
                e.printStackTrace();
                p.sendMessage(ChatColor.RED + "Failure! " + e.getClass().getSimpleName());
                return true;
            }
        }
        
        this.pl.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Successfully wrote \"" + book.getAuthor() + "-" + book.getTitle() + "\" to file.");
        p.sendMessage(ChatColor.GREEN + "Successfully wrote \"" + book.getAuthor() + "-" + book.getTitle() + "\" to file.");
        this.pl.incrementCount();

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
