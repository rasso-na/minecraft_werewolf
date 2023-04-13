package com.mozumozu.power.mzgames;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

import static com.mozumozu.power.mzgames.Init.*;

public class Always {
    public static void always() {
        Actionbar();
        Effect();
    }

    public static void Actionbar() {
        if (gamemode.equals("人狼")) {// 参加するであろう人数（スペクテイターでない人の人数）を取得
            List<String> maypart = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
                    maypart.add(player.getName());
                }
            }
            int jobnumsum = 0;
            for (String jobname : jobs.get(gamemode).keySet()){// 当該ゲームモードの役職名を取得
                jobnumsum = jobnumsum + jobs.get(gamemode).get(jobname);
            }
            StringBuilder jobinfomation = new StringBuilder();
            for (String jobname : jobs.get(gamemode).keySet()){// 当該ゲームモードの役職名を取得
                int jobnum = jobs.get(gamemode).get(jobname);// 役職ごとの人数を取得
                jobinfomation.append(ChatColor.BLUE + jobname + ": " + ChatColor.WHITE + jobnum + "人 | ");
            }
            jobinfomation.append(ChatColor.YELLOW + "" + jobnumsum + " / " + maypart.size() + " 人");


            String timeNotifier = ChatColor.YELLOW + "残り時間: " + ChatColor.AQUA + gametimer_ongame + ChatColor.WHITE + " 秒";
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!onGame) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(jobinfomation.toString()));
                }
                if (onGame) {
                    if (playerinfo.keySet().contains(player.getName())) {// 参加者の場合
                        String moneyNotifier = ChatColor.YELLOW + "所持金: " + ChatColor.AQUA + playerinfo.get(player.getName()).get("money") + ChatColor.WHITE + " 円";
                        String jobNotifier = ChatColor.YELLOW + "役職: " + ChatColor.WHITE + "" + ChatColor.BOLD + playerinfo.get(player.getName()).get("job");
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(timeNotifier + ChatColor.WHITE + " | " + moneyNotifier + ChatColor.WHITE + " | " + jobNotifier));
                    } else {// 参加者でない場合
                        String kannsenn = "[ 観戦中 ] | " + timeNotifier;
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(kannsenn));
                    }
                }
            }
        } else if (gamemode.equals("鬼ごっこ")) {
//            Actionbar_onigokko();
        } else {// ゲームモード未指定のとき
            String mishitei = ChatColor.AQUA + "ゲームモード: " + ChatColor.WHITE + gamemode + " | " +ChatColor.AQUA + "オンライン: " + ChatColor.WHITE + online + " 人";
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(mishitei));
            }
        }
    }

    public static void Effect() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (onGame) {// ゲーム中の場合
                if (gamemode.equalsIgnoreCase("人狼")) {
                    // 人狼中はエフェクトなし
                }
            } else {// ゲーム中でない場合
                player.setFoodLevel(20);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 4, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 255, true, false));
            }
        }
    }

}
