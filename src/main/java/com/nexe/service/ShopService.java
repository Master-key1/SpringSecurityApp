package com.nexe.service;

import com.nexe.entity.Shop;
import java.util.List;

public interface ShopService {

    Shop createShop(Shop shop);

    List<Shop> getAllShops();

    Shop getShopById(Long id);

    Shop updateShop(Long id, Shop shop);

    void deleteShop(Long id);

    Shop changeStatus(Long id, boolean active);
}