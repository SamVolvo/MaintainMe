package com.samvolvo.maintainme.nms_1_18_2;

import com.samvolvo.maintainme.MaintainMe;
import com.samvolvo.maintainme.listeners.AbstractServerListPingListener;
import com.samvolvo.maintainme.utils.MOTD;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener extends AbstractServerListPingListener {
    private final MaintainMe plugin;
    private final MOTD motdUtil;

    public ServerListPingListener(MaintainMe plugin){
        this.plugin = plugin;
        motdUtil = plugin.getMotdUtil();
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event){
        String line1;
        String line2;
        if (plugin.isMaintenanceMode()){
            line1 = plugin.getConfig().getString("motd.maintenance.line1");
            line2 = plugin.getConfig().getString("motd.maintenance.line2");
            event.setMaxPlayers(0);
        }else{
            line1 = plugin.getConfig().getString("motd.normal.line1");
            line2 = plugin.getConfig().getString("motd.normal.line2");
            event.setMaxPlayers(plugin.getServer().getMaxPlayers());
        }

        if (line1 == null || line1.isEmpty()){
            line1 = "A Minecraft Server";
        }

        event.setMotd(motdUtil.getMOTD(line1, line2));
    }
}
