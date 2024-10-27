package com.samvolvo.maintainme.nms_1_21_2;

import com.samvolvo.maintainme.MaintainMe;
import com.samvolvo.maintainme.listeners.AbstractServerListPingListener;
import com.samvolvo.maintainme.utils.MOTD;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;

public class ServerListPingListener extends AbstractServerListPingListener {
    private final MaintainMe plugin;
    private final MOTD motdUtil;

    public ServerListPingListener(MaintainMe plugin){
        this.plugin = plugin;
        motdUtil = plugin.getMotdUtil();
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event){
        if (!plugin.getConfig().getBoolean("motd.enabled")){
            return;
        }

        if (getMotd(plugin.getConfig()) == null){
            plugin.getSamVolvoLogger().error("No motd found!");
            return;
        }

        List<String> motd = getMotd(plugin.getConfig());

        String message = "";

        for (String line : motd){
            message += ChatColor.translateAlternateColorCodes('&', line + "\n");
        }

        event.setMotd(message);
        setMaxPlayers(event);
    }

    private List<String> getMotd(FileConfiguration config){
        List<String> motd = null;
        if (plugin.isMaintenanceMode()){
            motd = config.getStringList("motd.maintenance.motd");
        }else{
            motd = config.getStringList("motd.normal.motd");
        }
        return motd;
    }

    private void setMaxPlayers(ServerListPingEvent event){
        if (plugin.isMaintenanceMode()){
            event.setMaxPlayers(0);
        }else{
            event.setMaxPlayers(plugin.getServer().getMaxPlayers());
        }
    }
}
