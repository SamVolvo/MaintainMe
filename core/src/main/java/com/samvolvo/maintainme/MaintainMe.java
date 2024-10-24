package com.samvolvo.maintainme;

import com.samvolvo.maintainme.commands.MaintenanceCommand;
import com.samvolvo.maintainme.commands.tabCompleters.MaintenanceTabCompleter;
import com.samvolvo.maintainme.interfaces.NMSHandler;
import com.samvolvo.maintainme.listeners.AbstractPlayerJoinListener;
import com.samvolvo.maintainme.listeners.AbstractServerListPingListener;
import com.samvolvo.maintainme.utils.Logger;
import com.samvolvo.maintainme.utils.MOTD;
import com.samvolvo.maintainme.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaintainMe extends JavaPlugin {
    private MaintainMe plugin;
    private boolean maintenanceMode;
    private Timer maintenanceTimer;
    private BukkitRunnable scheduledMaintenanceTask;

    private NMSHandler nmsHandler;
    private String minecraftVersion;

    ///Utils
    private Logger SamVolvoLogger;
    private MOTD motdUtil;
    private UpdateChecker updateChecker;

    ///Config
    private File configFile;
    private FileConfiguration config;


    @Override
    public void onEnable(){
        SamVolvoLogger = new Logger(this);
        plugin = this;
        saveDefaultConfig();
        loadConfig();
        SamVolvoLogger.loading("MaintainMe 2.0 bèta");
        nmsHandler = createNMSHandler();
        SamVolvoLogger.loading("Version Supported");


        motdUtil = new MOTD(this);

        registerListeners();

        getCommand("maintenance").setExecutor(new MaintenanceCommand(this));
        getCommand("maintenance").setTabCompleter(new MaintenanceTabCompleter());
        updateChecker = new UpdateChecker(this, "SamVolvo", "MaintainMe", "https://modrinth.com/project/maintainme");
        checkforupdates();


        SamVolvoLogger.succes("Loaded successfully");

    }

    @Override
    public void onDisable(){
        SamVolvoLogger.loading("Shutting Down!");
        if (scheduledMaintenanceTask != null) {
            scheduledMaintenanceTask.cancel();
            scheduledMaintenanceTask = null; // Voeg deze lijn toe om de variabele op null te zetten
        }
        SamVolvoLogger.succes("Goodbye!");
    }

    /// UpdateChecker
    private void checkforupdates() {
        List<String> nameless = updateChecker.generateUpdateMessage(getDescription().getVersion());
        if (nameless == null) {
            return;
        }
        if (!nameless.isEmpty()) {
            for (String message : nameless) {
                SamVolvoLogger.warning(message);
            }
        }else{
            SamVolvoLogger.info("*** You are running the latest version of this plugin! ***");
        }
    }

    /**
     * register all listeners
     */
    private void registerListeners() {
        try {
            String clazzName = "com.samvolvo.maintainme.nms_" + minecraftVersion.replace(".", "_") + ".ServerListPingListener";
            Class<? extends AbstractServerListPingListener> listenerClass = (Class<? extends AbstractServerListPingListener>) Class.forName(clazzName);
            AbstractServerListPingListener serverListPingListener = listenerClass.getConstructor(MaintainMe.class).newInstance(this);
            String className = "com.samvolvo.maintainme.nms_" + minecraftVersion.replace(".", "_") + ".PlayerJoinListener";
            Class<? extends AbstractPlayerJoinListener> listener2Class = (Class<? extends AbstractPlayerJoinListener>) Class.forName(className);
            AbstractPlayerJoinListener PlayerJoinListener = listener2Class.getConstructor(MaintainMe.class).newInstance(this);
            getServer().getPluginManager().registerEvents(serverListPingListener, this);
            getServer().getPluginManager().registerEvents(PlayerJoinListener, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /// Getters
    public NMSHandler getNmsHandler(){
        return nmsHandler;
    }
    public Logger getSamVolvoLogger(){
        return SamVolvoLogger;
    }
    public MOTD getMotdUtil(){
        return motdUtil;
    }
    public FileConfiguration getConfig(){
        return config;
    }
    public boolean isMaintenanceMode() {
        return maintenanceMode;
    }
    public Timer getMaintenanceTimer() {
        return maintenanceTimer;
    }
    public BukkitRunnable getScheduledMaintenanceTask() {
        return scheduledMaintenanceTask;
    }
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    /**
     * Returns the actual running Minecraft version, e.g. 1.20 or 1.16.5
     *
     * @return Minecraft version
     */
    private String getMinecraftVersion() {
        if (minecraftVersion != null) {
            return minecraftVersion;
        } else {
            String bukkitGetVersionOutput = Bukkit.getVersion();
            Matcher matcher = Pattern.compile("\\(MC: (?<version>[\\d]+\\.[\\d]+(\\.[\\d]+)?)\\)").matcher(bukkitGetVersionOutput);
            if (matcher.find()) {
                return minecraftVersion = matcher.group("version");
            } else {
                throw new RuntimeException("Could not determine Minecraft version from Bukkit.getVersion(): " + bukkitGetVersionOutput);
            }
        }
    }

    /**
     * Checks if the plugin is compatible with the server version
     * @return NMSHandler
     * @throws RuntimeException
     */
    @SuppressWarnings("unchecked")
    private NMSHandler createNMSHandler() throws RuntimeException {
        String clazzName = "com.samvolvo.maintainme.nms_" + getMinecraftVersion()
                .replace(".", "_") + ".NMSHandlerImpl";
        try {
            Class<? extends NMSHandler> clazz = (Class<? extends NMSHandler>) Class.forName(clazzName);
            return clazz.getConstructor().newInstance();
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("Can't instantiate NMSHandlerImpl for version " + getMinecraftVersion() +
                    " (class " + clazzName + " not found. This usually means that this Minecraft version is not " +
                    "supported by this version of the plugin.)", exception);
        } catch (InvocationTargetException exception) {
            throw new RuntimeException("Can't instantiate NMSHandlerImpl for version " + getMinecraftVersion() +
                    " (constructor in class " + clazzName + " threw an exception)", exception);
        } catch (InstantiationException exception) {
            throw new RuntimeException("Can't instantiate NMSHandlerImpl for version " + getMinecraftVersion() +
                    " (class " + clazzName + " is abstract)", exception);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException("Can't instantiate NMSHandlerImpl for version " + getMinecraftVersion() +
                    " (no-args constructor in class " + clazzName + " is not accessible)", exception);
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException("Can't instantiate NMSHandlerImpl for version " + getMinecraftVersion() +
                    " (no no-args constructor found in class " + clazzName + ")", exception);
        }
    }

    /**
     * Loads the config, or generates a new one if it doesn't exist.
     */
    public void loadConfig() {
        if (configFile == null) {
            configFile = new File(getDataFolder(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Save the new config.
     */
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            SamVolvoLogger.error("There was an error saving the config");
        }
    }

    public void setMaintenanceMode(boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    /// Enable the MaintenanceMode
    public void enableMaintenance(){
        /// Stop the maintenanceTimer if running
        if (getScheduledMaintenanceTask() != null){
            plugin.stopMaintenanceTimer();
        }
        /// Turn on maintenance mode.
        plugin.setMaintenanceMode(true);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()){
            if (!onlinePlayer.hasPermission("maintainme.join")){
                nmsHandler.kick(onlinePlayer, plugin);
            }else{
                onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',  plugin.getConfig().getString("prefix") + "&7: &eMaintenance mode is enabled!"));
                nmsHandler.playOrbSound(onlinePlayer, plugin);
            }
        }
    }

    public void disableMaintenance(){
        setMaintenanceMode(false);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()){
            onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&7: &eMaintenance mode is disabled!"));
            nmsHandler.playOrbSound(onlinePlayer, plugin);
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
                    enableMaintenance();
                    cancel();
                    return;
                }
                String message = getMessage(timeleft);
                if (!message.isEmpty()) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("prefix") + "&7: " + message));
                        nmsHandler.playOrbSound(players, plugin);
                    }
                    SamVolvoLogger.info(message);
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
            scheduledMaintenanceTask = null;
        }
    }

}