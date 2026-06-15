package com.nexe.service;

import com.nexe.entity.MenuItem;

import java.util.List;

public interface MenuService {

    MenuItem addMenuItem(Long shopId, MenuItem item);

    List<MenuItem> getMenuByShop(Long shopId);

    MenuItem getMenuItemById(Long id);

    MenuItem updateMenuItem(Long id, MenuItem item);

    void deleteMenuItem(Long id);
}