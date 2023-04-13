package com.mozumozu.power.mzgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.mozumozu.power.mzgames.Init.gamemode;
import static com.mozumozu.power.mzgames.Init.playerinfo;
import static com.mozumozu.power.mzgames.InventoryClass.addItemMoz;
import static com.mozumozu.power.mzgames.InventoryClass.addItemMozToPlayer;
import static com.mozumozu.power.mzgames.MzGames.config;
import static com.mozumozu.power.mzgames.ItemClass.specialItems;


public class ShopClass {

    public static void buy(Player player, InventoryClickEvent e, ItemStack itemStack) {
        if (itemStack == null
                || itemStack.getType().equals(Material.AIR)) {
            ;
        } else {

            int itemprice = Integer.parseInt(config.getString("item." + itemStack.getItemMeta().getDisplayName() + ".price"));
            int playersmoney = Integer.parseInt(playerinfo.get(player.getName()).get("money"));
            if (itemprice <= playersmoney) {// 金額的には購入可能な場合
                if (config.contains("item." + itemStack.getItemMeta().getDisplayName() + ".whocanbuy")) {// whocanbuyがあれば確認
                    String whocanbuy = config.getString("item." + itemStack.getItemMeta().getDisplayName() + ".whocanbuy");
                    if (!whocanbuy.equalsIgnoreCase(playerinfo.get(player.getName()).get("job"))) {// 購入不可（whocanbuyを満たしていない）の場合
                        player.sendMessage(ChatColor.RED + whocanbuy + "のみが購入可能です");
                        e.setCancelled(true);
                        return;
                    }
                }
                int after = playersmoney - itemprice;
                playerinfo.get(player.getName()).replace("money", String.valueOf(after));
                Inventory inventory = player.getInventory();
                String itemname = itemStack.getItemMeta().getDisplayName();
                addItemMozToPlayer(inventory, itemname);
                e.setCancelled(true);
                player.sendMessage(ChatColor.AQUA + "" + itemprice + ChatColor.WHITE + "円払って<" + itemname + ChatColor.WHITE + ">を" + "購入しました! (残金: " + after + "円)");
                specialItems(player, e, itemStack);
                itemStack.setAmount(itemStack.getAmount() - 1);
            } else {// 購入不可の場合
                player.sendMessage(ChatColor.RED + "お金が足りません");
                e.setCancelled(true);
            }

        }
    }
}
