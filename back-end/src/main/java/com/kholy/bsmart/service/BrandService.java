package com.kholy.bsmart.service;

import com.kholy.bsmart.entity.Brand;
import com.kholy.bsmart.repository.BrandRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepo brandRepo;

    public Optional<Brand> getBrandByName(String name) {
        return brandRepo.findByName(name);
    }

    public String addBrand(Brand brand) {
        brandRepo.save(brand);
        return "Brand is added";
    }
}
