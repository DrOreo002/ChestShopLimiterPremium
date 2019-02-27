package me.droreo002.cslimit;

import lombok.Getter;
import me.droreo002.cslimit.api.ChestShopAPI;
import me.droreo002.cslimit.commands.CommandListener;
import me.droreo002.cslimit.commands.CommandManager;
import me.droreo002.cslimit.commands.TabManager;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.database.CSLDatabase;
import me.droreo002.cslimit.hook.HookManager;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.listener.PlayerConnectionListener;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.cslimit.manager.LicenseManager;
import me.droreo002.cslimit.manager.logger.LogFile;
import me.droreo002.cslimit.metrics.Metrics;
import me.droreo002.cslimit.objects.ChestShopLimiterHandler;
import me.droreo002.cslimit.utils.CommonUtils;
import me.droreo002.oreocore.OreoCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestShopLimiter extends JavaPlugin {

    @Getter
    private static ChestShopLimiter instance;
    @Getter
    private HookManager hookManager;
    @Getter
    private ConfigManager configManager;
    @Getter
    private LangManager langManager;
    @Getter
    private Metrics met;
    @Getter
    private ChestShopAPI chestShopAPI;
    @Getter
    private CSLDatabase database;
    @Getter
    private LogFile logFile;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logFile = new LogFile();
        configManager = new ConfigManager(this);
        langManager = new LangManager(this);
        chestShopAPI = new ChestShopLimiterHandler();
        database = new CSLDatabase(this, configManager.getMemory().getDatabaseType());
        OreoCore.getInstance().dependPlugin(this, true);

        Bukkit.getPluginCommand("chestshoplimiter").setExecutor(new CommandListener());
        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
//        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
//        Bukkit.getPluginManager().registerEvents(new ShopCreateListener(this), this);
//        Bukkit.getPluginManager().registerEvents(new ShopDestroyListener(this), this);

        printInformation();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void printInformation() {
        Debug.info("&8&m-----------------------&7 [ &aChestShopLimiter &7] &8&m-----------------------", false, true);
        System.out.println(" ");
        // Dependency check (Hard depend)
        if (!CommonUtils.isPluginExists("ChestShop")) {
            Debug.info("&cChestShop plugin cannot be found!. This plugin will not working if there's no ChestShop plugin found!", false, true);
            System.out.println(" ");
            Debug.info("&8&m-----------------------&7 [ &aChestShopLimiter &7] &8&m-----------------------", false, true);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (!CommonUtils.isPluginExists("OreoCore")) {
            Debug.info("&cOreoCore plugin cannot be found!. This plugin cannot run without it!, plugin will now be disabled", false, true);
            System.out.println(" ");
            Debug.info("&8&m-----------------------&7 [ &aChestShopLimiter &7] &8&m-----------------------", false, true);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Debug.info("&fSetting up the &econfig.yml", false, true);
        Debug.info("&fSetting up the &eLang &ffile", false, true);
        Debug.info("&fEnabling &eChestShopLimiter-API", false, true);
        Debug.info("&fHooking to some plugins...", false, true);
        hookManager = new HookManager();
        Debug.info("&fPlugin is currently using &b" + configManager.getMemory().getDatabaseType() + "&f database type!", false, true);
        Debug.info("&bDatabase &fhas been successfully initialized!", false, true);
        CommandManager.init();
        Bukkit.getPluginCommand("chestshoplimiter").setTabCompleter(new TabManager());
        if (configManager.getMemory().isUseBstats()) {
            Debug.info("&fConnecting to &bBSTATS", false, true);
            if (configManager.getConfig().getBoolean("use-bstats")) {
                Debug.info("&fPlugin has been connected to &bBSTATS &fserver", false, true);
                met = new Metrics(this);
                met.addCustomChart(new Metrics.SimplePie("plugintype", () -> "Premium"));
            } else {
                Debug.info("&bBSTATS&f seems to be disabled. Now disconnecting from the server...",false, true);
                met = null;
            }
        }
        Debug.info("&fSuccess!. We're now running on version " + Bukkit.getBukkitVersion() + " &7(&bServer&7) &fand " + getDescription().getVersion() + " &7(&bPlugin&7)", false, true);
        Debug.info(LicenseManager.getBuyerInformation(), false, true);

        System.out.println(" ");
        Debug.info("&8&m-----------------------&7 [ &aChestShopLimiter &7] &8&m-----------------------", false, true);
    }
}
