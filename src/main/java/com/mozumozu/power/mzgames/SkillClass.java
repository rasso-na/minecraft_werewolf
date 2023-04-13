package com.mozumozu.power.mzgames;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static com.mozumozu.power.mzgames.Init.gamemode;
import static com.mozumozu.power.mzgames.Init.playerinfo;
import static com.mozumozu.power.mzgames.MzGames.config;

public class SkillClass {

    public static void uraSkill(Player player){// playerは実行者（占い）
        player.sendMessage("\n=============================");
        player.sendMessage(ChatColor.GRAY + "[占い対象の選択]");
        for (String targetname : playerinfo.keySet()) {
            if(!targetname.equalsIgnoreCase(player.getName())) {//自分以外の名前を表示
                TextComponent message = new TextComponent(ChatColor.AQUA + targetname);
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ura " + targetname));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + targetname + " を占う")));
                player.sendMessage(message);
            }
        }
        player.sendMessage("=============================");
    }

    public static void reiSkill(Player player){// playerは実行者（霊媒）
        player.sendMessage("\n=============================");
        player.sendMessage(ChatColor.GRAY + "[霊媒対象の選択]");
        for (String targetname : playerinfo.keySet()) {
            if(!targetname.equalsIgnoreCase(player.getName())) {//自分以外の名前を表示
                TextComponent message = new TextComponent(ChatColor.AQUA + targetname);
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rei " + targetname));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + targetname + " を霊媒する")));
                player.sendMessage(message);
            }
        }
        player.sendMessage("=============================");
    }

    public static void metSkill(Player player){// playerは実行者（ﾒﾀﾓﾝ）
        player.sendMessage("\n=============================");
        player.sendMessage(ChatColor.GRAY + "[変身対象の選択]");
        for (String targetname : playerinfo.keySet()) {
            if(!targetname.equalsIgnoreCase(player.getName())) {//自分以外の名前を表示
                TextComponent message = new TextComponent(ChatColor.AQUA + targetname);
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/met " + targetname));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + targetname + " に変身する")));
                player.sendMessage(message);
            }
        }
        player.sendMessage("=============================");
    }

    public static void touSkill(Player player){// playerは実行者（透視）
        player.sendMessage("\n=============================");
        player.sendMessage(ChatColor.GRAY + "[透視対象の選択]");
        for (String targetname : playerinfo.keySet()) {
            if(!targetname.equalsIgnoreCase(player.getName())) {//自分以外の名前を表示
                TextComponent message = new TextComponent(ChatColor.AQUA + targetname);
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tou " + targetname));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + targetname + " の持ち物を透視する")));
                player.sendMessage(message);
            }
        }
        player.sendMessage("=============================");
    }


}
