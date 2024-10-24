package com.samvolvo.maintainme.nms_1_19_4;

import com.samvolvo.maintainme.MaintainMe;
import com.samvolvo.maintainme.listeners.AbstractPlayerJoinListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener extends AbstractPlayerJoinListener {
    public PlayerJoinListener(MaintainMe plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.isMaintenanceMode() && !event.getPlayer().hasPermission("maintainme.join")){
            plugin.getNmsHandler().kick(event.getPlayer(), plugin);
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
