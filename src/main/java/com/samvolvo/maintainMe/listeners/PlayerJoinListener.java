package com.samvolvo.maintainMe.listeners;

import com.samvolvo.maintainMe.MaintainMe;
import com.samvolvo.maintainMe.methods.KickMethod;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
            plugin.getKickMethod().kick(event.getPlayer());
        }
    }

    @EventHandler
    public void onStaffJoin(PlayerJoinEvent event){
        Player player = e.getPlayer();
        if (player.isOp() || player.hasPermission("prefixpro.version")){
            String message = plugin.getUpdateChecker().generateUpdateMessageColored(plugin.getDescription().getVersion());
            if (message != null || !message.isEmpty()){
                player.sendMessage(message);
            }
        }
    }
}
