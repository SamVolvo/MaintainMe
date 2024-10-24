package com.samvolvo.maintainme.interfaces;

import com.samvolvo.maintainme.MaintainMe;
import org.bukkit.entity.Player;

public interface NMSHandler {

    public void kick(Player player, MaintainMe plugin);
    public void playOrbSound(Player player, MaintainMe plugin);

}
