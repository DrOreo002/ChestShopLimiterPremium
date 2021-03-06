package me.droreo002.cslimit.conversation.prompts;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.CSLConfig;
import me.droreo002.cslimit.conversation.helper.ConversationType;
import me.droreo002.cslimit.conversation.helper.SessionDataKey;
import me.droreo002.cslimit.database.object.PlayerData;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.oreocore.utils.item.helper.ItemMetaType;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CIntegerConversation extends StringPrompt {

    private final ChestShopLimiter plugin;
    private final LangManager lang;
    private final CSLConfig config;

    public CIntegerConversation(ChestShopLimiter plugin) {
        this.plugin = plugin;
        this.lang = plugin.getLangManager();
        this.config = plugin.getCslConfig();
    }

    @Override
    public @NotNull String getPromptText(ConversationContext context) {
        Player forWhom = (Player) context.getForWhom();
        ConversationType type = (ConversationType) context.getSessionData(SessionDataKey.CONVERSATION_TYPE);
        PlayerData targetData = (PlayerData) context.getSessionData(SessionDataKey.PLAYER_DATA);
        String targetName = Bukkit.getOfflinePlayer(targetData.getPlayerUUID()).getName();
        TextPlaceholder pl = new TextPlaceholder(ItemMetaType.NONE, "%target%", targetName);
        String result;
        switch (Objects.requireNonNull(type)) {
            case CHANGE_MAX_SHOP:
                result = lang.getLang(LangPath.TE_CHANGE_MAX_SHOP, pl, true);
                break;
            case CHANGE_CURRENT_SHOP:
                result = lang.getLang(LangPath.TE_CHANGE_CURR_SHOP, pl, true);
                break;
            case ADD_MAX_SHOP:
                result = lang.getLang(LangPath.TE_ADD_MAX_SHOP, pl, true);
                break;
            case ADD_CURRENT_SHOP:
                result = lang.getLang(LangPath.TE_ADD_CURR_SHOP, pl, true);
                break;
            default:
                config.getTEditorFailureSound().send(forWhom);
                return "An internal error occurred, please contact admin!";
        }
        config.getTEditorSuccessSound().send(forWhom);
        return result;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player) context.getForWhom();
        PlayerData targetData = (PlayerData) context.getSessionData(SessionDataKey.PLAYER_DATA);
        ConversationType type = (ConversationType) context.getSessionData(SessionDataKey.CONVERSATION_TYPE);
        int numberInput;
        try {
            numberInput = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            player.sendMessage(lang.getLang(LangPath.TE_ERROR_NOT_INTEGER, null, true));
            config.getTEditorFailureSound().send(player);
            return Prompt.END_OF_CONVERSATION;
        }
        TextPlaceholder pl = new TextPlaceholder(ItemMetaType.NONE, "%value%", String.valueOf(numberInput));
        switch (type) {
            /*
            Change shop value
             */
            case CHANGE_MAX_SHOP:
                player.sendMessage(lang.getLang(LangPath.TE_SUCCESS_CHANGE_MAX_SHOP, pl, true));
                targetData.setMaxShop(numberInput);
                break;
            case CHANGE_CURRENT_SHOP:
                if (numberInput > targetData.getMaxShop()) {
                    player.sendMessage(lang.getLang(LangPath.TE_SHOP_CREATED_GREATER, null, true));
                    config.getTEditorFailureSound().send(player);
                    return Prompt.END_OF_CONVERSATION;
                }
                player.sendMessage(lang.getLang(LangPath.TE_SUCCESS_CHANGE_CURR_SHOP, pl, true));
                targetData.setShopCreated(numberInput);
                break;

            /*
            Add shop value
             */
            case ADD_CURRENT_SHOP:
                if ((numberInput + targetData.getShopCreated()) > targetData.getMaxShop()) {
                    player.sendMessage(lang.getLang(LangPath.TE_SHOP_CREATED_GREATER, null, true));
                    config.getTEditorFailureSound().send(player);
                    return Prompt.END_OF_CONVERSATION;
                }
                player.sendMessage(lang.getLang(LangPath.TE_ADD_CURR_SHOP, pl, true));
                targetData.addShopCreated(numberInput);
                break;
            case ADD_MAX_SHOP:
                player.sendMessage(lang.getLang(LangPath.TE_SUCCESS_ADD_MAX_SHOP, pl, true));
                targetData.addMaxShop(numberInput);
                break;
        }
        config.getTEditorSuccessSound().send(player);
        plugin.getDatabase().getWrapper().savePlayerData(targetData);
        return Prompt.END_OF_CONVERSATION;
    }
}
