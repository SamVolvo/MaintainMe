package com.samvolvo.maintainMe.listeners;

import com.samvolvo.maintainMe.MaintainMe;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener implements Listener {
    private final MaintainMe plugin;

    public ServerListPingListener(MaintainMe plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event){
        boolean maintenanceMode = plugin.isMaintenanceMode();
        plugin.getSamVolvoLogger().debug("Listener triggered. Mode is " + maintenanceMode);
        if (maintenanceMode){
            plugin.getSamVolvoLogger().debug("maintenanceMode is on.");
            event.setMotd(plugin.getMotdTools().getMOTD("§aServer is in maintenance mode.", "§bPlease check back later!"));
        }else{
            plugin.getSamVolvoLogger().debug("maintenanceMode is off.");
            event.setMotd(plugin.getMotdTools().getMOTD("§eServer is online", "§dJoin now!"));
        }
    }
}
