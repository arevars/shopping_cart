package com.grokonez.jwtauthentication.repository;

import com.grokonez.jwtauthentication.model.Cart;
import com.grokonez.jwtauthentication.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Product> getProductsByUserId(String userid);
}