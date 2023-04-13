package com.mozumozu.power.mzgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.mozumozu.power.mzgames.Init.playerinfo;

public class Rei {

    public static boolean rei(Player player, String[] argList) {
        String playername = player.getName();
        String arg0 = argList.length == 0 ? "" : argList[0];// 第１引数がなければ第１引数を空欄にする
        int avalable = Integer.parseInt(playerinfo.get(player.getName()).get("skill"));
        if (avalable == 0) {// 霊媒可能回数が０であればメッセージを表示して終了する
            player.sendMessage("\n**********");
            return true;
        }
        String target = Bukkit.getServer().getPlayer(arg0).getName();// 指定したプレイヤーの名前を変数tに格納
        avalable--;
        playerinfo.get(player.getName()).put("skill", String.valueOf(avalable));// skillの回数を置き換え
        if (playerinfo.get(target).get("death").equalsIgnoreCase("false")) {// 指定したプレイヤーがまだ生きている場合にはメッセージを表示して終了する
            player.sendMessage("\n" + ChatColor.YELLOW + target + ChatColor.WHITE + " **********");
            return true;
        }
        if (playerinfo.get(target).get("job").equalsIgnoreCase("人狼")) {// 指定したプレイヤーが人狼であった場合
            player.sendMessage("\n" + ChatColor.YELLOW + target + ChatColor.WHITE + " は" + ChatColor.RED + " 人狼 " + ChatColor.WHITE + "です。");
        } else {// 指定したプレイヤーが人狼でなかった場合
            player.sendMessage("\n" + ChatColor.YELLOW + target + ChatColor.WHITE + " は" + ChatColor.RED + " 人狼 " + ChatColor.WHITE + "ではありません。");
        }
        return true;

    }

}
