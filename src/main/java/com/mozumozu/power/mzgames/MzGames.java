package com.mozumozu.power.mzgames;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import static com.mozumozu.power.mzgames.ConfigClass.saveAndReloadConfig;
import static com.mozumozu.power.mzgames.Init.spawnpoint;
import static com.mozumozu.power.mzgames.Init.admin;
import static com.mozumozu.power.mzgames.NameTag.showNameTag;


public final class MzGames
        extends JavaPlugin {

    static Plugin plugin;

    static FileConfiguration config;
    Init init = new Init();
//    CustomConfig config;
//    CustomConfig item;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        // config.ymlが存在しない場合はファイルに出力します。
        saveDefaultConfig();
        // config.ymlを読み込みます。
        config = getConfig();

//        config = new CustomConfig(this);
//        item = new CustomConfig(this, "item.yml");


        getServer().getPluginManager().registerEvents(new ListenerClass(), this);
        getCommand("moz").setExecutor(new CommandClass());
        getCommand("ura").setExecutor(new CommandClass());
        getCommand("rei").setExecutor(new CommandClass());
        getCommand("met").setExecutor(new CommandClass());
        getCommand("tou").setExecutor(new CommandClass());

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {// プラグイン起動中常時実行の処理を開始
            @Override
            public void run() {
                Always.always();
            }
        }, 0L, 10L);// 1秒に2回実行する

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                init.allinit();
                getLogger().info("*初期化が完了しました*");
                if (config.contains("spawn.spawnpoint")) {// spawnpointの設定
                    World world = Bukkit.getServer().getWorld(config.getString("spawn.spawnpoint.world"));
                    double x = config.getDouble("spawn.spawnpoint.x");
                    double y = config.getDouble("spawn.spawnpoint.y");
                    double z = config.getDouble("spawn.spawnpoint.z");
                    float yaw = (float) config.getDouble("spawn.spawnpoint.yaw");
                    float pitch = (float) config.getDouble("spawn.spawnpoint.pitch");
                    spawnpoint = new Location(world, x, y, z, yaw, pitch);
                    getLogger().info("* config.ymlからスポーン地点を取得しました*");
                } else {// spawnpointが未指定の場合
                    getLogger().info("* config.ymlからスポーン地点を取得することができませんでした*");
                }
                if (config.contains("spawn.admin")) {// adminの設定
                    World world = Bukkit.getServer().getWorld(config.getString("spawn.admin.world"));
                    double x = config.getDouble("spawn.admin.x");
                    double y = config.getDouble("spawn.admin.y");
                    double z = config.getDouble("spawn.admin.z");
                    float yaw = (float) config.getDouble("spawn.admin.yaw");
                    float pitch = (float) config.getDouble("spawn.admin.pitch");
                    admin = new Location(world, x, y, z, yaw, pitch);
                    getLogger().info("* config.ymlからadminを取得しました*");
                } else {// adminが未指定の場合
                    getLogger().info("* config.ymlからadminを取得することができませんでした*");
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(config.getConfigurationSection("admin.name").getKeys(false).contains(player.getName())){// config.ymlの中に含まれていなかった場合には0にしていれる
                        config.set("admin.name." + player.getName(), 0);
                    }
                }
                saveAndReloadConfig();
                showNameTag();
            }
        };
        runnable.runTaskLater(this, 1L);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void saveAndReloadConfig() {
        saveConfig();
        reloadConfig();
        config = getConfig();
    }
}
