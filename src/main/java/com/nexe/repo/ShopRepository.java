package com.nexe.repo;

import com.nexe.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    List<Shop> findByActiveTrue();

    List<Shop> findByBusinessType(String businessType);

    Shop findByMobileNumber(String mobileNumber);

    List<Shop> findByBusinessNameContainingIgnoreCase(String name);
}