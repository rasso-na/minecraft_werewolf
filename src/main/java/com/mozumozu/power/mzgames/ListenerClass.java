package com.mozumozu.power.mzgames;


import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


import static com.mozumozu.power.mzgames.Book.giveAdbook;
import static com.mozumozu.power.mzgames.ChestClass.chestFill;
import static com.mozumozu.power.mzgames.ConfigClass.saveAndReloadConfig;
import static com.mozumozu.power.mzgames.DeathClass.setDeadBody;
import static com.mozumozu.power.mzgames.Init.*;
import static com.mozumozu.power.mzgames.MzGames.config;
import static com.mozumozu.power.mzgames.MzGames.plugin;
import static com.mozumozu.power.mzgames.NameTag.hideNameTag;
import static com.mozumozu.power.mzgames.NameTag.showNameTag;
import static com.mozumozu.power.mzgames.ShopClass.buy;
import static com.mozumozu.power.mzgames.ItemClass.*;
import static com.mozumozu.power.mzgames.ShopSystem.importItemsToEnderChest;


public class ListenerClass
        implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        online = Bukkit.getOnlinePlayers().size();
        if(!config.getConfigurationSection("admin.name").getKeys(false).contains(player.getName())){// config.ymlの中に含まれていなかった場合には0にしていれる
            e.setJoinMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.BLACK + "・～・" + ChatColor.WHITE + "<初" + ChatColor.DARK_PURPLE + "] " + ChatColor.YELLOW + player.getName() + "**********");
            config.set("admin.name." + player.getName(), 0);
        } else {// すでに参加したことがある場合(config.ymlに名前がある場合)
            e.setJoinMessage(ChatColor.YELLOW + player.getName() + "**********");
        }
        saveAndReloadConfig();

        BukkitRunnable runnable = new BukkitRunnable() {// 入ってから3ティック後に実施(multiversecoreの仕様回避のための遅延実行。別に1ティックでもいい気はする)
            @Override
            public void run() {
                if (onGame) {// onGameの場合  そのまま参加させる（ただし参加者でなければ観戦モードとなり、インベントリも消去される）
                    hideNameTag();
                    if (playerinfo.keySet().contains(player.getName())) {// 参加者の場合
                        player.setGameMode(GameMode.ADVENTURE);
                    } else {// 参加者でない場合 アイテム削除+スペクテイタ+初期地があればそこに移動
                        player.setGameMode(GameMode.SPECTATOR);
                        player.getInventory().clear();// アイテム削除
                        if (spawnpoint != null) {// spawnpointがあった場合
                            player.teleport(spawnpoint);
                        }
                    }
                } else {// onGameでない場合
                    showNameTag();
                    if (config.getInt("admin.name." + player.getName()) == 1) {// playerがOPを持っていた場合
                        player.setGameMode(GameMode.CREATIVE);
                    } else {// playerがOPを持っていなかった場合 アイテム削除+アドベンチャー+初期地があればそこに移動
                        player.getInventory().clear();
                        player.setGameMode(GameMode.ADVENTURE);
                        if (spawnpoint != null) {// spawnpointがあった場合
                            player.teleport(spawnpoint);
                        }
                    }
                }
            }
        };
        runnable.runTaskLater(plugin, 3L);

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(ChatColor.LIGHT_PURPLE + player.getName() + "**********");
        online--;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity().getPlayer();
        String playername = player.getName();

        if (onGame) {// ゲーム中の場合

            // ゲーム中共通の処理
            playerinfo.get(playername).put("death", "true");// 死んだらdeathをtrueにする
            setDeadBody(player);//　死体の生成

            Inventory inv = Bukkit.getServer().getPlayer(playername).getInventory();
            StringBuilder items = new StringBuilder();
            for (int i=0; i < inv.getSize(); i++) {
                String itemname;
                if (inv.getItem(i) != null && !inv.getItem(i).getType().equals(Material.AIR)) {
                    if (inv.getItem(i).getItemMeta().hasDisplayName()) {
                        itemname = inv.getItem(i).getItemMeta().getDisplayName();
                    } else {
                        itemname = inv.getItem(i).getType().toString();
                    }
                    int amount = inv.getItem(i).getAmount();
                    items.append(ChatColor.YELLOW + itemname + ChatColor.WHITE + " × " + ChatColor.AQUA + amount + ChatColor.GRAY + ".  ");
                }
            }
            playerinfo.get(playername).put("items", String.valueOf(items));

            if (gamemode.equalsIgnoreCase("人狼")) {
                gameinfo.get("jobsleft").put(playerinfo.get(playername).get("job"), gameinfo.get("jobsleft").get(playerinfo.get(playername).get("job")) - 1);// 死んだプレイヤーの属していた役職を1減らす
                if (gameinfo.get("jobsleft").get("村人") + gameinfo.get("jobsleft").get("占い") + gameinfo.get("jobsleft").get("霊媒") + gameinfo.get("jobsleft").get("透視") == 0 || gameinfo.get("jobsleft").get("人狼") == 0) {// どちらかの陣営が0人になったらゲーム終了
                    Stop.stop();
                }
            } else if (gamemode.equalsIgnoreCase("鬼ごっこ")) {

            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (spawnpoint != null) {
            e.setRespawnLocation(spawnpoint);// spawnpointがあればそこにリスポーンさせる
        }
    }

    @EventHandler
    public void afterRespawn(PlayerPostRespawnEvent e) {
        Player player = e.getPlayer();
        if (onGame) {
            player.sendMessage(ChatColor.YELLOW + "マウスホイールをクリックor数字キーでプレイヤーにtpできます");
            player.setGameMode(GameMode.SPECTATOR);// スペクテイタにする
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(onGame) {
            Player player = (Player) e.getWhoClicked();
            if (!e.getInventory().equals(player.getEnderChest())) {
                return;
            }//ショップじゃなければ関係なし
            if (e.getAction().equals(InventoryAction.DROP_ONE_SLOT)
                    || e.getAction().equals(InventoryAction.DROP_ALL_SLOT)
                    || e.getAction().equals(InventoryAction.DROP_ONE_CURSOR)
                    || e.getAction().equals(InventoryAction.DROP_ALL_CURSOR)
                    || e.getAction().equals(InventoryAction.HOTBAR_SWAP)
                    || e.getAction().equals(InventoryAction.HOTBAR_MOVE_AND_READD)
                    || e.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)
                    || e.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)
                    || e.getAction().equals(InventoryAction.PICKUP_SOME)
                    || e.getAction().equals(InventoryAction.PICKUP_HALF)
                    || e.getAction().equals(InventoryAction.PICKUP_ONE)
                    || e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                e.setCancelled(true);
                return;
            }
            if (!e.getClick().isLeftClick()) {// 左クリック以外は許可しない
                e.setCancelled(true);
                return;
            }
            if (e.getAction().equals(InventoryAction.PLACE_ONE)
                    || e.getAction().equals(InventoryAction.PLACE_SOME)
                    || e.getAction().equals(InventoryAction.PLACE_ALL)) {
                if (e.getRawSlot() < e.getInventory().getSize()) {// 上にアイテムを置こうとした場合
                    e.setCancelled(true);
                    return;
                }
            }
            if (e.getRawSlot() < e.getInventory().getSize()) {// クリックしたのが上のインベントリの場合(買う)
                // buy
                ItemStack itemStack = e.getCurrentItem();
                buy(player, e, itemStack);
            }
        }
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
//        if (onGame) {
//            Player player = (Player) e.getPlayer();
//            ItemStack itemStack = player.getItemOnCursor();
//            ItemMeta itemMeta = itemStack.getItemMeta();
//            Inventory inventory = e.getInventory();

            /*
            if (inventory.equals(shop)) {// ショップならアイテム補完(clickeditemslotを使うのはここ（shopの場合）だけ)
                if (itemStack == null
                        || itemMeta == null
                        || itemStack.getType().equals(Material.AIR)) {
                    ;
                } else {// ちなみにこの時点で絶対にclickeditemslotは存在していることになるからnullエラーは吐かない
                    if (Integer.parseInt(playerinfo.get(player.getName()).get("clickeditemslot")) < inventory.getSize()) {// もともとの場所が上のインベントリであった場合→上に戻す
                        inventory.addItem(itemStack);
                        itemStack.setAmount(0);
                    }
                }
            }

             */
/*
            if (inventory.equals(player.getEnderChest())) {// エンチェスならオーダーペーパー回収してショップ入荷
                addItemsToShopByOrderPaper(inventory);
            }

 */
//        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
//        Bukkit.broadcastMessage(""+e.getInventory().getSize());
//        Bukkit.broadcastMessage(""+e.getPlayer().getInventory().getSize());
        Inventory inventory = e.getInventory();
        if (onGame) {// ゲーム中のみ実行
            Player player = (Player) e.getPlayer();

            chestFill(inventory);

            importItemsToEnderChest(player, inventory);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack having = player.getInventory().getItemInMainHand();
        if (e.getHand() != null) {
            if (e.getHand().equals(EquipmentSlot.HAND)) {// ２回判定をなくす
                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {// 右クリックの場合
                    if (e.getClickedBlock() != null) {// クリックブロックの判定
                        Block block = e.getClickedBlock();
                        if (block.getType().equals(Material.OAK_SIGN) || block.getType().equals(Material.OAK_WALL_SIGN)) {
                            Sign sign = (Sign) block.getState();
                            if (sign.getLine(1).equalsIgnoreCase("OP")) {
                                if (config.getInt("admin.name." + player.getName()) == 1) {// OP持ってたらOPなくす
                                    config.set("admin.name." + player.getName(), 0);
                                    saveAndReloadConfig();
                                    showNameTag();
                                    player.sendMessage("管理者から抜けました");
                                    for (int i=0; i<player.getInventory().getSize(); i++){
                                        if(player.getInventory().getItem(i) != null){
                                            if(player.getInventory().getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "" + ChatColor.BOLD + "管理本")){
                                                player.getInventory().clear(i);
                                            }
                                        }
                                    }
                                    if (onGame) {
                                        hideNameTag();
                                    }
                                } else if (config.getInt("admin.name." + player.getName()) == 0) {// OP なければOP付与
                                    config.set("admin.name." + player.getName(), 1);
                                    saveAndReloadConfig();
                                    showNameTag();
                                    player.sendMessage("管理者になりました");
                                    giveAdbook(player);
                                    if (onGame) {
                                        hideNameTag();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (onGame) {// ゲーム中のみ実行
            if (e.getHand() != null) {
                if (e.getHand().equals(EquipmentSlot.HAND)) {// ２回判定をなくす
                    if (e.getClickedBlock() != null) {// クリックブロックの判定
                        Block block = e.getClickedBlock();
                        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {// 右クリックの場合
                            /*
                            if (block.getType().equals(Material.EMERALD_BLOCK)) {
                                player.openInventory(shop);
                            }

                             */
                        }
                    }
                    if (having != null) {// 手持ちアイテムの判定
                        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {// 右クリックの場合
                            dekoi(player, having);
                            hikaru(player, having);
                            toumei(player, having);
                            seizonnsha(player, having);
                            jobball(player, having, e);
                            namestickPlayerInteract(player, having, e);
                        }
                        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {// 左クリックの場合
                            jobball(player, having, e);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) e.setCancelled(false);//Check if the event is cancelled and enable it again
        Entity damaged = e.getEntity();
        Entity damager = e.getDamager();
        if (damager instanceof Player && damaged instanceof Player) {
            Player damagedplayer = ((Player) damaged).getPlayer();
            Player damagerplayer = ((Player) damager).getPlayer();
            if (damagerplayer.getInventory().getItemInMainHand() != null) {
                if (damagerplayer.getInventory().getItemInMainHand().getItemMeta() != null) {
                    ItemStack having = damagerplayer.getInventory().getItemInMainHand();
                    fire(damagerplayer, having);
                    namestickDamage(damagedplayer, damagerplayer, having, e);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickupArrow(PlayerPickupArrowEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (onGame) {
            if (e.getInventory().getType().equals(InventoryType.ENDER_CHEST)) {
                e.setCancelled(true);
            }
        }
    }

}