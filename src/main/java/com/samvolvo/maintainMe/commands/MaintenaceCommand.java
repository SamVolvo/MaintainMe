package com.samvolvo.maintainMe.commands;

import com.samvolvo.maintainMe.MaintainMe;
import com.samvolvo.maintainMe.methods.KickMethod;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

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
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&cYou do not have the permission to use this command."));
                return true;
            }

            if (args.length != 1 && args.length != 2){
                player.sendMessage( ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: &cInvalid command usage. Please read the plugin description for more info on the commands."));
                return true;
            }

            String option = args[0];

            switch (option.toLowerCase()){
                case "on":
                    if (plugin.isMaintenanceMode()){
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: &eMaintenance mode is already enabled!"));
                        break;
                    }
                    plugin.getMaintenanceMethod().enableMaintenance();
                    break;
                case "off":
                    if (!plugin.isMaintenanceMode()){
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: &eMaintenance mode is already disabled!"));
                        break;
                    }
                    plugin.getMaintenanceMethod().disableMaintenance();
                    break;
                case "reload":
                    if (args.length != 2){
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: Please specify what you want to reload."));
                    }
                    switch (args[1].toLowerCase()){
                        case "plugin":
                            plugin.getSamVolvoLogger().loading("Reloading plugin!");
                            plugin.onDisable();
                            plugin.onEnable();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: &e reload completed!"));
                            break;
                        case "config":
                            plugin.getSamVolvoLogger().loading("Reloading config!");
                            plugin.loadConfig();
                            plugin.getSamVolvoLogger().info("Reload Completed!");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: &eConfig reload completed!"));
                            break;
                    }
                    break;
                case "schedule":
                    if (plugin.isMaintenanceMode()) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: &eMaintenance mode is already enabled!"));
                        break;
                    }
                    if (args.length != 2) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: Please specify a time in minutes."));
                        break;
                    }
                    try {
                        int minutes = Integer.parseInt(args[1]);
                        if (plugin.startMaintenanceTimer(minutes)) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: &eScheduled maintenance in " + minutes + " minutes."));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: &eThere is already an active maintenance timer."));
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: Invalid time format. Use /maintenance schedule '<time in minutes>'"));
                    }
                    break;
                case "stop":
                    if (plugin.getScheduledMaintenanceTask() == null){
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: &eThere is no maintenance countdown running!"));
                        break;
                    }

                    plugin.stopMaintenanceTimer();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: &eMaintenance countdown stopped."));
                    break;
                default:
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: &cUse /maintenance <on/off/reload/schedule>"));
                    break;
            }
            return true;
        }else{
            sender.sendMessage("We are currently working on the commands for console.");
        }
        return true;
    }
}
