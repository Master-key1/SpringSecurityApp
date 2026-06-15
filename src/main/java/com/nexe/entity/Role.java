package com.nexe.entity;

import java.util.Set;

public enum Role {

    // ================= CUSTOMER =================
    ROLE_USER(Set.of(
            AppPermission.SHOP_READ,
            AppPermission.MENU_READ,
            AppPermission.CART_READ,
            AppPermission.CART_CREATE,
            AppPermission.CART_UPDATE,
            AppPermission.CART_DELETE,
            AppPermission.ORDER_READ,
            AppPermission.ORDER_CREATE
    )),

    // ================= SHOP OWNER =================
    ROLE_SHOP_OWNER(Set.of(
            AppPermission.SHOP_READ,
            AppPermission.SHOP_UPDATE,

            AppPermission.MENU_READ,
            AppPermission.MENU_CREATE,
            AppPermission.MENU_UPDATE,
            AppPermission.MENU_DELETE,

            AppPermission.ORDER_READ,
            AppPermission.ORDER_UPDATE
    )),

    // ================= ADMIN =================
    ROLE_ADMIN(Set.of(
            AppPermission.SHOP_READ,
            AppPermission.SHOP_CREATE,
            AppPermission.SHOP_UPDATE,
            AppPermission.SHOP_DELETE,

            AppPermission.MENU_READ,
            AppPermission.MENU_CREATE,
            AppPermission.MENU_UPDATE,
            AppPermission.MENU_DELETE,

            AppPermission.CART_READ,
            AppPermission.CART_DELETE,

            AppPermission.ORDER_READ,
            AppPermission.ORDER_UPDATE,
            AppPermission.ORDER_DELETE
    ));

    private final Set<AppPermission> permissions;

    Role(Set<AppPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<AppPermission> getPermissions() {
        return permissions;
    }
}