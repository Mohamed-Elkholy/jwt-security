package com.kholy.bsmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private String city;
    @Column(unique = true, nullable = false)
    private String ZIP;
    @Column(unique = true, nullable = false)
    private String phone;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
