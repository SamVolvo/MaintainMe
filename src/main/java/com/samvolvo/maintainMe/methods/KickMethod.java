package com.samvolvo.maintainMe.methods;

import com.samvolvo.maintainMe.MaintainMe;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KickMethod {
    private MaintainMe plugin;

    public KickMethod(MaintainMe plugin){
        this.plugin = plugin;
    }

    public void kick(Player player){
        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "" +
                plugin.getConfig.getString("prefix") +
                "\n" +
                "\n" +
                "&bWe are currently under maintenance.\n" +
                "&dWe will be back soon!"
        ));
    }

}
