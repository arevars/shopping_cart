package com.grokonez.jwtauthentication.controller;


import com.grokonez.jwtauthentication.model.Cart;
import com.grokonez.jwtauthentication.model.User;
import com.grokonez.jwtauthentication.repository.CartRepository;
import com.grokonez.jwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/cart")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Set getCartProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.findByUsername(currentPrincipalName).get();
        Cart cart = cartRepository.findCartByUserId(user.getId());

        if (cart == null) {
            cart = new Cart();
        }

        return cart.getCartProducts();

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();
//
//        User user = userRepository.findByUsername(currentPrincipalName).get();
//
//        return cartRepository.findAllByUserId(user.getId());
    }

    @PostMapping("/cart")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity addToCart(@RequestBody Long productId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.findByUsername(currentPrincipalName).get();
        Cart cart = cartRepository.findCartByUserId(user.getId());

        if (cart == null) {
            cart = new Cart();
        }

        Set products = cart.getCartProducts();
        products.add(productId);
        cart.setCartProducts(products);

        cartRepository.save(cart);


        return ResponseEntity.ok().body("Product added successfully!");

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();
//
//        User user = userRepository.findByUsername(currentPrincipalName).get();
//        cart.setUserId(user.getId());
//
//        cartRepository.save(cart);
//        return ResponseEntity.ok().body(cart);
    }

    @DeleteMapping("/cart")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void delete() {

    }


}