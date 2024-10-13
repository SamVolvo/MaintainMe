package com.samvolvo.maintainMe.commands.tabCompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        List<String> completions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("maintenance")){
            if (args.length == 1){
                completions.add("on");
                completions.add("off");
                completions.add("reload");
            }
        }

        return List.of();
    }
}
