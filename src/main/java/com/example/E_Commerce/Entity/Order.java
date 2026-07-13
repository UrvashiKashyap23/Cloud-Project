package com.example.E_Commerce.Entity;

import com.example.E_Commerce.DTO.Enums.CancelReason;
import com.example.E_Commerce.DTO.Enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private double TotalAmount;

    @Enumerated(EnumType.STRING)
    private StatusType Status;

    private LocalDate OrderDate;

    private LocalDateTime createdAt;

    private  LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private CancelReason cancelReason;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentMethod paymentMethod;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.OrderDate = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
