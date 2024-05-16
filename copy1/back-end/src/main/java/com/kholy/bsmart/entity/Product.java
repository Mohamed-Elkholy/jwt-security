package com.kholy.bsmart.entity;

import com.kholy.bsmart.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Var;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String shortDescription;
    @Lob
    @Column(nullable = false)
    private String longDescription;
    @Column(nullable = false)
    private Integer Price;
    private Integer discountPercent;
    private Integer discountedPrice;
    @Column(nullable = false)
    private Integer quantity;
    @Column(unique = true, nullable = false)
    private String SKU;
    private Double rate;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VarianceColor> avaliableColors = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FAQ> faqList = new ArrayList<>();


}
