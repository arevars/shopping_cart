package com.grokonez.jwtauthentication.controller;


import com.grokonez.jwtauthentication.model.Product;
import com.grokonez.jwtauthentication.model.User;
import com.grokonez.jwtauthentication.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    CartRepository cartRepository;

    @GetMapping("/cart")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List <Product> getCartProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        Long userId = user.getId();

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return cartRepository.getCartProductsByUserId(userId);
    }

}