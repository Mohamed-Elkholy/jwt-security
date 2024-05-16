package com.kholy.bsmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kholy.bsmart.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;
    private String cardName;
    private String cardNum;
    private String CVV;
    private LocalDate expDate;
    private String walletNum;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
