package com.samvolvo.maintainMe.methods;

import com.samvolvo.maintainMe.MaintainMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MaintenanceMethod {
    private final MaintainMe plugin;

    public MaintenanceMethod(MaintainMe plugin){
        this.plugin = plugin;
    }

    public void enableMaintenance(){
        /// Stop the maintenanceTimer if running
        if (plugin.getScheduledMaintenanceTask() != null){
            plugin.stopMaintenanceTimer();
        }
        /// Turn on maintenance mode.
        plugin.setMaintenanceMode(true);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()){
            if (!onlinePlayer.hasPermission("maintainme.join")){
                plugin.getKickMethod().kick(onlinePlayer);
            }else{
                onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',  plugin.getConfig().getString("prefix") + "&7: &eMaintenance mode is enabled!"));
            }
        }
    }

    public void disableMaintenance(){
        /// Disable maintenance mode
        plugin.setMaintenanceMode(false);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()){
            onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: &eMaintenance mode is disabled!"));
        }
    }

    public void scheduleMaintenance(int timeInMinutes){

    }

}
