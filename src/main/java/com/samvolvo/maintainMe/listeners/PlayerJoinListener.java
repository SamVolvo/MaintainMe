package com.samvolvo.maintainMe.listeners;

import com.samvolvo.maintainMe.MaintainMe;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.isMaintenanceMode() && !event.getPlayer().hasPermission("maintainme.join")) {
            event.setQuitMessage(null);
        }
    }

    @EventHandler
    public void onStaffJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (player.isOp() || player.hasPermission("maintainme.version")){
            String message = plugin.getUpdateChecker().generateUpdateMessageColored(plugin.getDescription().getVersion());
            if (message != null && !message.isEmpty()){
                player.sendMessage(message);
            }
        }
    }
}
