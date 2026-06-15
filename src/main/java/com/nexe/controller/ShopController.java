package com.nexe.controller;

import com.nexe.entity.Shop;
import com.nexe.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shops")
@CrossOrigin(origins = "*")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    // ✅ Create Shop
    @PostMapping 
    public ResponseEntity<Shop> createShop(@RequestBody Shop shop) {
        Shop savedShop = shopService.createShop(shop);
        return ResponseEntity.ok(savedShop);
    }

    // ✅ Get all shops
    @GetMapping
    public ResponseEntity<List<Shop>> getAllShops() {
        return ResponseEntity.ok(shopService.getAllShops());
    }

    // ✅ Get shop by ID
    @GetMapping("/{id}")
    public ResponseEntity<Shop> getShopById(@PathVariable Long id) {
        return ResponseEntity.ok(shopService.getShopById(id));
    }

    // ✅ Update shop
    @PutMapping("/{id}")
    public ResponseEntity<Shop> updateShop(@PathVariable Long id,
                                           @RequestBody Shop shop) {
        return ResponseEntity.ok(shopService.updateShop(id, shop));
    }

    // ✅ Soft delete (set active = false)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ResponseEntity.ok("Shop deleted successfully (soft delete)");
    }

    // ✅ Activate / Deactivate shop
    @PatchMapping("/{id}/status")
    public ResponseEntity<Shop> changeStatus(@PathVariable Long id,
                                              @RequestParam boolean active) {
        return ResponseEntity.ok(shopService.changeStatus(id, active));
    }
}