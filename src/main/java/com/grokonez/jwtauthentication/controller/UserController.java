package com.grokonez.jwtauthentication.controller;

import com.grokonez.jwtauthentication.model.User;
import com.grokonez.jwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/api/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity getUser() {

        return ResponseEntity.ok().body(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

    }

    @PutMapping("/api/user")
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

}