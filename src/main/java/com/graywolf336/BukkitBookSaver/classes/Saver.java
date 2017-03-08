package com.graywolf336.BukkitBookSaver.classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graywolf336.BukkitBookSaver.BukkitBookSaverMain;
import com.graywolf336.BukkitBookSaver.enums.Settings;

public class Saver {
    private BukkitBookSaverMain pl;
    private ObjectMapper objectMapper;
    
    public Saver(BukkitBookSaverMain plugin) {
        this.pl = plugin;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    public boolean isValidBook(ItemStack item) {
        return item != null && item.getType() == Material.WRITTEN_BOOK && !item.hasItemMeta() || item.getItemMeta() instanceof BookMeta;
    }
    
    public boolean saveBook(Player p, BookMeta data) {
        Book book;
        
        try {
            book = Book.fromBookMetadata(data);
        } catch (Exception e) {
            p.sendMessage(ChatColor.RED + "The book in your hand isn't a valid book for some reason.");
            return false;
        }
        
        String ext = Settings.JSON.asBoolean() ? ".json" : ".txt";
        File f = new File(this.pl.getSavesFolder(), book.getAuthor() + "-" + book.getTitle() + ext);
        
        if (f.exists()) {
            if (p.isOp()) {
                f.delete();
            } else {
                p.sendMessage(ChatColor.DARK_AQUA + "The book \"" + book.getAuthor() + "-" + book.getTitle() + "\" was already saved to file.");
                return false;
            }
        }
        
        if (Settings.JSON.asBoolean()) {
            try {
                f.createNewFile();
                BufferedWriter b = new BufferedWriter(new FileWriter(f));
                b.write(this.objectMapper.writeValueAsString(book));
                b.close();
            } catch (Exception e) {
                e.printStackTrace();
                p.sendMessage(ChatColor.RED + "Failure! " + e.getClass().getSimpleName());
                return false;
            }
        } else {
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
                return false;
            }
        }
        
        this.pl.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Successfully wrote \"" + book.getAuthor() + "-" + book.getTitle() + "\" to file.");
        p.sendMessage(ChatColor.GREEN + "Successfully wrote \"" + book.getAuthor() + "-" + book.getTitle() + "\" to file.");
        this.pl.incrementCount();
        
        return true;
    }
}
