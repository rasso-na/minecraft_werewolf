package com.mozumozu.power.mzgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.mozumozu.power.mzgames.Init.gameinfo;
import static com.mozumozu.power.mzgames.Init.playerinfo;

public class Met {

    public static boolean met(Player player, String[] argList) {
        String playername = player.getName();
        String arg0 = argList.length == 0 ? "" : argList[0];// 第１引数がなければ第１引数を空欄にする
        int avalable = Integer.parseInt(playerinfo.get(player.getName()).get("skill"));
        if (avalable == 0) {// 使用可能回数が０であればメッセージを表示して終了する
            player.sendMessage("\n**********");
            return true;
        }
        String target = Bukkit.getServer().getPlayer(arg0).getName();// 指定したプレイヤーの名前を変数targetに格納
        avalable--;
        playerinfo.get(player.getName()).put("skill", String.valueOf(avalable));// skillの回数を置き換え(-1する)
        if (playerinfo.get(target).get("death").equalsIgnoreCase("true")) {// 指定したプレイヤーがすでに死んでいた場合にはメッセージを表示して終了する
            player.sendMessage("\n" + ChatColor.YELLOW + target + ChatColor.WHITE + " **********");
            return true;
        }
        String targetjob = playerinfo.get(target).get("job");
        String targetskill = playerinfo.get(target).get("skill");
        playerinfo.get(playername).put("job", targetjob); // ﾒﾀﾓﾝのjobをtargetのjobに置き換える
        playerinfo.get(playername).put("skill", targetskill); // ﾒﾀﾓﾝのskillをtargetのskillに置き換える
        int targetjobnow = gameinfo.get("jobsleft").get(targetjob);
        gameinfo.get("jobsleft").put(targetjob, targetjobnow + 1);// 対象の役職の人数を一人追加(ﾒﾀﾓﾝが増えるので)
        int metamonnow = gameinfo.get("jobsleft").get("ﾒﾀﾓﾝ");
        gameinfo.get("jobsleft").put("ﾒﾀﾓﾝ", metamonnow - 1);// ﾒﾀﾓﾝの人数を一人削除(ﾒﾀﾓﾝが減るので)
        player.sendMessage("\n" + ChatColor.AQUA + target + ChatColor.WHITE + "**********");
        player.sendMessage(ChatColor.GRAY + "[変身後の役職] " + ChatColor.AQUA + targetjob);
        player.sendMessage(ChatColor.GRAY + "**********");
        return true;
    }


}
