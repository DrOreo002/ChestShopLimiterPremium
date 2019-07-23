package me.droreo002.cslimit.inventory;

import lombok.Getter;
import me.droreo002.cslimit.ChestShopLimiter;
import me.droreo002.cslimit.config.ConfigManager;
import me.droreo002.cslimit.lang.LangManager;
import me.droreo002.cslimit.lang.LangPath;
import me.droreo002.oreocore.inventory.button.GUIButton;
import me.droreo002.oreocore.inventory.paginated.PaginatedInventory;
import me.droreo002.oreocore.utils.item.CustomItem;
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

    public SelectorInventory(String title, Selected selected) {
        super(45, title);
        this.selected = selected;
        final ChestShopLimiter plugin = ChestShopLimiter.getInstance();
        final LangManager lang = plugin.getLangManager();
        final ConfigManager.Memory mem = plugin.getConfigManager().getMemory();

        setSoundOnOpen(mem.getPSelectorOpenSound());
        setSoundOnClick(mem.getPSelectorClickSound());
        setSoundOnClose(mem.getPSelectorCloseSound());

        setSearchRow(4, true, CustomItem.GRAY_GLASSPANE);
        setItemRow(0, 1, 2, 3);

        // INFO : NO NEED ASYNC. BECAUSE WE"RE ALREADY OPENED THIS INVENTORY VIA ASYNC WAY
        for (Player player : Bukkit.getOnlinePlayers()) {
            final TextPlaceholder placeholder = new TextPlaceholder(ItemMetaType.DISPLAY_NAME, "%player", player.getName());
            ItemStack head = CustomItem.applyFromSection(CustomSkull.getHead(player.getUniqueId()), lang.asSection(LangPath.INVENTORY_PLAYER_SELECTOR_PLAYER_BUTTON), placeholder);
            addPaginatedButton(new GUIButton(head).setListener(inventoryClickEvent -> {
                ItemStack curr = inventoryClickEvent.getCurrentItem();
                selected.selected(inventoryClickEvent, curr, player);
            }));
        }
    }

    public interface Selected {
        void selected(InventoryClickEvent e, ItemStack item, Player selected);
    }
}
