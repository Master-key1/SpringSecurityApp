package com.nexe.service.impl;

import com.nexe.entity.Shop;
import com.nexe.repo.ShopRepository;
import com.nexe.service.ShopService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    public ShopServiceImpl(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public Shop createShop(Shop shop) {
        return shopRepository.save(shop);
    }

    @Override
    public List<Shop> getAllShops() {
        return shopRepository.findAll();
    }

    @Override
    public Shop getShopById(Long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
    }

    @Override
    public Shop updateShop(Long id, Shop shop) {
        Shop existing = getShopById(id);

        existing.setOwnerName(shop.getOwnerName());
        existing.setMobileNumber(shop.getMobileNumber());
        existing.setBusinessName(shop.getBusinessName());
        existing.setBusinessType(shop.getBusinessType());
        existing.setBusinessAddress(shop.getBusinessAddress());
        existing.setGstNumber(shop.getGstNumber());
        existing.setOpeningTime(shop.getOpeningTime());
        existing.setClosingTime(shop.getClosingTime());
        existing.setUpiId(shop.getUpiId());
        existing.setOwnerPhotoPath(shop.getOwnerPhotoPath());
        existing.setShopPhotoPath(shop.getShopPhotoPath());

        return shopRepository.save(existing);
    }

    @Override
    public void deleteShop(Long id) {
        Shop shop = getShopById(id);
        shop.setActive(false);
        shopRepository.save(shop);
    }

    @Override
    public Shop changeStatus(Long id, boolean active) {
        Shop shop = getShopById(id);
        shop.setActive(active);
        return shopRepository.save(shop);
    }
}