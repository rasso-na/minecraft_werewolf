package com.mozumozu.power.mzgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

import static com.mozumozu.power.mzgames.Init.*;
import static com.mozumozu.power.mzgames.MzGames.plugin;

public class GameTimer {

    public static void gameTimer() {
        gametimer_ongame = gametimer;
        BukkitTask gametimertask = new BukkitRunnable() {
            @Override
            public void run() {
                if (gametimer_ongame == 0) {
                    Bukkit.getScheduler().cancelTask(gameinfo.get("resetabletasks").get("gametimertask"));
                    return;
                }
                gametimer_ongame--;
            }
        }.runTaskTimer(plugin, 0L, 20L);// １秒毎に実行する
        gameinfo.get("resetabletasks").put("gametimertask", gametimertask.getTaskId());
    }

    public static void chestFillIncrease() {
        BukkitTask chestfillincreasetask = new BukkitRunnable() {
            @Override
            public void run() {
                chestfill_interval--;
                bossBar.setTitle(ChatColor.WHITE + "次のチェスト補充ルーレットまで......(" + ChatColor.YELLOW + chestfill_interval + ChatColor.WHITE + "秒)");
                bossBar.setProgress((double) chestfill_interval / 60);
                if(chestfill_interval == 0){
                    String chestfill_notify;
                    chestfill_interval = 60;
                    Random random = new Random();
                    int r = random.nextInt(10);
                    if (r < 7) { // (0, 1, 2, 3, 4, 5, 6), 7, 8, 9 : 7割の確率で補充される
                        chestfill++;
                        chestfill_notify = ChatColor.YELLOW + "‡ " + ChatColor.WHITE + "チェストが補充されました" + ChatColor.YELLOW + " ‡";
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        }
                    } else { // 7, 8, 9
                        chestfill_notify = ChatColor.RED + "× " + ChatColor.WHITE + "チェストは補充されません" + ChatColor.RED + " ×";
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, (float) 0.5);
                        }
                    }
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendTitle("", chestfill_notify);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);// １秒毎に実行する
        gameinfo.get("resetabletasks").put("chestfillincreasetask", chestfillincreasetask.getTaskId());
    }

}
