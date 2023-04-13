package com.mozumozu.power.mzgames;

import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mozumozu.power.mzgames.DeathClass.setDeadBody;
import static com.mozumozu.power.mzgames.Init.*;
import static com.mozumozu.power.mzgames.MzGames.config;
import static com.mozumozu.power.mzgames.SkillClass.*;

public class ItemClass {

    public static void dekoi(Player player, ItemStack having) {
        ItemMeta itemMeta = having.getItemMeta();
        if (having.getType().equals(Material.ARMOR_STAND)) {
            if (itemMeta.getDisplayName().equalsIgnoreCase("**********")) {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                setDeadBody(player);
            }
        }
    }

    public static void hikaru(Player player, ItemStack having) {
        ItemMeta itemMeta = having.getItemMeta();
        if (having.getType().equals(Material.GLOWSTONE_DUST)) {
            if (itemMeta.getDisplayName().equalsIgnoreCase("**********")) {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                Bukkit.broadcastMessage("**********");
                Bukkit.broadcastMessage("**********");
                for (String playername : playerinfo.keySet()) {
                    if (Bukkit.getServer().getPlayer(playername) != null) {
                        if (playerinfo.get(playername).get("death").equalsIgnoreCase("false")) {// if player is alive
                            Bukkit.getServer().getPlayer(playername).playSound(Bukkit.getServer().getPlayer(playername).getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
                            Bukkit.getServer().getPlayer(playername).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 30 * 20, 1, true, false));
                        }
                    }
                }
            }
        }
    }

    public static void toumei(Player player, ItemStack having) {
        ItemMeta itemMeta = having.getItemMeta();
        if (having.getType().equals(Material.LEATHER)) {
            if (itemMeta.getDisplayName().equalsIgnoreCase("**********")) {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                player.sendMessage("**********");
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30 * 20, 1, true, false));
            }
        }
    }

    public static void seizonnsha(Player player, ItemStack having) {
        ItemMeta itemMeta = having.getItemMeta();
        List<String> alive = new ArrayList<>();
        if (having.getType().equals(Material.WITHER_ROSE)) {
            if (itemMeta.getDisplayName().equalsIgnoreCase("**********")) {
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                for (String playername : playerinfo.keySet()) {
                    if (playerinfo.get(playername).get("death").equalsIgnoreCase("false")) {// if player is alive
                        alive.add(playername);
                    }
                }
                player.sendMessage(ChatColor.AQUA + "" + alive.size() + ChatColor.WHITE + " **********");
            }
        }
    }

    public static void fire(Player damagerplayer, ItemStack having) {
        ItemMeta itemMeta = having.getItemMeta();
        if (having.getType().equals(Material.BLAZE_POWDER)) {
            if (itemMeta.getDisplayName().equalsIgnoreCase("**********")) {
                damagerplayer.getItemInHand().setAmount(damagerplayer.getItemInHand().getAmount() - 1);
            }
        }
    }

    public static void jobball(Player player, ItemStack having, PlayerInteractEvent e) {
        ItemMeta itemMeta = having.getItemMeta();
        if (e.getHand().equals(EquipmentSlot.HAND)) {// ２回判定をなくす
            if (having.getType().equals(Material.HEART_OF_THE_SEA)) {
                if (itemMeta.getDisplayName().equalsIgnoreCase("**********")) {
                    String playersjob = playerinfo.get(player.getName()).get("job");
                    if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {// 右クリックの場合
                        // スキル使用処理
                        if (playersjob.equalsIgnoreCase("占い")) {
                            uraSkill(player);
                        } else if (playersjob.equalsIgnoreCase("霊媒")) {
                            reiSkill(player);
                        } else if (playersjob.equalsIgnoreCase("ﾒﾀﾓﾝ")) {
                            metSkill(player);
                        } else if (playersjob.equalsIgnoreCase("透視")) {
                            touSkill(player);
                        }
                    }
                    if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {// 左クリックの場合
                        // スキル詳細確認処理
                        String skillname = null;
                        for (String name : config.getConfigurationSection("skill").getKeys(false)) {
                            List<String> condition_gamemode = Arrays.asList(config.getString("skill." + name + ".gamemode").split(" "));
                            List<String> condition_job = Arrays.asList(config.getString("skill." + name + ".job").split(" "));
                            if (condition_job.contains(playerinfo.get(player.getName()).get("job")) && condition_gamemode.contains(gamemode)) {// 現在のゲームモードのスキルであれば
                                skillname = name;//スキルの名前を取得
                            }
                        }
                        List<String> lore = new ArrayList<>();
                        for (String desc : config.getConfigurationSection("skill." + skillname + ".description").getKeys(false)) {
                            if (!desc.equalsIgnoreCase("usage") && !desc.equalsIgnoreCase("times")) {// usage と times 以外を追加
                                lore.add("  " + ChatColor.WHITE + config.getString("skill." + skillname + ".description." + desc));
                            }
                        }
                        if (config.contains("skill." + skillname + ".description.usage")) {// usageがあれば設定
                            lore.add(ChatColor.GRAY + "[使用方法] " + ChatColor.WHITE + config.getString("skill." + skillname + ".description.usage"));
                        }
                        if (config.contains("skill." + skillname + ".description.times")) {// times があれば設定
                            lore.add(ChatColor.GRAY + "[使用可能回数] " + ChatColor.WHITE + playerinfo.get(player.getName()).get("skill") + "回");
                        }
                        player.sendMessage("\n********************=========");
                        player.sendMessage(ChatColor.GRAY + "[スキル] " + ChatColor.YELLOW + "" + ChatColor.BOLD + skillname);
                        player.sendMessage(ChatColor.GRAY + "[詳細]");
                        for (String s : lore) {
                            player.sendMessage(s);
                        }
                        player.sendMessage("********************=========");
                    }
                }
            }
        }
    }

    public static void namestickPlayerInteract(Player player, ItemStack having, PlayerInteractEvent e) {
        ItemMeta itemMeta = having.getItemMeta();
        if (having.getType().equals(Material.CARROT_ON_A_STICK)) {
            if (itemMeta.getDisplayName().equalsIgnoreCase("**********")) {
                // 参加者一覧の表示
                StringBuilder participants = new StringBuilder();
                for (String playername : playerinfo.keySet()) {
                    participants.append(ChatColor.WHITE + playername + ChatColor.GRAY + ".  ");
                }
                player.sendMessage(ChatColor.GRAY + "[参加者一覧] " + participants);
            }
        }
    }

    public static void namestickDamage(Player damagedplayer, Player damagerplayer, ItemStack having, EntityDamageByEntityEvent e) {
        ItemMeta itemMeta = having.getItemMeta();
        if (having.getType().equals(Material.CARROT_ON_A_STICK)) {
            if (itemMeta.getDisplayName().equalsIgnoreCase("**********")) {
                e.setCancelled(true);// イベントをキャンセル
                // 殴った側に殴られた側の名前を表示（チャットで）
                damagerplayer.sendMessage(ChatColor.GRAY + "[名前] " + ChatColor.WHITE + damagedplayer.getName());
            }
        }
    }


    public static void specialItems(Player player, InventoryClickEvent e, ItemStack itemStack) {
        // 専用アイテム（人狼斧以外）の処理
        if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("**********")) {
//            e.setCancelled(true);
//            int amount = itemStack.getAmount();
//            itemStack.setAmount(0);// アイテムの削除
            for (int i = 0; i < player.getInventory().getSize(); i++){
                if (player.getInventory().getItem(i) != null) {
                    if (player.getInventory().getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("**********")) {
                        player.getInventory().getItem(i).setAmount(0);
                    }
                }
            }
            int beforeuse = Integer.parseInt(playerinfo.get(player.getName()).get("skill"));
            int afteruse = beforeuse + 1;
            playerinfo.get(player.getName()).put("skill", String.valueOf(afteruse));
            player.sendMessage("占い可能回数が " + afteruse + " 回になりました");
        } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("**********")) {
//            e.setCancelled(true);
//            int amount = itemStack.getAmount();
//            itemStack.setAmount(0);// アイテムの削除
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) != null) {
                    if (player.getInventory().getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("**********")) {
                        player.getInventory().getItem(i).setAmount(0);
                    }
                }
            }

            int beforeuse = Integer.parseInt(playerinfo.get(player.getName()).get("skill"));
            int afteruse = beforeuse + 1;
            playerinfo.get(player.getName()).put("skill", String.valueOf(afteruse));
            player.sendMessage("霊媒可能回数が " + afteruse + " 回になりました");
        } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("**********")) {
//            e.setCancelled(true);
//            int amount = itemStack.getAmount();
//            itemStack.setAmount(0);// アイテムの削除
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) != null) {
                    if (player.getInventory().getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("**********")) {
                        player.getInventory().getItem(i).setAmount(0);
                    }
                }
            }
//            for (int i = 0; i < amount; i++) {
            if (wolfs.size() != 0) {// まだ特定されていない人狼がいた場合
                player.sendMessage("**********");
                wolfs.remove(0);
            } else {// すでにすべての人狼が特定されていた場合
                player.sendMessage("**********");
            }
//                if (e.getAction().equals(InventoryAction.PICKUP_ONE)
//                        || e.getAction().equals(InventoryAction.PICKUP_SOME)
//                        || e.getAction().equals(InventoryAction.PICKUP_HALF)
//                        || e.getAction().equals(InventoryAction.PICKUP_ALL)) {// アイテムをピックするとき
//
//                } else if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {// シフトクリック
//                }
//            }
        }
    }
}
