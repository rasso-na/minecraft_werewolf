package com.mozumozu.power.mzgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.mozumozu.power.mzgames.Init.*;
import static com.mozumozu.power.mzgames.MzGames.config;
import static com.mozumozu.power.mzgames.MzGames.plugin;
import static org.bukkit.Bukkit.getLogger;

public class InventoryClass {

    public static void fillInv(Player player, Inventory inventory, String forwhat) {// inventoryの中身を消去して.ymlにあるinvnameの中のアイテムをinventoryに充填する
        inventory.clear();// 指定インベントリの全消去（空にする）
        String job = playerinfo.get(player.getName()).get("job");
        for (String itemname : config.getConfigurationSection("item").getKeys(false)) {// 各アイテムごとに処理を開始
            List<String> condition_gamemode = Arrays.asList(config.getString("item." + itemname + ".gamemode").split(" "));
            List<String> condition_forwhat = Arrays.asList(config.getString("item." + itemname + ".forwhat").split(" "));
            if(condition_gamemode.contains(gamemode) && condition_forwhat.contains(forwhat)) {// 現在のゲームモードの該当インベントリ（ショップとか）であればそのアイテムを追加
                if (config.contains("item." + itemname + ".whocanbuy")) {// whocanbuyがあれば確認
                    String whocanbuy = config.getString("item." + itemname + ".whocanbuy");
                    if (!job.equalsIgnoreCase(whocanbuy)) {// whocanbuyの対象でなければ追加されない
                        continue;
                    }
                }
                for (int i = 0; i < config.getInt("item." + itemname + ".num"); i++) {// アイテムごとに指定された個数分addItemMozを実行
                    addItemMoz(player, inventory, itemname);
                }
            }
        }
    }

    public static void createEnderChestShop() {
        //ショップ生成
        for (Player player : Bukkit.getOnlinePlayers()) {
            fillInv(player, player.getEnderChest(), "shop");
        }
    }

    public static void giveFirstItem() {
        for (Player player : Bukkit.getOnlinePlayers()) {// 全プレイヤーに初期アイテムを配布
            fillInv(player, player.getInventory(), "firstitem");
        }
    }

    public static void addItemMoz(Player player, Inventory inventory, String itemname) {
        ItemStack itemStack = new ItemStack(Material.matchMaterial(config.getString("item." + itemname + ".base")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemname);
        List<String> lore = new ArrayList<>();
        for (String desc : config.getConfigurationSection("item." + itemname + ".description").getKeys(false)) {
            if (!desc.equalsIgnoreCase("usage") && !desc.equalsIgnoreCase("times")) {// usage と times 以外を追加
                lore.add(config.getString("item." + itemname + ".description." + desc));
            }
        }
        if (config.contains("item." + itemname + ".description.usage")) {// usageがあれば設定
            lore.add(ChatColor.GRAY + "使用方法: " + ChatColor.WHITE + config.getString("item." + itemname + ".description.usage"));
        }
        if (config.contains("item." + itemname + ".description.times")) {// times があれば設定
            lore.add(ChatColor.GRAY + "使用可能回数: " + ChatColor.WHITE + config.getString("item." + itemname + ".description.times") + "回");
        }
        if (config.contains("item." + itemname + ".whocanbuy")) {// whocanbuyがあれば設定
            lore.add(ChatColor.GRAY + "購入可能: " + ChatColor.WHITE + config.getString("item." + itemname + ".whocanbuy"));
        }
        if (config.contains("item." + itemname + ".price")) {// priceがあれば設定
            lore.add(ChatColor.GRAY + "価格: " + ChatColor.WHITE + config.getString("item." + itemname + ".price") + "円");
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        if (config.contains("item." + itemname + ".durability")) {// 耐久値があれば耐久値設定
            itemStack.setDurability((short) Integer.parseInt(config.getString("item." + itemname + ".durability")));
        }
        if (config.contains("item." + itemname + ".enchantment")) {// エンチャントがあればエンチャント設定
            for (String ench : config.getConfigurationSection("item." + itemname + ".enchantment").getKeys(false)) {
                String enchantname = config.getString("item." + itemname + ".enchantment." + ench).split(":")[0];
                int enchantlvl = Integer.parseInt(config.getString("item." + itemname + ".enchantment." + ench).split(":")[1]);
                //do your thing
                itemStack.addUnsafeEnchantment(Enchantment.getByName(enchantname), enchantlvl);
            }
        }
        Map<Integer, ItemStack> addItem = inventory.addItem(itemStack);
        if (onGame) {
            if (addItem.size() == 0) {// addItemに成功した場合
                player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "[入荷] " + ChatColor.WHITE + itemname);
            } else {// addItemに失敗した場合
                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[入荷不可] " + ChatColor.GRAY + itemname + " <- ショップ満杯につき");
            }
        }
    }

    public static void addItemMozToPlayer(Inventory inventory, String itemname) {
        ItemStack itemStack = new ItemStack(Material.matchMaterial(config.getString("item." + itemname + ".base")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemname);
        List<String> lore = new ArrayList<>();
        for (String desc : config.getConfigurationSection("item." + itemname + ".description").getKeys(false)) {
            if (!desc.equalsIgnoreCase("usage") && !desc.equalsIgnoreCase("times")) {// usage と times 以外を追加
                lore.add(config.getString("item." + itemname + ".description." + desc));
            }
        }
        if (config.contains("item." + itemname + ".description.usage")) {// usageがあれば設定
            lore.add(ChatColor.GRAY + "使用方法: " + ChatColor.WHITE + config.getString("item." + itemname + ".description.usage"));
        }
        if (config.contains("item." + itemname + ".description.times")) {// times があれば設定
            lore.add(ChatColor.GRAY + "使用可能回数: " + ChatColor.WHITE + config.getString("item." + itemname + ".description.times") + "回");
        }
        if (config.contains("item." + itemname + ".whocanbuy")) {// whocanbuyがあれば設定
            lore.add(ChatColor.GRAY + "購入可能: " + ChatColor.WHITE + config.getString("item." + itemname + ".whocanbuy"));
        }
        if (config.contains("item." + itemname + ".price")) {// priceがあれば設定
            lore.add(ChatColor.GRAY + "価格: " + ChatColor.WHITE + config.getString("item." + itemname + ".price") + "円");
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        if (config.contains("item." + itemname + ".durability")) {// 耐久値があれば耐久値設定
            itemStack.setDurability((short) Integer.parseInt(config.getString("item." + itemname + ".durability")));
        }
        if (config.contains("item." + itemname + ".enchantment")) {// エンチャントがあればエンチャント設定
            for (String ench : config.getConfigurationSection("item." + itemname + ".enchantment").getKeys(false)) {
                String enchantname = config.getString("item." + itemname + ".enchantment." + ench).split(":")[0];
                int enchantlvl = Integer.parseInt(config.getString("item." + itemname + ".enchantment." + ench).split(":")[1]);
                //do your thing
                itemStack.addUnsafeEnchantment(Enchantment.getByName(enchantname), enchantlvl);
            }
        }
        inventory.addItem(itemStack);
    }

}
