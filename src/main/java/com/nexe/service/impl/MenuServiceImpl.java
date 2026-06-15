package com.nexe.service.impl;

import com.nexe.entity.MenuItem;
import com.nexe.entity.Shop;
import com.nexe.repo.MenuRepository;
import com.nexe.repo.ShopRepository;
import com.nexe.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;

    public MenuServiceImpl(MenuRepository menuRepository,
                           ShopRepository shopRepository) {
        this.menuRepository = menuRepository;
        this.shopRepository = shopRepository;
    }

    @Override
    public MenuItem addMenuItem(Long shopId, MenuItem item) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        item.setShop(shop);
        return menuRepository.save(item);
    }

    @Override
    public List<MenuItem> getMenuByShop(Long shopId) {
        return menuRepository.findByShopShopId(shopId);
    }

    @Override
    public MenuItem getMenuItemById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
    }

    @Override
    public MenuItem updateMenuItem(Long id, MenuItem item) {
        MenuItem existing = getMenuItemById(id);

        existing.setItemName(item.getItemName());
        existing.setPrice(item.getPrice());
        existing.setCategory(item.getCategory());

        return menuRepository.save(existing);
    }

    @Override
    public void deleteMenuItem(Long id) {
        menuRepository.deleteById(id);
    }
}