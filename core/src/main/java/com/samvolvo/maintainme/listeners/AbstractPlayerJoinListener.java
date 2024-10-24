package com.samvolvo.maintainme.listeners;

import com.samvolvo.maintainme.MaintainMe;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AbstractPlayerJoinListener implements Listener {
    public final MaintainMe plugin;

    public AbstractPlayerJoinListener(MaintainMe plugin){
        this.plugin = plugin;
    }
}
