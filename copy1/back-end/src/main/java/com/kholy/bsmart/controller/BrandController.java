package com.kholy.bsmart.controller;

import com.kholy.bsmart.entity.Brand;
import com.kholy.bsmart.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/brand")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
public class BrandController {
    private final BrandService brandService;
    @GetMapping("/get/{name}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'customer:read')")
    public ResponseEntity<?> getBrandByName(@PathVariable String name) {
        return ResponseEntity.ok(brandService.getBrandByName(name));
    }
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<?> addBrand(@RequestBody Brand brand) {
        return ResponseEntity.ok(brandService.addBrand(brand));
    }
}
