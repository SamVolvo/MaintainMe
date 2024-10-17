package com.samvolvo.maintainMe;

import com.samvolvo.maintainMe.commands.MaintenaceCommand;
import com.samvolvo.maintainMe.commands.tabCompleters.MaintenanceTabCompleter;
import com.samvolvo.maintainMe.listeners.PlayerJoinListener;
import com.samvolvo.maintainMe.listeners.ServerListPingListener;
import com.samvolvo.maintainMe.methods.KickMethod;
import com.samvolvo.maintainMe.methods.MaintenanceMethod;
import com.samvolvo.maintainMe.utils.Logger;
import com.samvolvo.maintainMe.utils.Metrics;
import com.samvolvo.maintainMe.utils.Motd;
import com.samvolvo.maintainMe.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public final class MaintainMe extends JavaPlugin {
    private boolean maintenanceMode;
    private Timer maintenanceTimer;
    private BukkitRunnable scheduledMaintenanceTask;


    // Tools
    private Motd motdTools;
    private UpdateChecker updateChecker;
    private Logger logger;
    private MaintenanceMethod maintenanceMethod;
    private KickMethod kickMethod;

    // Config
    private File configFile;
    private FileConfiguration config;
    private boolean isDebugOn;

    @Override
    public void onEnable() {
        logger = new Logger(this);
        logger.loading("Starting");


        Metrics metrics = new Metrics(this, 23626);

        saveDefaultConfig();
        loadConfig();

        maintenanceMode = config.getBoolean("settings.maintenanceOnStart");
        if (isDebugOn){
            if (maintenanceMode){
                logger.debug("Server starts with Maintenance mode on!");
            }else{
                logger.debug("Server starts without Maintenance mode on!");
            }

        }

        // Register tools
        motdTools = new Motd(this);
        maintenanceMethod = new MaintenanceMethod(this);
        kickMethod = new KickMethod(this);

        // Register commands
        getCommand("maintenance").setExecutor(new MaintenaceCommand(this));
        getCommand("maintenance").setTabCompleter(new MaintenanceTabCompleter());

        // Register Listeners
        getServer().getPluginManager().registerEvents(new ServerListPingListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        updateChecker = new UpdateChecker(this, "SamVolvo", "MaintainMe", "https://modrinth.com/project/maintainme");
        if (isDebugOn){
            logger.debug("Loading UpdateChecker!");
        }
        checkforupdates();
        logger.succes("Succesfully loaded MaintainMe!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (scheduledMaintenanceTask != null) {
            scheduledMaintenanceTask.cancel();
            scheduledMaintenanceTask = null; // Voeg deze lijn toe om de variabele op null te zetten
        }
        logger.loading("Shutting down!");
        logger.succes("Goodbye");
    }


    // Getters
    public boolean isMaintenanceMode() {
        return maintenanceMode;
    }

    /**
     * @param what do you want it to be on or off?
     */
    public void setMaintenanceMode(boolean what) {
        maintenanceMode = what;
    }

    public Logger getSamVolvoLogger() {
        return logger;
    }

    public Motd getMotdTools() {
        return motdTools;
    }

    public MaintenanceMethod getMaintenanceMethod() {
        return maintenanceMethod;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public KickMethod getKickMethod() {
        return kickMethod;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public BukkitRunnable getScheduledMaintenanceTask() {
        return scheduledMaintenanceTask;
    }

    // UpdateChecker
    private void checkforupdates() {
        List<String> nameless = updateChecker.generateUpdateMessage(getDescription().getVersion());
        if (nameless == null) {
            return;
        }
        if (!nameless.isEmpty()) {
            for (String message : nameless) {
                logger.warning(message);
            }
        }else{
            logger.info("*** You are running the latest version of this plugin! ***");
        }
    }

    public void loadConfig() {
        if (configFile == null) {
            configFile = new File(getDataFolder(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        isDebugOn = config.getBoolean("special.debugMode");
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            logger.error("There was an error saving the config");
        }
    }

    public void scheduleMaintenance(int minutes) {
        if (scheduledMaintenanceTask != null) { // Voeg deze lijn toe
            scheduledMaintenanceTask.cancel(); // Voeg deze lijn toe
        } // Voeg deze lijn toe

        scheduledMaintenanceTask = new BukkitRunnable() {
            int timeleft = minutes * 60;

            @Override
            public void run() {
                if (isMaintenanceMode()) {
                    cancel();
                }
                if (timeleft <= 0) {
                    maintenanceMethod.enableMaintenance();
                    cancel();
                    return;
                }
                String message = getMessage(timeleft);
                if (!message.isEmpty()) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("prefix") + "&7: " + message));
                        players.playSound(players.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                    }
                    logger.info(message);
                }
                timeleft--;
            }
        };
        scheduledMaintenanceTask.runTaskTimer(this, 0L, 20L);
    }


    /**
     * @param timeLeft How many seconds are left?
     * @return What message do you get when there is a specific amount of time left
     */
    private String getMessage(int timeLeft) {
        if (timeLeft % 300 == 0 && timeLeft > 300) { // Every 5 minutes while above 5 minutes left.
            return "&eMaintenance starting in " + (timeLeft / 60) + " minutes.";
        } else if (timeLeft == 60) { // 1 minute left.
            return "&eMaintenance starting in 1 minute.";
        } else if (timeLeft <= 300 && timeLeft % 60 == 0) { // Every minute when less than 5 minutes left.
            return "&eMaintenance starting in " + (timeLeft / 60) + " minutes.";
        } else if (timeLeft == 30) { // Only when there are 30 seconds left.
            return "&eMaintenance starting in 30 seconds.";
        } else if (timeLeft <= 10) { // Every second under 10 seconds.
            return "&eMaintenance starting in " + timeLeft + " seconds.";
        }
        return "";
    }

    public boolean startMaintenanceTimer(int minutes) {
        if (scheduledMaintenanceTask != null) {
            return false; // Timer is al bezig
        }
        scheduleMaintenance(minutes); // Start de maintenance timer
        return true;
    }


    public void stopMaintenanceTimer() {
        if (scheduledMaintenanceTask != null) {
            scheduledMaintenanceTask.cancel();
            scheduledMaintenanceTask = null; // Voeg deze lijn toe om de variabele op null te zetten
        }
    }
}
