package com.samvolvo.maintainme.utils;

import com.samvolvo.maintainme.MaintainMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {
    private final MaintainMe plugin;


    public Logger(MaintainMe plugin){
        this.plugin = plugin;
    }

    public void error(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[ERROR] &7[" + plugin.getName() +"]: &r" + message));
    }

    public void debug(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&b[DEBUG] &7[" + plugin.getName() + "]: &r" + message));
    }

    public void warning(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[WARNING] &7[" + plugin.getName() + "]: &r" + message));
    }

    public void loading(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&9[LOADING] &7[" + plugin.getName() + "]: &r" + message));
    }

    public void info(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[INFO] &7[" + plugin.getName() + "]: &r" + message));
    }

    public void succes(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a[SUCCESS] &7[" + plugin.getName() + "]: &r" + message));
    }
}
