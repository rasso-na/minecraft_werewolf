package com.mozumozu.power.mzgames;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static com.mozumozu.power.mzgames.Book.giveAdbook;

public class Operate {

    public static void send_operatable_text(Player player) {
        giveAdbook(player);
    }

}
