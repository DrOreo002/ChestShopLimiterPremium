package me.droreo002.cslimit.commands.universal;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.database.object.PlayerData;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.cslimit.manager.logger.Debug;
import me.droreo002.cslimit.utils.PlayerUtils;
import me.droreo002.oreocore.commands.CommandArg;
import me.droreo002.oreocore.commands.CustomCommand;
import me.droreo002.oreocore.utils.item.helper.ItemMetaType;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import me.droreo002.oreocore.utils.strings.TextBuilder;
import me.droreo002.oreocore.utils.world.LocationUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import static me.droreo002.oreocore.utils.strings.StringUtils.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CheckCommand extends CommandArg {

    private final ChestShopLimiter plugin;
    private final LangManager lang;

    public CheckCommand(CustomCommand parent, ChestShopLimiter plugin) {
        super("check", parent);
        this.plugin = plugin;
        this.lang = plugin.getLangManager();

        setPermission("csl.admin.check", lang.getLang(LangPath.NORMAL_NO_PERMISSION, null, true));
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Debug.info("Command check has been executed by " + commandSender.getName(), false, Debug.LogType.FILE);
        if (args.length == 1) {
            error(commandSender);
            sendMessage(commandSender, lang.getLang(LangPath.ERROR_USAGE_COMMAND_CHECK, null, true));
            return;
        }
        if (args.length == 2) {
            String name = args[1];
            Player target = Bukkit.getPlayerExact(name);
            if (target == null) {
                success(commandSender);
                UUID offlineUUID = null;
                try {
                    offlineUUID = PlayerUtils.getPlayerUuid(name).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                OfflinePlayer off = Bukkit.getOfflinePlayer(offlineUUID);
                if (!off.hasPlayedBefore()) {
                    sendMessage(commandSender, lang.getLang(LangPath.ERROR_PLAYER_NEVER_PLAYED, null, true));
                    error(commandSender);
                    return;
                }
                sendCheckFormat(commandSender, off);
                return;
            }
            success(commandSender);
            sendCheckFormat(commandSender, target);
        } else {
            error(commandSender);
            sendMessage(commandSender, lang.getLang(LangPath.NORMAL_TOO_MUCH_ARGS, null, true));
        }
    }
    
    private void sendCheckFormat(CommandSender sender, OfflinePlayer target) {
        final PlayerData data = plugin.getChestShopAPI().getData(target.getUniqueId());
        final String targetName = target.getName();

        if (data == null || data.isEmptyData()) {
            sendMessage(sender, lang.getLang(LangPath.NORMAL_DATA_NOT_FOUND, new TextPlaceholder(ItemMetaType.NONE, "%player%", targetName), true));
            error(sender);
            return;
        }
        Location location = LocationUtils.toLocation(data.getLastShopLocation());
        if (!(sender instanceof Player)) {
            for (String s : lang.getLangList(LangPath.LIST_CHECK_MESSAGE, null)) {
                String shopLimit = String.valueOf(data.getMaxShop());
                String lastShop = data.getLastShopLocation();
                if (data.getLastPermission().equalsIgnoreCase("csl.limit.unlimited")) {
                    shopLimit = lang.getLang(LangPath.MISC_SHOP_LIMIT_UNLIMITED, null, false);
                }
                if (location == null) {
                    lastShop = lang.getLang(LangPath.MISC_TELEPORT_NO_LOCATION_FOUND, null, false);
                }
                String result = s
                        .replace("%player%", targetName)
                        .replace("%uuid%", target.getUniqueId().toString())
                        .replace("%shopcreated%", String.valueOf(data.getShopCreated()))
                        .replace("%shoplimit%", shopLimit)
                        .replace("%lastshop%", lastShop);
                sender.sendMessage(color(result));
            }
            return;
        }
        for (String s : lang.getLangList(LangPath.LIST_CHECK_MESSAGE, null)) {
            if (location != null) {
                if (s.contains("%lastshop%")) {
                    String result = s.replace("%lastshop%", "");
                    TextBuilder builder = TextBuilder.of(result).addText(lang.getLang(LangPath.MISC_TELEPORT_BUTTON_TEXT, null, false));
                    builder.setHoverEvent(HoverEvent.Action.SHOW_TEXT, lang.getLang(LangPath.MISC_TELEPORT_BUTTON_HOVER_TEXT, null, false));
                    // Setup the click event
                    if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
                        builder.setClickEvent(ClickEvent.Action.RUN_COMMAND, "/tppos " + Math.round(location.getX()) + " " + Math.round(location.getY() + 2.5D) + " " + Math.round(location.getZ()) + " " + Math.round(location.getYaw()) + " " + Math.round(location.getPitch()));
                    } else {
                        builder.setClickEvent(ClickEvent.Action.RUN_COMMAND, "/minecraft:tp " + sender.getName() + Math.round(location.getX()) + " " + Math.round(location.getY() + 2.5D) + " " + Math.round(location.getZ()) + " " + Math.round(location.getYaw()) + " " + Math.round(location.getPitch()));
                    }
                    builder.send((Player) sender);
                    continue;
                }
            } else {
                if (s.contains("%lastshop%")) {
                    sender.sendMessage(s.replace("%lastshop%", lang.getLang(LangPath.MISC_TELEPORT_NO_LOCATION_FOUND, null, false)));
                    continue;
                }
            }
            String shopLimit = String.valueOf(data.getMaxShop());
            if (data.getLastPermission().equalsIgnoreCase("csl.limit.unlimited")) {
                shopLimit = lang.getLang(LangPath.MISC_SHOP_LIMIT_UNLIMITED, null, false);
            }
            String result = s
                .replace("%player%", targetName)
                .replace("%uuid%", target.getUniqueId().toString())
                .replace("%shopcreated%", String.valueOf(data.getShopCreated()))
                .replace("%shoplimit%", shopLimit);
            sender.sendMessage(color(result));
        }
    }
}
