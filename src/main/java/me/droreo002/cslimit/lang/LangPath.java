package me.droreo002.cslimit.lang;

import lombok.Getter;

public enum LangPath {

    /*
    Normal values
     */
    NORMAL_CONFIG_RELOADED("Normal.config-reloaded"),
    NORMAL_LANG_RELOADED("Normal.lang-reloaded"),
    NORMAL_FILE_UPDATED("Normal.file-updated"),
    NORMAL_NO_PERMISSION("Normal.no-permission"),
    NORMAL_PLAYER_ONLY("Normal.player-only"),
    NORMAL_TOO_MUCH_ARGS("Normal.too-much-args"),
    NORMAL_SHOP_CREATED("Normal.shop-created"),
    NORMAL_SHOP_REMOVED("Normal.shop-removed"),
    NORMAL_CONSOLE_ONLY("Normal.console-only"),
    NORMAL_SHOP_CREATED_RESET("Normal.shop-created-reset"),
    NORMAL_SHOP_CREATED_RESET_OTHER("Normal.shop-created-reset-other"),
    NORMAL_DATA_SAVE_SUCCESS("Normal.data-save-success"),
    NORMAL_DATA_NOT_FOUND("Normal.data-not-found"),
    NORMAL_INV_TEMPLATE_RELOADED("Normal.inv-template-reloaded"),

    /*
    Text Editor values
     */
    TE_PLAYER_ONLY("TextEditor.player-only"),
    TE_CLOSED("TextEditor.closed"),
    TE_CHANGE_MAX_SHOP("TextEditor.entry.change-max-shop"),
    TE_CHANGE_CURR_SHOP("TextEditor.entry.change-curr-shop"),
    TE_ADD_MAX_SHOP("TextEditor.entry.add-max-shop"),
    TE_ADD_CURR_SHOP("TextEditor.entry.add-curr-shop"),
    TE_ERROR_NOT_INTEGER("TextEditor.error.not-integer"),
    TE_SUCCESS_CHANGE_MAX_SHOP("TextEditor.success.change-max-shop"),
    TE_SUCCESS_CHANGE_CURR_SHOP("TextEditor.success.change-current-shop"),
    TE_SUCCESS_ADD_MAX_SHOP("TextEditor.success.add-max-shop"),
    TE_SUCCESS_ADD_CURR_SHOP("TextEditor.success.add-current-shop"),
    TE_SHOP_CREATED_GREATER("TextEditor.error.shop-created-greater"),

    /*
    Misc values
     */
    MISC_TELEPORT_BUTTON_TEXT("Misc.teleport-button-text"),
    MISC_TELEPORT_BUTTON_HOVER_TEXT("Misc.teleport-button-hover-text"),
    MISC_TELEPORT_NO_LOCATION_FOUND("Misc.teleport-no-location-found"),
    MISC_SHOP_LIMIT_UNLIMITED("Misc.shop-limit-unlimited"),

    /*
    Error values
     */
    ERROR_USAGE_COMMAND_CHECK("Error.usage.command-check"),
    ERROR_USAGE_COMMAND_UNKNOWN("Error.usage.command-unknown"),
    ERROR_USAGE_COMMAND_RELOAD("Error.usage.command-reload"),
    ERROR_USAGE_COMMAND_RESET("Error.usage.command-reset"),
    ERROR_USAGE_COMMAND_SAVE_DATA("Error.usage.command-save-data"),
    ERROR_USAGE_COMMAND_STATUS("Error.usage.command-status"),
    ERROR_USAGE_COMMAND_EDIT("Error.usage.command-edit"),
    ERROR_USAGE_COMMAND_EDIT_MAX("Error.usage.command-edit-max"),
    ERROR_USAGE_COMMAND_EDIT_COUNT("Error.usage.command-edit-count"),
    ERROR_CANNOT_LOAD_DATA("Error.cannot-load-data"),
    ERROR_PLAYER_NEVER_PLAYED("Error.player-never-played"),
    ERROR_LIMIT_REACHED("Error.limit-reached"),
    ERROR_INVALID_RELOAD_TYPE("Error.invalid-reload-type"),
    ERROR_TAB_COMPLETE_NO_PERM("Error.tab-complete-no-permission"),

    /*
    List values
     */
    LIST_HELP_MESSAGE("List.help-message"),
    LIST_CHECK_MESSAGE("List.check-message"),
    LIST_PLAYER_STATUS_MESSAGE("List.player-status-message");

    @Getter
    private String path;

    LangPath(String path) {
        this.path = path;
    }
}
