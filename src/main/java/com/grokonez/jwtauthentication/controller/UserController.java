package com.grokonez.jwtauthentication.controller;

import com.grokonez.jwtauthentication.model.Product;
import com.grokonez.jwtauthentication.model.User;
import com.grokonez.jwtauthentication.repository.ProductRepository;
import com.grokonez.jwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity getUser() {

        return ResponseEntity.ok().body(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

    }

    @PutMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")

    public ResponseEntity<User> updateUser(@RequestBody User userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.findByUsername(authentication.getName()).
                orElseThrow(() -> new RuntimeException("Fail! -> No user found"));

        //@TODO implement update only data fields which changed
        user.setUsername(currentPrincipalName);
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(encoder.encode(userDetails.getPassword()));
        User updateUser = userRepository.save(user);

        return ResponseEntity.ok().body(updateUser);

//        @TODO update user in security context
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);


    }
//    @TODO implement logout


    // @TODO can't add duplicate products to function table, find out why?
    @PostMapping("/addToCart")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity addToCart(@RequestBody Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.findByUsername(currentPrincipalName).get();
        try {
            if (productRepository.existsById(product.getId())) {
                user.getShoppingCart().add(product);

                userRepository.save(user);
            }
        } catch (Exception e) {
            e.getMessage();
        }


        return ResponseEntity.ok().body("Product added successfully!");
    }

}