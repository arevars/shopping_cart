package com.grokonez.jwtauthentication.repository;

import com.grokonez.jwtauthentication.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    //    List<Cart> findAllByUserId(Long userId);
    Cart findCartByUserId(Long userId);
}