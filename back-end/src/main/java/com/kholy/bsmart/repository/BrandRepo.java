package com.kholy.bsmart.repository;

import com.kholy.bsmart.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepo extends JpaRepository<Brand, Long> {
    Optional<Brand> findByName(String name);
}
