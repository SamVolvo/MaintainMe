package com.samvolvo.maintainMe.commands;

import com.samvolvo.maintainMe.MaintainMe;
import com.samvolvo.maintainMe.methods.KickMethod;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig.getString("prefix") + "&cYou do not have the permission to use this command."));
                return true;
            }

            if (args.length != 1){
                player.sendMessage( ChatColor.translateAlternateColorCodes('&', plugin.getConfig.getString("prefix") + "&7: &cUse /maintenance <on/off>"));
                return true;
            }

            String option = args[0];

            switch (option.toLowerCase()){
                case "on":
                    plugin.getMaintenanceMethod().enableMaintenance();
                    break;
                case "off":
                    plugin.getMaintenanceMethod().disableMaintenance();
                    break;
                case "reload":
                    plugin.getSamVolvoLogger().loading("Reloading plugin!");
                    plugin.loadConfig();
                    plugin.getSamVolvoLogger().info("Reload Completed!");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig.getString("prefix") + "&7: &eReload completed!"));
                    break;
                default:
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig.getString("prefix") + "&7: &cUse /maintenance <on/off>"));
                    break;
            }
            return true;
        }
        return true;
    }
}
