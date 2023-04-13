package com.mozumozu.power.mzgames;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;
import static com.mozumozu.power.mzgames.Init.*;


public class Book {

    String title;
    String author;
    String currentPage = "";
    int numPages = 0;
    int numLines = 0;

    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta bookMeta = (BookMeta) book.getItemMeta();

    public void setName(String name) {
        bookMeta.setDisplayName(name);
    }

    public void setTitle(String title) {
        this.title = title;
        bookMeta.setTitle(title);
    }

    public void setAuthor(String author) {
        this.author = author;
        bookMeta.setAuthor(author);
    }

    public void addPage () {
        bookMeta.addPage(currentPage);
        ++numPages;
    }

    public void addToPage (BaseComponent[] page) {
        bookMeta.spigot().addPage(page);
    }

    public void addPage2 (Component page2) {
        bookMeta.addPages(page2);
    }

    public void addInfo() {
        book.setItemMeta(bookMeta);
    }

    public void giveBook(Player player) {
        if (player.getInventory().firstEmpty() != -1)
        {
            player.getInventory().addItem(book);
        }
        else
        {
            player.sendMessage(ChatColor.RED + "インベントリがいっぱいです");
        }
    }

    public static void giveAdbook (Player player){
        Book book = new Book();

        book.setName(ChatColor.YELLOW + "" + ChatColor.BOLD + "管理本");
        book.setTitle("");
        book.setAuthor("");


        /********************************************************
         *
         * 　　　　　　　　　　　管理本（1ページ目）
         *
         ********************************************************/

        ComponentBuilder page1_tc = new ComponentBuilder();

        TextComponent gamemode_midashi = new TextComponent(ChatColor.GRAY + "" + ChatColor.BOLD + "[ゲームモード]");
        gamemode_midashi.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "1"));
        page1_tc.append(gamemode_midashi);

        TextComponent gamemode_mishitei = new TextComponent(ChatColor.BLUE + "\n未指定 ");
        gamemode_mishitei.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz gamemode mishitei"));
        page1_tc.append(gamemode_mishitei);

        TextComponent gamemode_jinrou = new TextComponent(ChatColor.BLUE + "人狼 ");
        gamemode_jinrou.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz gamemode jinrou"));
        page1_tc.append(gamemode_jinrou);

        TextComponent game_manage = new TextComponent(ChatColor.GRAY + "" + ChatColor.BOLD + "\n\n[ゲーム管理]");
        game_manage.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "1"));
        page1_tc.append(game_manage);

        TextComponent game_start = new TextComponent(ChatColor.BLUE + "\n*** START ***");
        game_start.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz start"));
        page1_tc.append(game_start);

        TextComponent force_setspawn = new TextComponent(ChatColor.RED + "" + ChatColor.BOLD + "\n※開始前にスポーン地点を設定してください");
        force_setspawn.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "1"));
        page1_tc.append(force_setspawn);

        TextComponent others_midashi = new TextComponent(ChatColor.GRAY + "" + ChatColor.BOLD + "\n\n[その他]");
        others_midashi.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "1"));
        page1_tc.append(others_midashi);

        TextComponent init = new TextComponent(ChatColor.BLUE + "\n初期化 ");
        init.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz init"));
        page1_tc.append(init);

        TextComponent setspawn = new TextComponent(ChatColor.BLUE + "\nスポーン地点をここにする");
        setspawn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz setspawn"));
        page1_tc.append(setspawn);

        TextComponent admin = new TextComponent(ChatColor.BLUE + "\n管理室へ移動");
        admin.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz admin"));
        page1_tc.append(admin);

        TextComponent tpall = new TextComponent(ChatColor.BLUE + "\n全員をここへtp");
        tpall.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp @a @s"));
        page1_tc.append(tpall);

        TextComponent setadmin = new TextComponent(ChatColor.BLUE + "\n管理室をここにする");
        setadmin.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz setadmin"));
        page1_tc.append(setadmin);

        BaseComponent[] page1_bc = page1_tc.create();
        book.addToPage(page1_bc);

        /********************************************************
         *
         * 　　　　　　　　　　　管理本（2ページ目）
         *
         ********************************************************/

        ComponentBuilder page2_tc = new ComponentBuilder();

        TextComponent jobs_midashi = new TextComponent(ChatColor.GRAY + "" + ChatColor.BOLD + "[役職設定]");
        jobs_midashi.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "2"));
        page2_tc.append(jobs_midashi);

        TextComponent jobs_jinrou = new TextComponent(ChatColor.GRAY + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "\n人狼");
        jobs_jinrou.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "2"));
        page2_tc.append(jobs_jinrou);

        for (String job : jobs.get("人狼").keySet()) {
            TextComponent job_midashi = new TextComponent(ChatColor.DARK_GRAY + "\n" + job + ": ");
            job_midashi.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "2"));
            page2_tc.append(job_midashi);

            for (int i = 0; i < 9; i++) {
                TextComponent job_num = new TextComponent(ChatColor.BLUE + "" + i + " ");
                if (job.equalsIgnoreCase("村人")) {
                    job_num.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz num mur " + i));
                } else if (job.equalsIgnoreCase("人狼")) {
                    job_num.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz num jin " + i));
                } else if (job.equalsIgnoreCase("狂人")) {
                    job_num.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz num kyo " + i));
                } else if (job.equalsIgnoreCase("占い")) {
                    job_num.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz num ura " + i));
                } else if (job.equalsIgnoreCase("霊媒")) {
                    job_num.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz num rei " + i));
                } else if (job.equalsIgnoreCase("ﾒﾀﾓﾝ")) {
                    job_num.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz num met " + i));
                } else if (job.equalsIgnoreCase("透視")) {
                    job_num.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moz num tou " + i));
                }
                page2_tc.append(job_num);
            }
        }


        BaseComponent[] page2_bc = page2_tc.create();
        book.addToPage(page2_bc);

        /********************************************************
         *
         * 　　　　　　　　　　　管理本（3ページ目）
         *
         ********************************************************/


        /*********************************************************
         *
         *    　　　　　　　　　本の内容ここまで
         *
         *********************************************************/

        book.addInfo();
        book.giveBook(player);
    }




}
