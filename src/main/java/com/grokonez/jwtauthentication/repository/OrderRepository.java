package com.grokonez.jwtauthentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.grokonez.jwtauthentication.model.OrderProduct;

import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<OrderProduct, Long> {

}