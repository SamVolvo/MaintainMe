package com.samvolvo.maintainme.nms_1_17_1;

import com.samvolvo.maintainme.MaintainMe;
import com.samvolvo.maintainme.listeners.AbstractServerListPingListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener extends AbstractServerListPingListener {
    private final MaintainMe plugin;

    public ServerListPingListener(MaintainMe plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event){
        boolean maintenanceMode = plugin.isMaintenanceMode();
        if (maintenanceMode){
            if (plugin.getConfig().getBoolean("motd.maintenance.centered")){
                event.setMotd(plugin.getMotdUtil().getMOTD(plugin.getConfig().getString("motd.maintenanceMode.line1"), plugin.getConfig().getString("motd.maintenanceMode.line2")));
            }else{
                event.setMotd(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("motd.maintenance.line1")+ "\n" + plugin.getConfig().getString("motd.maintenance.line2")));
            }
            event.setMaxPlayers(0);
        }else{
            if (plugin.getConfig().getBoolean("motd.normal.centered")){
                if (plugin.getConfig().getString("motd.normal.line1").isEmpty() && plugin.getConfig().getString("motd.normal.line2").isEmpty()){
                    event.setMotd(plugin.getServer().getMotd());
                }else{
                    event.setMotd(plugin.getMotdUtil().getMOTD(plugin.getConfig().getString("motd.normal.line1"), plugin.getConfig().getString("motd.normal.line2")));
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