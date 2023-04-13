package com.mozumozu.power.mzgames;

import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

import static com.mozumozu.power.mzgames.ConfigClass.saveAndReloadConfig;
import static com.mozumozu.power.mzgames.NameTag.showNameTag;
import static com.mozumozu.power.mzgames.PotionEffect.removePotionEffects;


public class Init {

    // プラグイン読み込み時だけ初期化すればいい処理（こっちにだけ書けばおｋ）
    static Location spawnpoint = null;
    static Location admin = null;
    static int online = 0;
    static String gamemode = "未指定";
    static int startcounter = 0;
    static int gametimer = 500;
    static Map<String, Map<String, Integer>> jobs = new HashMap<>();
    static Scoreboard hnt;

    // 毎ゲームごとに初期化する値
    static boolean onGame = false;
    static int chestfill = 1;
    static int chestfill_interval = 60;
    static Map<String, Map<String, String>> playerinfo = new HashMap<>();
    static Map<String, Map<String, Integer>> gameinfo = new HashMap<>();
    static int gametimer_ongame = 0;
    static List<String> wolfs = new ArrayList<>();
    static BossBar bossBar = null;
    static StringBuilder metname;


    public static void allinit() { // 全変数の初期化（ガチ）or というよりは、上の欄で初期化できないもの（mapとか）

        gamemode = "未指定";

        hnt = Bukkit.getScoreboardManager().getMainScoreboard();

        jobs.put("人狼", new HashMap<>());
        jobs.get("人狼").put("村人", 3);// 人狼の役職の初期値
        jobs.get("人狼").put("人狼", 2);
        jobs.get("人狼").put("狂人", 1);
        jobs.get("人狼").put("占い", 1);
        jobs.get("人狼").put("霊媒", 1);
        jobs.get("人狼").put("ﾒﾀﾓﾝ", 1);
        jobs.get("人狼").put("透視", 1);


        gameinit();

        saveAndReloadConfig();
    }

    public static void gameinit() {// ここに書くのは毎ゲームごとに初期化する値のみ

        // ゲームルール設定
        for (World w : Bukkit.getWorlds()) {
            w.setGameRule(GameRule.DO_INSOMNIA, false);//ファントム沸きOFF
            w.setGameRule(GameRule.DISABLE_RAIDS, true);//襲撃OFF
            w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);//実績表示OFF
            w.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);//コマブロ出力OFF
            w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);//時間進行OFF
            w.setGameRule(GameRule.DO_FIRE_TICK, false);//炎の延焼OFF
            w.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);//即時リスポーンOFF
            w.setGameRule(GameRule.DO_MOB_SPAWNING, false);//モブスポーンOFF
            w.setGameRule(GameRule.DO_PATROL_SPAWNING, false);//襲撃者スポーンOFF
            w.setGameRule(GameRule.DO_TRADER_SPAWNING, false);//行商人スポーンOFF
            w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);//天気サイクルOFF
            w.setGameRule(GameRule.KEEP_INVENTORY, true);//キープインベントリON
            w.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, true);//コマンドフィードバックON
            w.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);//デスメッセージ表示OFF
            w.setDifficulty(Difficulty.NORMAL);//難易度をノーマルにする
        }


        showNameTag();


        if(bossBar != null){
            bossBar.removeAll();
        }
        bossBar = null;


        onGame = false;
        chestfill = 1;
        chestfill_interval = 60;

        gametimer_ongame = gametimer;

        if (gameinfo.containsKey("resetabletasks")) {
            for (String task : gameinfo.get("resetabletasks").keySet()) {// 動いているリセット（すべき）タスクを終了
                if (gameinfo.get("resetabletasks").get(task) != null) {
                    Bukkit.getScheduler().cancelTask(gameinfo.get("resetabletasks").get(task));
                }
            }
        }

        wolfs.clear();

        if (playerinfo != null) {
            playerinfo.clear();
        }
        playerinfo = new HashMap<>();
        
        if (gameinfo != null) {
            gameinfo.clear();
        }
        gameinfo = new HashMap<>();
        gameinfo.put("chestopened", new HashMap<>());
        gameinfo.put("jobsleft", new HashMap<>());
        gameinfo.put("resetabletasks", new HashMap<>());

        removePotionEffects(); // ポーション効果を除去

    }
}
