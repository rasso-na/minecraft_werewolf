package com.mozumozu.power.mzgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mozumozu.power.mzgames.Init.*;
import static com.mozumozu.power.mzgames.MzGames.config;


public class ChestClass {

    static Random random = new Random();

    public static void chestFill(Inventory inventory) {
        if (inventory.getHolder() instanceof Chest) {
            if (chestfill == 0) {return;}// chestfillが0の場合は何もしない
            if (!gameinfo.get("chestopened").containsKey(inventory.toString())) {// まだチェストが一度も登録されていない場合
                addOrderPaperToChest(inventory);
                gameinfo.get("chestopened").put(inventory.toString(), chestfill);
            } else {// キーは含んでいる→登録はされている場合
                if (gameinfo.get("chestopened").get(inventory.toString()) < chestfill) {// 登録されてはいるが指定回数分満たされていない場合
                    addOrderPaperToChest(inventory);
                    gameinfo.get("chestopened").put(inventory.toString(), chestfill);
                }
            }
        }
    }

    public static void addOrderPaperToChest(Inventory inventory) {
        ItemStack itemStack = new ItemStack(Material.matchMaterial(config.getString("item.発注書.base")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("発注書");
        List<String> lore = new ArrayList<>();
        for (String desc : config.getConfigurationSection("item.発注書.description").getKeys(false)) {
            if (!desc.equalsIgnoreCase("usage") && !desc.equalsIgnoreCase("times")) {// usage と times 以外を追加
                lore.add(config.getString("item.発注書.description." + desc));
            }
        }
        if (config.contains("item.発注書.description.usage")) {// usageがあれば設定
            lore.add(ChatColor.GRAY + "使用方法: " + ChatColor.WHITE + config.getString("item.発注書.description.usage"));
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        inventory.clear();
        int r;
        int max = 5;
        int now = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            r = random.nextInt(10);// 各スロットに対して10分の一の確率でダイヤモンドが生成
            if (r == 0 && now < max) {// あたりで且つmaxに満たないときに追加
                inventory.setItem(i, itemStack);
                now++;
            }
        }
    }
}
