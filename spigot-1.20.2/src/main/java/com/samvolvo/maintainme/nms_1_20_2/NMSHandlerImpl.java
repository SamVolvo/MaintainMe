package com.samvolvo.maintainme.nms_1_20_2;

import com.samvolvo.maintainme.MaintainMe;
import com.samvolvo.maintainme.interfaces.NMSHandler;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class NMSHandlerImpl implements NMSHandler {

    @Override
    public void kick(Player player, MaintainMe plugin) {
        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "" +
                plugin.getConfig().getString("prefix") +
                "\n" +
                "\n" +
                "&bWe are currently under maintenance.\n" +
                "&dWe will be back soon!"
        ));
    }

    @Override
    public void playOrbSound(Player player, MaintainMe plugin) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
    }
}
