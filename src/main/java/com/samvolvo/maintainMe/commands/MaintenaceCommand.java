package com.samvolvo.maintainMe.commands;

import com.samvolvo.maintainMe.MaintainMe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MaintenaceCommand implements CommandExecutor {
    private MaintainMe plugin;

    public MaintenaceCommand(MaintainMe plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;

            if (!player.hasPermission("maintainme.maintenance")){
                player.sendMessage("§cYou do not have the permission to use this command.");
                return true;
            }

            if (args.length != 1){
                player.sendMessage("§cUse /maintenance <on/off>");
                return true;
            }

            String option = args[0];

            switch (option.toLowerCase()){
                case "on":
                    plugin.setMaintenanceMode(true);
                    player.sendMessage("§b§l[DEBUG]: §eMaintenance mode is now §aenabled.");
                    break;
                case "off":
                    plugin.setMaintenanceMode(false);
                    player.sendMessage("§b§l[DEBUG]: §eMaintenance mode is now §cdisabled.");
                    break;
                default:
                    player.sendMessage("§cUse /maintenance <on/off>");
                    break;
            }
            return true;
        }
        return true;
    }
}
