package com.mozumozu.power.mzgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import static com.mozumozu.power.mzgames.Init.gamemode;
import static com.mozumozu.power.mzgames.Init.hnt;
import static com.mozumozu.power.mzgames.MzGames.config;

public class NameTag {

    public static void hideNameTag() {
        if (hnt.getTeam("nhide") != null) {
            hnt.getTeam("nhide").unregister();
        }
        Team t = hnt.registerNewTeam("nhide");
        for (Player all : Bukkit.getOnlinePlayers()) {
            t.addEntry(all.getName());
            all.setPlayerListName(" ");
        }
        t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        t.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.NEVER);
        t.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        t.setCanSeeFriendlyInvisibles(false);
        t.setAllowFriendlyFire(true);
    }

    public static void showNameTag() {
        if (hnt.getTeam("nhide") != null) {
            hnt.getTeam("nhide").unregister();
        }
        for (Player all : Bukkit.getOnlinePlayers()) {
            if(config.getInt("admin.name." + all.getName()) == 1) {// 管理者なら管理者タグをつける
                all.setPlayerListName(ChatColor.YELLOW + "[管理]" + all.getName());
            } else {// 管理者でなければシンプルにそのままリセット
                all.setPlayerListName("");
            }
        }
    }

}
