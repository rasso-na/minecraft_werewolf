package com.mozumozu.power.mzgames;
import static com.mozumozu.power.mzgames.MzGames.config;
import static com.mozumozu.power.mzgames.MzGames.plugin;

public class ConfigClass {

    public static void saveAndReloadConfig() {
        plugin.saveConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

}
