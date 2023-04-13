package com.mozumozu.power.mzgames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class DeathClass {

    public static void setDeadBody(Player p) {// 死体の生成
        Location loc = p.getLocation();
        ArmorStand stand = (ArmorStand) loc.getWorld().spawn(loc, ArmorStand.class);
        stand.setGravity(false);
        stand.setBasePlate(false);
        stand.setVisible(true);
        stand.setCustomName(p.getName());
        stand.setCustomNameVisible(false);
        ItemStack helm = createHead(p.getName());
        stand.setHelmet(helm);
    }

    public static ItemStack createHead(String name) {// プレイヤーヘッドの生成
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(name);
        head.setItemMeta(meta);
        return head;
    }

    public static void clearDeadBody() {
        for (World w : Bukkit.getWorlds()) {// 死体アーマースタンドの破壊
            if (w.getEntitiesByClass(ArmorStand.class) != null && w.getEntitiesByClass(ArmorStand.class).size() != 0) {
                for (ArmorStand as : w.getEntitiesByClass(ArmorStand.class)) {
                    if (as.getCustomName() != null) {
                        as.remove();
                    }
                }
            }
        }
    }


}
