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
            if (plugin.getConfig.getBoolean("motd.maintenance.centered")){
                event.setMotd(plugin.getMotdTools().getMOTD(plugin.getConfig.getString("motd.maintenance.line1"), plugin.getConfig.getString("motd.maintenance.line2")));
            }else{
                event.setMotd(plugin.getConfig.getString("motd.maintenance.line1")+ "\n" + plugin.getConfig.getString("motd.maintenance.line2"));
            }
            event.setMaxPlayers(0);
        }else{
            plugin.getSamVolvoLogger().debug("maintenanceMode is off.");
            if (plugin.getConfig.getBoolean("motd.normal.centered")){
                if (plugin.getConfig.getString("motd.normal.line1").isEmpty() && plugin.getConfig.getString("motd.normal.line2").isEmpty()){
                    event.setMotd(plugin.getServer().getMotd());
                }else{
                    event.setMotd(plugin.getMotdTools().getMOTD(plugin.getConfig.getString("motd.normal.line1"), plugin.getConfig.getString("motd.normal.line2")));
                }
            }else{
                if (plugin.getConfig.getString("motd.normal.line1").isEmpty() && plugin.getConfig.getString("motd.normal.line2").isEmpty()){
                    event.setMotd(plugin.getServer().getMotd());
                }else{
                    event.setMotd(plugin.getConfig.getString("motd.normal.line1") + "\n" + plugin.getConfig.getString("motd.normal.line2"));
                }
            }
            event.setMaxPlayers(plugin.getServer().getMaxPlayers());
        }
    }
}
