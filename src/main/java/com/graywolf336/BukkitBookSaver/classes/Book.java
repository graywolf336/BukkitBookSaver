package com.graywolf336.BukkitBookSaver.classes;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.BookMeta;

public class Book {
    private String title, author;
    private ArrayList<String> pages;
    
    public Book() {
        this.pages = new ArrayList<String>();
    }
    
    public Book(String title) {
        this.title = title;
        this.pages = new ArrayList<String>();
    }
    
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.pages = new ArrayList<String>();
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getAuthor() {
        return this.author;
    }
    
    public void addPage(String page) {
        this.pages.add(page);
    }
    
    public String removePage(int index) {
        String page = this.pages.get(index);
        this.pages.remove(index);
        return page;
    }
    
    public ArrayList<String> getPages() {
        return this.pages;
    }
    
    public boolean worthyOfBeingSaved() {
        return !this.title.isEmpty() && !this.author.isEmpty() && !this.pages.isEmpty();
    }
    
    public static Book fromBookMetadata(BookMeta data) throws Exception {
        if (!data.hasAuthor() || !data.hasTitle() || !data.hasPages()) {
            throw new Exception("Invalid BookMeta data.");
        }
        
        Book b = new Book();
        b.setTitle(ChatColor.stripColor(data.getTitle()));
        b.setAuthor(ChatColor.stripColor(data.getAuthor()));
        
        for (String p : data.getPages()) {
            b.addPage(ChatColor.stripColor(p));
        }
        
        return b;
    }
}
