package com.samvolvo.maintainMe.utils;

import com.samvolvo.maintainMe.MaintainMe;

public class Motd {
    private MaintainMe plugin;

    public Motd(MaintainMe plugin) {
        this.plugin = plugin;
    }

    public String getMOTD(String line1, String line2){
        int maxLineLenght = 53;
        if (line1.length() > maxLineLenght || line2.length() > maxLineLenght){
            throw new IllegalArgumentException("MOTD lines mus be within " + maxLineLenght + " characters.");
        }
        String motd = "§e" + centerText(line1) + "\n§b" + centerText(line2);
        return motd;
    }

    public String centerText(String text){
        int maxLineLength = 53;
        int padding = (maxLineLength - getEffectiveLength(text)) / 2;
        return " ".repeat(padding) + text + " ".repeat(padding);
    }

    private int getEffectiveLength(String text){
        return text.replaceAll("§c", "").length();
    }
}
