package com.nexe.controller;

import com.nexe.entity.MenuItem;
import com.nexe.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // ✅ Add menu item to a shop
    @PostMapping("/{shopId}")
    public ResponseEntity<MenuItem> addMenuItem(@PathVariable Long shopId,
                                                 @RequestBody MenuItem item) {
        return ResponseEntity.ok(menuService.addMenuItem(shopId, item));
    }

    // ✅ Get all menu items of a shop
    @GetMapping("/{shopId}")
    public ResponseEntity<List<MenuItem>> getMenuByShop(@PathVariable Long shopId) {
        return ResponseEntity.ok(menuService.getMenuByShop(shopId));
    }

    // ✅ Get single menu item by id
    @GetMapping("/item/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.getMenuItemById(id));
    }

    // ✅ Update menu item
    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id,
                                                    @RequestBody MenuItem item) {
        return ResponseEntity.ok(menuService.updateMenuItem(id, item));
    }

    // ✅ Delete menu item
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {
        menuService.deleteMenuItem(id);
        return ResponseEntity.ok("Menu item deleted successfully");
    }
}