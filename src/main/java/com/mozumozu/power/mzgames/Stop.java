package com.mozumozu.power.mzgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static com.mozumozu.power.mzgames.Init.*;
import static com.mozumozu.power.mzgames.NameTag.showNameTag;
import static com.mozumozu.power.mzgames.PotionEffect.removePotionEffects;
import static com.mozumozu.power.mzgames.MzGames.config;

public class Stop {

    public static void stop() {
        if (onGame) {// ゲーム中の場合（止める）

            // 全ゲーム共通の処理
            removePotionEffects();
            showNameTag();
            onGame = false;
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setLevel(0);// レベル0にする
                player.playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);// 効果音再生
                player.getInventory().clear();// 全員のインベントリ消去
                player.getEnderChest().clear();// 全員のエンダーチェスト消去
                if (config.getInt("admin.name." + player.getName()) == 1) {// OPがあればクリエイティブになる
                    player.setGameMode(GameMode.CREATIVE);
                } else {// OPがなければアドベンチャーになる
                    player.setGameMode(GameMode.ADVENTURE);
                }
            }
            for (String task : gameinfo.get("resetabletasks").keySet()) {// 動いているリセット（すべき）タスクを終了
                if (gameinfo.get("resetabletasks").get(task) != null) {
                    Bukkit.getScheduler().cancelTask(gameinfo.get("resetabletasks").get(task));
                }
            }

            if (gamemode.equalsIgnoreCase("人狼")) {
                // 人狼中共通の処理
                StringBuilder murname = new StringBuilder();
                StringBuilder jinname = new StringBuilder();
                StringBuilder kyoname = new StringBuilder();
                StringBuilder uraname = new StringBuilder();
                StringBuilder reiname = new StringBuilder();
//                StringBuilder metname = new StringBuilder();
                StringBuilder touname = new StringBuilder();
                for (String jobjudged : playerinfo.keySet()) {
                    if (playerinfo.get(jobjudged).get("job").equalsIgnoreCase("村人")) {
                        murname.append(ChatColor.WHITE + jobjudged + ChatColor.GRAY + ".  ");
                    } else if (playerinfo.get(jobjudged).get("job").equalsIgnoreCase("人狼")) {
                        jinname.append(ChatColor.WHITE + jobjudged + ChatColor.GRAY + ".  ");
                    } else if (playerinfo.get(jobjudged).get("job").equalsIgnoreCase("狂人")) {
                        kyoname.append(ChatColor.WHITE + jobjudged + ChatColor.GRAY + ".  ");
                    } else if (playerinfo.get(jobjudged).get("job").equalsIgnoreCase("占い")) {
                        uraname.append(ChatColor.WHITE + jobjudged + ChatColor.GRAY + ".  ");
                    } else if (playerinfo.get(jobjudged).get("job").equalsIgnoreCase("霊媒")) {
                        reiname.append(ChatColor.WHITE + jobjudged + ChatColor.GRAY + ".  ");
                    } else if (playerinfo.get(jobjudged).get("job").equalsIgnoreCase("透視")) {
                        touname.append(ChatColor.WHITE + jobjudged + ChatColor.GRAY + ".  ");
                    }
//                    else if (playerinfo.get(jobjudged).get("job").equalsIgnoreCase("ﾒﾀﾓﾝ")) {
//                        metname.append(ChatColor.WHITE + jobjudged + ChatColor.GRAY + ".  ");
//                    }
                }
                Bukkit.broadcastMessage(ChatColor.BOLD + "\n\n\n\n+-+-+-+-+-+ 今回の役職 +-+-+-+-+-+");
                Bukkit.broadcastMessage("\n" + ChatColor.GREEN + "" + ChatColor.BOLD + "村人");
                Bukkit.broadcastMessage("" + murname);
                Bukkit.broadcastMessage("\n" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "人狼");
                Bukkit.broadcastMessage("" + jinname);
                Bukkit.broadcastMessage("\n" + ChatColor.GOLD + "" + ChatColor.BOLD + "狂人");
                Bukkit.broadcastMessage("" + kyoname);
                Bukkit.broadcastMessage("\n" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "占い");
                Bukkit.broadcastMessage("" + uraname);
                Bukkit.broadcastMessage("\n" + ChatColor.GRAY + "" + ChatColor.BOLD + "霊媒");
                Bukkit.broadcastMessage("" + reiname);
                Bukkit.broadcastMessage("\n" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "ﾒﾀﾓﾝ");
                Bukkit.broadcastMessage("" + metname);
                Bukkit.broadcastMessage("\n" + ChatColor.BLUE + "" + ChatColor.BOLD + "透視");
                Bukkit.broadcastMessage("" + touname);
                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage(" ");

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (gameinfo.get("jobsleft").get("村人") + gameinfo.get("jobsleft").get("占い") + gameinfo.get("jobsleft").get("霊媒") == 0) {// 村人陣営0人（＝人狼陣営の勝利）の場合
                        String JinWins = ChatColor.DARK_RED + "" + ChatColor.BOLD + "人狼陣営 " + ChatColor.YELLOW + "勝利" + ChatColor.WHITE + "!!!";
                        player.sendTitle(JinWins, "");
                    } else if (gameinfo.get("jobsleft").get("人狼") == 0) {// 人狼陣営0人（＝村人陣営の勝利）の場合
                        String MurWins = ChatColor.GREEN + "" + ChatColor.BOLD + "村人陣営 " + ChatColor.YELLOW + "勝利" + ChatColor.WHITE + "!!!";
                        player.sendTitle(MurWins, "");
                    }
                }
            } else if (gamemode.equalsIgnoreCase("鬼ごっこ")) {

            }
            gameinit();
        } else {// ゲーム中ではない場合（なにもしない）
            Bukkit.broadcastMessage("ゲーム中ではありません");
        }
    }
}
