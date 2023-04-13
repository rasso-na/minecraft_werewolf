package com.mozumozu.power.mzgames;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.mozumozu.power.mzgames.Book.giveAdbook;
import static com.mozumozu.power.mzgames.ConfigClass.saveAndReloadConfig;
import static com.mozumozu.power.mzgames.Init.*;
import static com.mozumozu.power.mzgames.MzGames.config;
import static com.mozumozu.power.mzgames.Start.start;

public class Moz {

    public static boolean moz(Player player, String[] argList) {
        if (config.getInt("admin.name." + player.getName()) != 1) {// 実行者が管理者ではない場合は処理を終了してメッセージを表示
            player.sendMessage("このコマンドはサーバーの管理者(op所持者)のみが実行可能です。");
            return true;
        } else if (config.getInt("admin.name." + player.getName()) == 1) {// 実行者が管理者の場合はそのままコマンドを実行
            String arg0 = argList.length == 0 ? "" : argList[0];// 第１引数がなければ第１引数を空欄にする
            if (arg0.equals("")) {// 第１引数がなかった場合にはメッセージを表示して終了する
                player.sendMessage("このコマンドはサブコマンド必須です。/moz [サブコマンド] で使用してください。");
                return true;
            }
            //      [第１引数のみで使うコマンド]
            if (argList.length == 1) {// 第1引数のみの場合
                if (arg0.equalsIgnoreCase("setspawn")) {

                    config.set("spawn.spawnpoint.world", player.getWorld().getName());
                    config.set("spawn.spawnpoint.x", player.getLocation().getX());
                    config.set("spawn.spawnpoint.y", player.getLocation().getY());
                    config.set("spawn.spawnpoint.z", player.getLocation().getZ());
                    config.set("spawn.spawnpoint.yaw", player.getLocation().getYaw());
                    config.set("spawn.spawnpoint.pitch", player.getLocation().getPitch());
                    saveAndReloadConfig();
                    World world = Bukkit.getServer().getWorld(config.getString("spawn.spawnpoint.world"));
                    double x = config.getDouble("spawn.spawnpoint.x");
                    double y = config.getDouble("spawn.spawnpoint.y");
                    double z = config.getDouble("spawn.spawnpoint.z");
                    float yaw = (float) config.getDouble("spawn.spawnpoint.yaw");
                    float pitch = (float) config.getDouble("spawn.spawnpoint.pitch");
                    spawnpoint = new Location(world, x, y, z, yaw, pitch);
                    player.sendMessage("---------------");
                    player.sendMessage("[新しいスポーン地点]");
                    player.sendMessage(ChatColor.GOLD + "world: " + ChatColor.WHITE + world.getName());
                    player.sendMessage(ChatColor.GOLD + "location: " + ChatColor.WHITE + "X: " + String.format("%.1f", x) + ", Y: " + String.format("%.1f", y) + ", Z: " +  String.format("%.1f", z));
                    player.sendMessage(ChatColor.GOLD + "angle: " + ChatColor.WHITE + "yaw: " + String.format("%.1f", yaw) + ", pitch: " + String.format("%.1f", pitch));
                    player.sendMessage("---------------");

                } else if (arg0.equalsIgnoreCase("respawn")) {

                    if (spawnpoint != null) {// spawnpointがあった場合
                        player.teleport(spawnpoint);
                        player.sendMessage("スポーン地点に移動しました");
                    } else {// spawnpointがnullだった場合
                        player.sendMessage("spawnpointが設定されていません。/moz setspawn で設定してください");
                    }

                } else if (arg0.equalsIgnoreCase("setadmin")) {

                    config.set("spawn.admin.world", player.getWorld().getName());
                    config.set("spawn.admin.x", player.getLocation().getX());
                    config.set("spawn.admin.y", player.getLocation().getY());
                    config.set("spawn.admin.z", player.getLocation().getZ());
                    config.set("spawn.admin.yaw", player.getLocation().getYaw());
                    config.set("spawn.admin.pitch", player.getLocation().getPitch());
                    saveAndReloadConfig();
                    World world = Bukkit.getServer().getWorld(config.getString("spawn.admin.world"));
                    double x = config.getDouble("spawn.admin.x");
                    double y = config.getDouble("spawn.admin.y");
                    double z = config.getDouble("spawn.admin.z");
                    float yaw = (float) config.getDouble("spawn.admin.yaw");
                    float pitch = (float) config.getDouble("spawn.admin.pitch");
                    admin = new Location(world, x, y, z, yaw, pitch);
                    player.sendMessage("---------------");
                    player.sendMessage("[管理室]");
                    player.sendMessage(ChatColor.GOLD + "world: " + ChatColor.WHITE + world.getName());
                    player.sendMessage(ChatColor.GOLD + "location: " + ChatColor.WHITE + "X: " + String.format("%.1f", x) + ", Y: " + String.format("%.1f", y) + ", Z: " +  String.format("%.1f", z));
                    player.sendMessage(ChatColor.GOLD + "angle: " + ChatColor.WHITE + "yaw: " + String.format("%.1f", yaw) + ", pitch: " + String.format("%.1f", pitch));
                    player.sendMessage("---------------");

                } else if (arg0.equalsIgnoreCase("admin")) {

                    if (admin != null) {// adminがあった場合
                        player.teleport(admin);
                        player.sendMessage("管理室に移動しました");
                    } else {// adminがnullだった場合
                        player.sendMessage("adminが設定されていません。/moz setadmin で設定してください");
                    }
                } else if (arg0.equalsIgnoreCase("start")) {
                    start();
                } else if (arg0.equalsIgnoreCase("debug")) {
                    player.sendMessage("jobs: " + jobs);
                    player.sendMessage("playerinfo: " + playerinfo);
                    player.sendMessage("gameinfo: " + gameinfo);
                } else if (arg0.equalsIgnoreCase("init")) {
                    allinit();
                    Bukkit.broadcastMessage("初期化が完了しました");
                } else if (arg0.equalsIgnoreCase("adbook")) {
                    giveAdbook(player);
                }

            } else if (argList.length >= 2) {// 引数が2つ以上の場合
                String arg1 = argList[1];
                if (arg0.equalsIgnoreCase("gamemode")) {// ゲームモード変更
                    if (arg1.equalsIgnoreCase("mishitei")) {
                        gamemode = "未指定";
                    } else if (arg1.equalsIgnoreCase("jinrou")) {
                        gamemode = "人狼";
                    } else if (arg1.equalsIgnoreCase("onigokko")) {
                        gamemode = "鬼ごっこ";
                    } else {
                        player.sendMessage("存在しないゲームモードです");
                        return true;
                    }
                    player.sendMessage("ゲームモードを " + gamemode + " に変更しました");

                } else if (arg0.equalsIgnoreCase("debug")) {
                    /*
                    if (arg1.equalsIgnoreCase("inv")) {
                        if (gamemode.equalsIgnoreCase("未指定")) {
                            player.sendMessage("ゲームモード指定しろカス");
                            return true;
                        }
                        player.openInventory(shop);
                    }
                     */
                } else if (arg0.equalsIgnoreCase("num")) {
                    String arg2;
                    if (argList.length >= 3) {
                        arg2 = argList[2];
                    } else {
                        player.sendMessage("引数の数が一致しません");
                        return true;
                    }

                    if (arg1.equalsIgnoreCase("jinrou")) {
                        List<String> args = new ArrayList<>(Arrays.asList(argList));
                        if (arg2.equalsIgnoreCase("now")) {
                            player.sendMessage(jobs.toString());
                            return true;
                        }
                        args.remove(0);
                        args.remove(0);
                        List<Integer> argnums = new ArrayList<>();
                        for (String num : args) {
                            boolean result = false;
                            Pattern pattern = Pattern.compile("^[0-9]+$");
                            result = pattern.matcher(num).matches();
                            if (result) {
                                int number = Integer.parseInt(num);
                                argnums.add(number);
                            }
                        }
                        if (argnums.size() == jobs.get("人狼").keySet().size()) {
                            jobs.get("人狼").put("村人", argnums.get(0));
                            jobs.get("人狼").put("人狼", argnums.get(1));
                            jobs.get("人狼").put("狂人", argnums.get(2));
                            jobs.get("人狼").put("占い", argnums.get(3));
                            jobs.get("人狼").put("霊媒", argnums.get(4));
                            jobs.get("人狼").put("ﾒﾀﾓﾝ", argnums.get(5));
                            jobs.get("人狼").put("透視", argnums.get(6));
                        } else {
                            player.sendMessage("引数の数が一致しません");
                        }
                        return true;
                    } else if (arg1.equalsIgnoreCase("mur")
                            || arg1.equalsIgnoreCase("jin")
                            || arg1.equalsIgnoreCase("kyo")
                            || arg1.equalsIgnoreCase("ura")
                            || arg1.equalsIgnoreCase("rei")
                            || arg1.equalsIgnoreCase("met")
                            || arg1.equalsIgnoreCase("tou")) {// 役職の数を変更するコマンド

                        boolean result = false;
                        Pattern pattern = Pattern.compile("^[0-9]+$");
                        result = pattern.matcher(arg2).matches();
                        if (result) {
                            // if arg2 == int
                            if (arg1.equalsIgnoreCase("mur")) {
                                jobs.get("人狼").put("村人", Integer.parseInt(arg2));
                            } else if (arg1.equalsIgnoreCase("jin")) {
                                jobs.get("人狼").put("人狼", Integer.parseInt(arg2));
                            } else if (arg1.equalsIgnoreCase("kyo")) {
                                jobs.get("人狼").put("狂人", Integer.parseInt(arg2));
                            } else if (arg1.equalsIgnoreCase("ura")) {
                                jobs.get("人狼").put("占い", Integer.parseInt(arg2));
                            } else if (arg1.equalsIgnoreCase("rei")) {
                                jobs.get("人狼").put("霊媒", Integer.parseInt(arg2));
                            } else if (arg1.equalsIgnoreCase("met")) {
                                jobs.get("人狼").put("ﾒﾀﾓﾝ", Integer.parseInt(arg2));
                            } else if (arg1.equalsIgnoreCase("tou")) {
                                jobs.get("人狼").put("透視", Integer.parseInt(arg2));
                            }
                        }
                    } else if (arg1.equalsIgnoreCase("ono")) {// 人狼斧の初期値を変更する
                        boolean result = false;
                        Pattern pattern = Pattern.compile("^[0-9]+$");
                        result = pattern.matcher(arg2).matches();
                        if (result) {
                            config.set("item.人狼.shop.人狼斧.num", Integer.parseInt(arg2));
                            saveAndReloadConfig();
                            Bukkit.broadcastMessage("人狼斧の初期入荷本数を " + config.getString("item.人狼.shop.人狼斧.num") + " 本に変更しました");
                        }
                    } else if (arg1.equalsIgnoreCase("money")) {// 人狼斧の初期値を変更する
                        if (onGame) {
                            boolean result = false;
                            Pattern pattern = Pattern.compile("^[0-9]+$");
                            result = pattern.matcher(arg2).matches();
                            if (result) {
                                playerinfo.get(player.getName()).put("money", arg2);
                                player.sendMessage("所持金を " + playerinfo.get(player.getName()).get("money") + " 円に変更しました");
                            }
                        } else {// ゲーム中でない場合
                            player.sendMessage("ゲーム中に使用してください");
                        }
                    }
                } 
            }
        }
        return true;
    }
}
