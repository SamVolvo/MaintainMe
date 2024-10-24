package com.samvolvo.maintainme.commands.tabCompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        List<String> completions = new ArrayList<>();
        if (commandSender instanceof Player && !commandSender.hasPermission("maintainme.maintenance")){
            return null;
        }
        if (command.getName().equalsIgnoreCase("maintenance")){
            if (args.length == 1){
                completions.add("on");
                completions.add("off");
                completions.add("reload");
                completions.add("schedule");
                completions.add("stop");
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("reload")){
                completions.add("plugin");
                completions.add("config");
            }
        }

        return completions;
    }
}