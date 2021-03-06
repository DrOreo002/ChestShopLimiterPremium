package me.droreo002.cslimit.inventory;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.CSLConfig;
import me.droreo002.oreocore.inventory.InventoryTemplate;
import me.droreo002.oreocore.inventory.animation.InventoryAnimationManager;
import me.droreo002.oreocore.inventory.button.GUIButton;
import me.droreo002.oreocore.inventory.paginated.PaginatedInventory;
import me.droreo002.oreocore.utils.item.CustomSkull;
import me.droreo002.oreocore.utils.item.helper.ItemMetaType;
import me.droreo002.oreocore.utils.item.helper.TextPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SelectorInventory extends PaginatedInventory {

    // Callbacks
    @Getter
    private Selected selected;

    public SelectorInventory(InventoryTemplate template, ChestShopLimiter plugin, Selected selected) {
        super(template);
        this.selected = selected;
        final CSLConfig config = plugin.getCslConfig();

        setSoundOnOpen(config.getPSelectorOpenSound());
        setSoundOnClick(config.getPSelectorClickSound());
        setSoundOnClose(config.getPSelectorCloseSound());
        setInventoryAnimationManager(InventoryAnimationManager.getDefault());

        for (Player online : Bukkit.getOnlinePlayers()) {
            GUIButton pButton = plugin.getInventoryTemplates().getPlayerHeadButton();
            pButton.applyTextPlaceholder(new TextPlaceholder(ItemMetaType.DISPLAY_NAME, "%player%", online.getName()));
            pButton.setItem(CustomSkull.toHeadUuid(pButton.getItem(), online.getUniqueId()), true, false);
            pButton.addListener(inventoryClickEvent -> {
                ItemStack curr = inventoryClickEvent.getCurrentItem();
                selected.selected(inventoryClickEvent, curr, online);
            });
            addPaginatedButton(pButton);
        }
    }

    /**
     * Simple interface callback
     */
    public interface Selected {

        /**
         * Called when player selected something
         *
         * @param e The click event
         * @param item The item player selected
         * @param selected The selected player
         */
        void selected(InventoryClickEvent e, ItemStack item, Player selected);
    }
}
