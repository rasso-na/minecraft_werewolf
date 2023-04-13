package com.mozumozu.power.mzgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

import static com.mozumozu.power.mzgames.Init.playerinfo;

public class Tou {

    public static boolean tou(Player player, String[] argList) {
        String arg0 = argList.length == 0 ? "" : argList[0];// 第１引数がなければ第１引数を空欄にする
        int avalable = Integer.parseInt(playerinfo.get(player.getName()).get("skill"));
        if (avalable == 0) {// 透視可能回数が０であればメッセージを表示して終了する
            player.sendMessage("\n**********");
            return true;
        }
        String target = Bukkit.getServer().getPlayer(arg0).getName();// 指定したプレイヤーの名前を変数tに格納
        avalable--;
        playerinfo.get(player.getName()).put("skill", String.valueOf(avalable));// skillの回数を置き換え

        if (playerinfo.get(target).get("death").equalsIgnoreCase("false")) {// 指定したプレイヤーがまだ生きている場合にはその時点でのアイテムを格納
            Inventory inv = Bukkit.getServer().getPlayer(target).getInventory();
            StringBuilder items = new StringBuilder();
            for (int i=0; i < inv.getSize(); i++) {
                String itemname;
                if (inv.getItem(i) != null && !inv.getItem(i).getType().equals(Material.AIR)) {
                    if (inv.getItem(i).getItemMeta().hasDisplayName()) {
                        itemname = inv.getItem(i).getItemMeta().getDisplayName();
                    } else {
                        itemname = inv.getItem(i).getType().toString();
                    }
                    int amount = inv.getItem(i).getAmount();
                    items.append(ChatColor.YELLOW + itemname + ChatColor.WHITE + " × " + ChatColor.AQUA + amount + ChatColor.GRAY + ".  ");
                }
            }
            playerinfo.get(target).put("items", String.valueOf(items));
        }

        player.sendMessage(ChatColor.GRAY + "\n[" + target + "の所持品]");
        player.sendMessage(playerinfo.get(target).get("items"));
        player.sendMessage(ChatColor.GRAY + "===");
        return true;

    }

}
