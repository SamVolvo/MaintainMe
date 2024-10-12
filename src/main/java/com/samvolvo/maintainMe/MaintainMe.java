package com.samvolvo.maintainMe;

import com.samvolvo.maintainMe.commands.MaintenaceCommand;
import com.samvolvo.maintainMe.listeners.PlayerJoinListener;
import com.samvolvo.maintainMe.listeners.ServerListPingListener;
import com.samvolvo.maintainMe.utils.Logger;
import com.samvolvo.maintainMe.utils.Motd;
import com.samvolvo.maintainMe.utils.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class MaintainMe extends JavaPlugin {
    private boolean maintenanceMode;
    // Tools
    private Motd motdTools;
    private UpdateChecker updateChecker;
    private Logger logger;


    @Override
    public void onEnable() {
        logger = new Logger(this);

        logger.loading("Starting");
        maintenanceMode = false;

        // register tools
        motdTools = new Motd(this);

        // Register commands
        getCommand("maintenance").setExecutor(new MaintenaceCommand(this));

        // Register Listeners
        getServer().getPluginManager().registerEvents(new ServerListPingListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        updateChecker = new UpdateChecker(this, "SamVolvo", "MaintainMe", "https://modrinth.com/project/maintiainme");


        logger.info("Succesfully loaded MaintainMe!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        logger.loading("Shutting down!");
        logger.info("Goodbye");
    }
    // getters

    public boolean isMaintenanceMode() {
        return maintenanceMode;
    }

    /**
     * @param what is it on or off?
     */
    public void setMaintenanceMode(boolean what) {
        logger.debug("maintenance mode set to " + what);
        maintenanceMode = what;

    }

    public Logger getSamVolvoLogger(){
        return logger;
    }

    public Motd getMotdTools() {
        return motdTools;
    }

    // UpdateChecker
    private void Checkforupdates(){
        List<String> nameless = updateChecker.generateUpdateMessage(getDescription().getVersion());
        if (!nameless.isEmpty()){
            for (String message : nameless){
                logger.warning(message);
            }
        }
    }
}
