package com.mozumozu.power.mzgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.*;

import static com.mozumozu.power.mzgames.GameTimer.chestFillIncrease;
import static com.mozumozu.power.mzgames.Init.*;
import static com.mozumozu.power.mzgames.Init.playerinfo;
import static com.mozumozu.power.mzgames.MzGames.config;

public class Start_JINROU {

    public static void start_JINROU(){

        if (gamemode.equalsIgnoreCase("人狼")) {

            if (startcounter == 3) {
                List<String> classedplayers = new ArrayList<>(playerinfo.keySet());
                Collections.shuffle(classedplayers);
                for (String jobname : jobs.get("人狼").keySet()) {// 役職振り分け
                    int n = jobs.get("人狼").get(jobname);
                    for (int j = 0; j < n; j++) {
                        String classed = classedplayers.get(0);
                        playerinfo.put(classed, new HashMap<>());
                        playerinfo.get(classed).put("job", jobname);
                        classedplayers.remove(0);
                    }
                }
                gameinfo.get("jobsleft").putAll(jobs.get("人狼"));
            }

            if (startcounter == 2) {
                for (String playername : playerinfo.keySet()) {// もろもろの数値設定(参加者のみ)
                    Player player = Bukkit.getServer().getPlayer(playername);// プレイヤーに変換
                    player.setFoodLevel(20);
                    player.setHealth(20.0);
                    player.setLevel(0);
                    player.setGameMode(GameMode.ADVENTURE);
                    playerinfo.get(playername).put("money", "0");
                    playerinfo.get(playername).put("death", "false");
                    playerinfo.get(playername).put("kake", "0");
                    for (String skillname : config.getConfigurationSection("skill").getKeys(false)) {
                        List<String> condition_gamemode = Arrays.asList(config.getString("skill." + skillname + ".gamemode").split(" "));
                        List<String> condition_job = Arrays.asList(config.getString("skill." + skillname + ".job").split(" "));
                        if (condition_job.contains(playerinfo.get(playername).get("job")) && condition_gamemode.contains(gamemode)) {// 現在のゲームモードのショップであれば
                            playerinfo.get(playername).put("skill", config.getString("skill." + skillname + ".description.times"));
                        }
                    }
                }
            }

            if (startcounter == 1) {

                metname = new StringBuilder();
                for (String playername : playerinfo.keySet()) {// もろもろの数値設定(参加者のみ)
                    if (playerinfo.get(playername).get("job").equalsIgnoreCase("人狼")) {
                        wolfs.add(playername);
                    } else if (playerinfo.get(playername).get("job").equalsIgnoreCase("ﾒﾀﾓﾝ")) {
                        metname.append(ChatColor.GRAY + playername + ".  ");
                    }
                }
                Collections.shuffle(wolfs);//人狼リストをシャッフルして順番をぐちゃぐちゃにする
            }

            if (startcounter == 0) {
                Bukkit.broadcastMessage(ChatColor.GRAY + "\n[今回の役職]");
                for (String jobname : jobs.get(gamemode).keySet()){// 当該ゲームモードの役職名を取得
                    int jobnum = jobs.get(gamemode).get(jobname);// 役職ごとの人数を取得
                    Bukkit.broadcastMessage(ChatColor.YELLOW + jobname + ": " + ChatColor.WHITE + jobnum + "人");
                }
                StringBuilder jinrou = new StringBuilder();
                for (String wolfname : wolfs) {
                    jinrou.append(ChatColor.WHITE + wolfname + ChatColor.GRAY + ".  ");
                }
                bossBar = Bukkit.createBossBar(ChatColor.WHITE + "次のチェスト補充ルーレットまで......(" + ChatColor.YELLOW + chestfill_interval + ChatColor.WHITE + "秒)", BarColor.BLUE, BarStyle.SOLID);// ボスバーを生成
                for (String playername : playerinfo.keySet()) {
                    Player player = Bukkit.getServer().getPlayer(playername);
                    bossBar.addPlayer(player);// ボスバーを全員に表示
                    player.sendMessage(ChatColor.GRAY + "\n[役職] " + ChatColor.AQUA + "" + ChatColor.BOLD + playerinfo.get(playername).get("job"));
                    if (playerinfo.get(playername).get("job").equalsIgnoreCase("人狼")) {
                        player.sendMessage(ChatColor.GRAY + "[今回の" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "人狼" + ChatColor.GRAY + "] " + jinrou);
                    }
                }
                chestFillIncrease();// チェストのfill増加をスタート
            }
        }

    }

}
