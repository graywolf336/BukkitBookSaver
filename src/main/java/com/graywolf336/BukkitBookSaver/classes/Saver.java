package com.graywolf336.BukkitBookSaver.classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    	if (Settings.DEBUG.asBoolean()) {
    		this.pl.getLogger().info("Is item not null: " + (item != null));
    		this.pl.getLogger().info("Is item type written book: " + (item.getType() == Material.WRITTEN_BOOK) + " " + item.getType().toString());
    		this.pl.getLogger().info("Does item has meta data: " + (item.hasItemMeta()));
    		this.pl.getLogger().info("Item item meta data book meta: " + (item.getItemMeta() instanceof BookMeta));
    	}

        return item != null && item.getType() == Material.WRITTEN_BOOK && item.hasItemMeta() && item.getItemMeta() instanceof BookMeta;
    }
    
    public boolean saveBook(CommandSender sender, ItemStack i) {
        Book book;
        
        try {
        	BookMeta bm = (BookMeta) i.getItemMeta();
            book = Book.fromBookMetadata(bm);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "The book in your hand isn't a valid book for some reason.");
            return false;
        }
        
        String ext = Settings.JSON.asBoolean() ? ".json" : ".txt";
        File f = new File(this.pl.getSavesFolder(), book.getAuthor() + "-" + book.getTitle().replaceAll(" ", "_") + ext);
        
        if (f.exists()) {
            if (sender.isOp()) {
                f.delete();
            } else {
                sender.sendMessage(ChatColor.DARK_AQUA + "The book \"" + book.getAuthor() + "-" + book.getTitle() + "\" was already saved to file.");
                return false;
            }
        }
        
        if (Settings.JSON.asBoolean()) {
            try {
            	OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f), Charset.forName("UTF-8"));
            	osw.write(this.objectMapper.writeValueAsString(book));
            	osw.close();
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "Failure! " + e.getClass().getSimpleName());
                return false;
            }
        } else {
            try {
                f.createNewFile();
                BufferedWriter b = new BufferedWriter(new PrintWriter(f));
                
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
                sender.sendMessage(ChatColor.RED + "Failure! " + e.getClass().getSimpleName());
                return false;
            }
        }
        
        if (Settings.SERIALIZED.asBoolean()) {
        	File sfile = new File(this.pl.getSerializedFolder(), book.getAuthor() + "-" + book.getTitle().replaceAll(" ", "_") + ".yml");
        	
        	if (sfile.exists()) {
                if (sender.isOp()) {
                	sfile.delete();
                } else {
                    sender.sendMessage(ChatColor.DARK_AQUA + "The book \"" + book.getAuthor() + " - " + book.getTitle() + "\" was already saved to file.");
                    return false;
                }
            }
        	
            FileConfiguration sf = YamlConfiguration.loadConfiguration(sfile);
            sf.set("book", i);
            
            try {
				sf.save(sfile);
			} catch (IOException e) {
				e.printStackTrace();
				sender.sendMessage(ChatColor.RED + "Failure! " + e.getClass().getSimpleName());
                return false;
			}
        }
        
        
        this.pl.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Successfully wrote \"" + book.getAuthor() + "-" + book.getTitle() + "\" to file.");
        sender.sendMessage(ChatColor.GREEN + "Successfully wrote \"" + book.getAuthor() + "-" + book.getTitle() + "\" to file.");
        this.pl.incrementCount();
        
        return true;
    }
    
    public ArrayList<String> getSeralizedBooks() {
    	ArrayList<String> books = new ArrayList<String>();

    	for(File f : this.pl.getSerializedFolder().listFiles()) {
    		if (f.isFile()) {
    			books.add(f.getName());
    		}
    	}
    	
    	return books;
    }
    
    public ItemStack getSeralizedBook(String name) throws Exception {
    	File sfile = new File(this.pl.getSerializedFolder(), name);
    	
    	if (!sfile.exists()) {
    		throw new Exception("Invalid book name, it wasn't saved");
        }
    	
        FileConfiguration sf = YamlConfiguration.loadConfiguration(sfile);
        
        return sf.getItemStack("book");
    }
    
    public ArrayList<String> getJsonBooks() {
    	ArrayList<String> books = new ArrayList<String>();

    	for(File f : this.pl.getSavesFolder().listFiles()) {
    		if (f.isFile() && f.getName().endsWith(".json")) {
    			books.add(f.getName());
    		}
    	}
    	
    	return books;
    }
    
    public ItemStack getJsonBook(String name) throws Exception {
    	File sfile = new File(this.pl.getSavesFolder(), name);
    	
    	if (!sfile.exists() || sfile.isDirectory()) {
    		throw new Exception("Invalid book name, it wasn't saved");
        }

    	return this.objectMapper.readValue(sfile, Book.class).toItem();
    }
}
