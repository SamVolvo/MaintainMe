package com.samvolvo.maintainMe.listeners;

import com.samvolvo.maintainMe.MaintainMe;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final MaintainMe plugin;
    public PlayerJoinListener(MaintainMe plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.isMaintenanceMode() && !event.getPlayer().hasPermission("maintainme.join")){
            event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', "" +
                            "&a&lMaintainMe" +
                    "\n" +
                    "\n" +
                    "&bWe are currently under maintenance.\n" +
                    "&dWe will be back soon!"
            ));
        }
    }
}
