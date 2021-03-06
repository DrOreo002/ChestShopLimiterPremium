package me.droreo002.cslimit.inventory;

import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.CSLConfig;
import me.droreo002.cslimit.conversation.helper.ConversationType;
import me.droreo002.cslimit.conversation.helper.SessionDataKey;
import me.droreo002.cslimit.database.object.PlayerData;
import me.droreo002.oreocore.inventory.InventoryTemplate;
import me.droreo002.oreocore.inventory.OreoInventory;
import me.droreo002.oreocore.inventory.button.ButtonClickEvent;
import me.droreo002.oreocore.inventory.button.ButtonListener;
import me.droreo002.oreocore.inventory.button.GUIButton;
import me.droreo002.oreocore.utils.item.CustomSkull;
import me.droreo002.oreocore.utils.item.helper.ItemMetaType;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EditorInventory extends OreoInventory {

    public EditorInventory(Player opener, Player targetPlayer, ChestShopLimiter plugin, InventoryTemplate template) {
        super(template);
        final CSLConfig config = plugin.getCslConfig();
        final PlayerData data = plugin.getChestShopAPI().getData(targetPlayer.getUniqueId());
        GUIButton pInformation = template.getGUIButtons("I").get(0);
        if (data == null) throw new NullPointerException("Data cannot be null!. Please contact dev!");
        setSoundOnClick(config.getEditorClickSound());
        setSoundOnOpen(config.getEditorOpenSound());
        setSoundOnClose(config.getEditorCloseSound());

        TextPlaceholder infoPlaceholder = new TextPlaceholder(ItemMetaType.DISPLAY_NAME, "%player%", targetPlayer.getName())
                .add(ItemMetaType.LORE, "%player%", targetPlayer.getName())
                .add(ItemMetaType.LORE, "%maxShop%", String.valueOf(data.getMaxShop()))
                .add(ItemMetaType.LORE,"%shopCount%", String.valueOf(data.getShopCreated()));

        pInformation.applyTextPlaceholder(infoPlaceholder);
        pInformation.setItem(CustomSkull.toHeadUuid(pInformation.getItem(), targetPlayer.getUniqueId()), true, false);
        template.applyListener("1", new ButtonListener() {

            @Override
            public @NotNull ClickType[] getAllowedClickType() {
                return new ClickType[] {ClickType.LEFT, ClickType.RIGHT};
            }

            @Override
            public void onClick(ButtonClickEvent inventoryClickEvent) {
                if (inventoryClickEvent.getClick() == ClickType.LEFT) {
                    Map<SessionDataKey, Object> sessionData = new HashMap<>();
                    sessionData.put(SessionDataKey.CONVERSATION_TYPE, ConversationType.CHANGE_MAX_SHOP);
                    sessionData.put(SessionDataKey.PLAYER_DATA, data);

                    plugin.getConversationManager().sendConversation(opener, ConversationType.CHANGE_MAX_SHOP, sessionData);
                } else {
                    Map<SessionDataKey, Object> sessionData = new HashMap<>();
                    sessionData.put(SessionDataKey.CONVERSATION_TYPE, ConversationType.ADD_MAX_SHOP);
                    sessionData.put(SessionDataKey.PLAYER_DATA, data);

                    plugin.getConversationManager().sendConversation(opener, ConversationType.ADD_MAX_SHOP, sessionData);
                }
                closeInventory(opener);
            }
        });
        template.applyListener("2", new ButtonListener() {

            @Override
            public @NotNull ClickType[] getAllowedClickType() {
                return new ClickType[] {ClickType.LEFT, ClickType.RIGHT};
            }

            @Override
            public void onClick(ButtonClickEvent inventoryClickEvent) {
                if (inventoryClickEvent.getClick() == ClickType.LEFT) {
                    Map<SessionDataKey, Object> sessionData = new HashMap<>();
                    sessionData.put(SessionDataKey.CONVERSATION_TYPE, ConversationType.CHANGE_CURRENT_SHOP);
                    sessionData.put(SessionDataKey.PLAYER_DATA, data);

                    plugin.getConversationManager().sendConversation(opener, ConversationType.CHANGE_CURRENT_SHOP, sessionData);
                } else {
                    Map<SessionDataKey, Object> sessionData = new HashMap<>();
                    sessionData.put(SessionDataKey.CONVERSATION_TYPE, ConversationType.ADD_CURRENT_SHOP);
                    sessionData.put(SessionDataKey.PLAYER_DATA, data);

                    plugin.getConversationManager().sendConversation(opener, ConversationType.ADD_CURRENT_SHOP, sessionData);
                }
                closeInventory(opener);
            }
        });
    }
}
