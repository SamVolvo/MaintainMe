package com.samvolvo.maintainMe;

import com.samvolvo.maintainMe.commands.MaintenaceCommand;
import com.samvolvo.maintainMe.commands.tabCompleters.MaintenanceTabCompleter;
import com.samvolvo.maintainMe.listeners.PlayerJoinListener;
import com.samvolvo.maintainMe.listeners.ServerListPingListener;
import com.samvolvo.maintainMe.methods.KickMethod;
import com.samvolvo.maintainMe.methods.MaintenanceMethod;
import com.samvolvo.maintainMe.utils.Logger;
import com.samvolvo.maintainMe.utils.Motd;
import com.samvolvo.maintainMe.utils.UpdateChecker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class MaintainMe extends JavaPlugin {
    private boolean maintenanceMode;
    // Tools
    private Motd motdTools;
    private UpdateChecker updateChecker;
    private Logger logger;
    private MaintenanceMethod maintenanceMethod;
    private KickMethod kickMethod;

    // Config
    private File configFile;
    private FileConfiguration config;


    @Override
    public void onEnable() {
        logger = new Logger(this);

        logger.loading("Starting");
        maintenanceMode = false;

        saveDefaultConfig();
        loadConfig();

        // register tools
        motdTools = new Motd(this);
        maintenanceMethod = new MaintenanceMethod(this);
        kickMethod = new KickMethod(this);

        // Register commands
        getCommand("maintenance").setExecutor(new MaintenaceCommand(this));
        getCommand("maintenance").setTabCompleter(new MaintenanceTabCompleter());

        // Register Listeners
        getServer().getPluginManager().registerEvents(new ServerListPingListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        updateChecker = new UpdateChecker(this, "SamVolvo", "MaintainMe", "https://modrinth.com/project/maintiainme");
        checkforupdates();

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
    public MaintenanceMethod getMaintenanceMethod(){
        return maintenanceMethod;
    }
    public FileConfiguration getConfig;
    public KickMethod getKickMethod(){
        return kickMethod;
    }
    public UpdateChecker getUpdateChecker(){
        return updateChecker;
    }

    // UpdateChecker
    private void checkforupdates(){
        List<String> nameless = updateChecker.generateUpdateMessage(getDescription().getVersion());
        if (!nameless.isEmpty()){
            for (String message : nameless){
                logger.warning(message);
            }
        }
    }

    public void loadConfig(){
        if (configFile == null){
            configFile = new File(getDataFolder(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig(){
        try{
            config.save(configFile);
        }catch (IOException e){
            logger.error("There was an error saving the config");
        }
    }
}
