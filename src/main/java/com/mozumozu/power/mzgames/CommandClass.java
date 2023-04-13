package com.mozumozu.power.mzgames;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.mozumozu.power.mzgames.Met.met;
import static com.mozumozu.power.mzgames.Moz.moz;
import static com.mozumozu.power.mzgames.Rei.rei;
import static com.mozumozu.power.mzgames.Tou.tou;
import static com.mozumozu.power.mzgames.Ura.ura;


public class CommandClass
        implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean isPlayer = (sender instanceof Player);// 実行者がプレイヤーの場合にはtrueになる
        if (!isPlayer) {// 実行者がプレイヤーでない場合（コンソールから実行された場合）にはメッセージを出力して処理を終了する
            sender.sendMessage("このコマンドはコンソールからは実行できません。");
            return true;
        }
        Player player = (Player) sender;
        // コマンドの処理を書く
        if (cmd.getName().equalsIgnoreCase("moz")) {
            moz(player, args);
        } else if (cmd.getName().equalsIgnoreCase("ura")) {
            ura(player, args);
        } else if (cmd.getName().equalsIgnoreCase("rei")) {
            rei(player, args);
        } else if (cmd.getName().equalsIgnoreCase("met")) {
            met(player, args);
        } else if (cmd.getName().equalsIgnoreCase("tou")) {
            tou(player, args);
        }
        return true;
    }
}
