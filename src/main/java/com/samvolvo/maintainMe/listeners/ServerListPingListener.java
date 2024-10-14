package com.samvolvo.maintainMe.listeners;

import com.samvolvo.maintainMe.MaintainMe;
import org.bukkit.ChatColor;
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
        if (maintenanceMode){
            if (plugin.getConfig().getBoolean("motd.maintenanceMode.centered")){
                event.setMotd(plugin.getMotdTools().getMOTD(plugin.getConfig().getString("motd.maintenanceMode.line1"), plugin.getConfig().getString("motd.maintenanceMode.line2")));
            }else{
                event.setMotd(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("motd.maintenanceMode.line1")+ "\n" + plugin.getConfig().getString("motd.maintenanceMode.line2")));
            }
            event.setMaxPlayers(0);
        }else{
            if (plugin.getConfig().getBoolean("motd.normal.centered")){
                if (plugin.getConfig().getString("motd.normal.line1").isEmpty() && plugin.getConfig().getString("motd.normal.line2").isEmpty()){
                    event.setMotd(plugin.getServer().getMotd());
                }else{
                    event.setMotd(plugin.getMotdTools().getMOTD(plugin.getConfig().getString("motd.normal.line1"), plugin.getConfig().getString("motd.normal.line2")));
                }
            }else{
                if (plugin.getConfig().getString("motd.normal.line1").isEmpty() && plugin.getConfig().getString("motd.normal.line2").isEmpty()){
                    event.setMotd(plugin.getServer().getMotd());
                }else{
                    event.setMotd(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("motd.normal.line1") + "\n" + plugin.getConfig().getString("motd.normal.line2")));
                }
            }
            event.setMaxPlayers(plugin.getServer().getMaxPlayers());
        }
    }
}
