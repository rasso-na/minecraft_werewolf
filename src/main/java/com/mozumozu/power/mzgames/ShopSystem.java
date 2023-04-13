package com.mozumozu.power.mzgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.mozumozu.power.mzgames.ChestClass.random;
import static com.mozumozu.power.mzgames.Init.gamemode;
import static com.mozumozu.power.mzgames.Init.playerinfo;
import static com.mozumozu.power.mzgames.InventoryClass.addItemMoz;
import static com.mozumozu.power.mzgames.MzGames.config;

public class ShopSystem {

    public static void importItemsToEnderChest(Player player, Inventory inventory) {
        int added = 0;
        boolean exist = false;
        int r = 0;
        if(inventory.getType().equals(InventoryType.ENDER_CHEST)) {
            Inventory playerinventory = player.getInventory();
            for (int i = 0; i < playerinventory.getSize(); i++) {
                if (playerinventory.getItem(i) != null) {
                    ItemStack itemStack = playerinventory.getItem(i);
                    if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("発注書")) {
                        exist = true;
                        int amount = itemStack.getAmount();
                        playerinventory.clear(i);
                        for (int j = 0; j < amount; j++) {
                            Random random = new Random();
                            r = random.nextInt(200)+100;
                            added = added + r;
                            addRandomItemToPlayersEnderChest(player, inventory);
                        }
                    }
                }
            }
        }
        if (exist) {
            int now = Integer.parseInt(playerinfo.get(player.getName()).get("money"));
            playerinfo.get(player.getName()).put("money", String.valueOf(now + added));
            player.sendMessage(ChatColor.AQUA + String.valueOf(added) + ChatColor.WHITE + "円を手に入れた!!!");
        }
    }

    public static void addRandomItemToPlayersEnderChest(Player player, Inventory inventory){
        int sumw = 0;
        String job = playerinfo.get(player.getName()).get("job");
        for (String itemname : config.getConfigurationSection("item").getKeys(false)) {// アイテムごとに
            List<String> condition_gamemode = Arrays.asList(config.getString("item." + itemname + ".gamemode").split(" "));
            List<String> condition_forwhat = Arrays.asList(config.getString("item." + itemname + ".forwhat").split(" "));
            if(condition_gamemode.contains(gamemode) && condition_forwhat.contains("shop")) {// 現在のゲームモードのショップであれば
                if (config.contains("item." + itemname + ".whocanbuy")) {// whocanbuyがあれば確認
                    String whocanbuy = config.getString("item." + itemname + ".whocanbuy");
                    if (!job.equalsIgnoreCase(whocanbuy)) {// whocanbuyの対象でなければ加算されない
                        continue;
                    }
                }
                int itemw = Integer.parseInt(config.getString("item." + itemname + ".w"));//重みを取得
                sumw = sumw + itemw;
            }
        }
        int r = random.nextInt(sumw) + 1;
        int match = 0;
        for (String itemjudged : config.getConfigurationSection("item").getKeys(false)) {// アイテムごとに
            List<String> condition_gamemode = Arrays.asList(config.getString("item." + itemjudged + ".gamemode").split(" "));
            List<String> condition_forwhat = Arrays.asList(config.getString("item." + itemjudged + ".forwhat").split(" "));
            if(condition_gamemode.contains(gamemode) && condition_forwhat.contains("shop")) {// 現在のゲームモードのショップが対象のアイテムであれば
                if (config.contains("item." + itemjudged + ".whocanbuy")) {// whocanbuyがあれば確認
                    String whocanbuy = config.getString("item." + itemjudged + ".whocanbuy");
                    if (!job.equalsIgnoreCase(whocanbuy)) {// whocanbuyの対象でなければ追加されない
                        continue;
                    }
                }
                int itemw = Integer.parseInt(config.getString("item." + itemjudged + ".w"));//重みを取得
                for (int k = 0; k < itemw; k++) {
                    match++;
                    if (match == r) {
                        addItemMoz(player, inventory, itemjudged);
                    }
                }
            }
        }
    }
}
