package com.mozumozu.power.mzgames;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PotionEffect {

    public static void removePotionEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {// 全員のポーション効果を解除
            for (org.bukkit.potion.PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }
        }
    }
}
