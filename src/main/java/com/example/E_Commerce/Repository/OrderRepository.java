package com.example.E_Commerce.Repository;

import com.example.E_Commerce.Entity.Order;
import com.example.E_Commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByUser(User user);
}
