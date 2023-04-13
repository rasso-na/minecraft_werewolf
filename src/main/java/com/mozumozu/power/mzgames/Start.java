package com.mozumozu.power.mzgames;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


import static com.mozumozu.power.mzgames.DeathClass.clearDeadBody;
import static com.mozumozu.power.mzgames.GameTimer.gameTimer;
import static com.mozumozu.power.mzgames.Init.*;
import static com.mozumozu.power.mzgames.InventoryClass.createEnderChestShop;
import static com.mozumozu.power.mzgames.InventoryClass.giveFirstItem;
import static com.mozumozu.power.mzgames.MzGames.plugin;
import static com.mozumozu.power.mzgames.NameTag.hideNameTag;
import static com.mozumozu.power.mzgames.Start_JINROU.start_JINROU;
import static com.mozumozu.power.mzgames.Start_ONIGOKKO.start_ONIGOKKO;

public class Start {

    public static void start() {
        if (gamemode.equalsIgnoreCase("未指定")) {
            Bukkit.broadcastMessage("ゲームモードが " + gamemode + " です。");// 「ゲームモードが 未指定 です。」
            return;
        }

        if (gameinfo.get("resetabletasks").get("countdown") == null) {// カウントダウンタスクが動いていない場合

            gameinit(); // 初期化

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
                    playerinfo.put(player.getName(), null);// 参加者（スペクテイター以外）をmapに格納
                }
            }

            if (gamemode.equalsIgnoreCase("人狼")) {// 人狼のときは人数確認
                int jobnumsum = 0;
                for (String jobname : jobs.get(gamemode).keySet()){// 当該ゲームモードの役職名を取得
                    jobnumsum = jobnumsum + jobs.get(gamemode).get(jobname);
                }
                if (playerinfo.keySet().size() != jobnumsum) {// 参加者人数が役職設定数と違ったら処理停止
                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "\n参加人数と役職数が合っていません。\n"
                            + ChatColor.WHITE + "役職数合計: " + jobnumsum + "人 =/= 参加人数: " + playerinfo.keySet().size() + "人\n"
                            + "なお、スペクテイターモードのプレイヤーは観戦者となるため加算されませんのでご注意ください。");
                    return;
                }// 違わなかったら（人数があっていたら）特に何もせず先に進む
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.getEnderChest().clear();// 全員のエンダーチェスト消去
            }

            clearDeadBody();// 死体を消去
            hideNameTag();//ネームタグを隠す

            Bukkit.broadcastMessage(ChatColor.BOLD + "\n=====================================");
            Bukkit.broadcastMessage("まもなく " + ChatColor.GOLD + "" + ChatColor.BOLD + gamemode + ChatColor.WHITE + " を開始します......");
            Bukkit.broadcastMessage("現時点でスペクテイターモードのプレイヤーは観戦者となります。");
            Bukkit.broadcastMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "参加プレイヤー" + ChatColor.WHITE + "(" + playerinfo.keySet().size() + "人)" + ChatColor.BLUE + "" + ChatColor.BOLD + ": " + ChatColor.WHITE + playerinfo.keySet());

            startcounter = 5;// スタートまでのカウントを設定
            BukkitTask countdown = new BukkitRunnable() {
                @Override
                public void run() {

                    if (0 < startcounter && startcounter <= 3) { // 3カウントだけ
                        String countdowntitle = ChatColor.YELLOW + "= " + ChatColor.RED + startcounter + ChatColor.YELLOW + " =";
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendTitle(countdowntitle, "");
                            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
                        }
                    }

                    /************************************************
                     *
                     * 　gamemodeごとの処理はカウント3~0の間のみにする。
                     * 　　　　　　　　　　　　　　　　　　　ここだけ条件分岐で。
                     *
                     * 　カウント 0 のときの共通処理は下にあるので
                     * 　　　　　　　　　　gamemode別の部分には書かなくてよい。
                     *
                     ************************************************/

                    start_JINROU();

                    start_ONIGOKKO();

                    /************************************************
                     *
                     * 　ここまでgamemodeごとの処理。
                     * 　　　　　　　　　　　　　　　以下、共通処理。
                     *
                     ************************************************/

                    if (startcounter == 0) {// カウント0のときの共通処理！！！！

                        createEnderChestShop();// ショップを生成
                        giveFirstItem();// 初期アイテム配布

                        String START = ChatColor.DARK_RED + "" + gamemode + ChatColor.YELLOW + " スタート" + ChatColor.WHITE + "!!!";
                        for (Player p : Bukkit.getOnlinePlayers()) {// ゲームタイトル表示（開始宣言）
                            p.sendTitle(START, "");
                            p.playSound(p.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
                        }
                        Bukkit.getScheduler().cancelTask(gameinfo.get("resetabletasks").get("countdown"));// カウントダウンタスクを終了
                        gameinfo.get("resetabletasks").put("countdown", null);
                        gameTimer();// ゲームタイマーをスタート
                        onGame = true;
                    }

                    startcounter--;

                }
            }.runTaskTimer(plugin, 0L, 20L);// １秒毎に実行する
            gameinfo.get("resetabletasks").put("countdown", countdown.getTaskId());

        } else {// すでにカウントダウンが開始されている場合
            Bukkit.broadcastMessage("すでに開始されています。");
        }
    }
}


