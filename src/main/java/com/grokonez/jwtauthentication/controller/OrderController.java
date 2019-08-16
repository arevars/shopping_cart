package com.grokonez.jwtauthentication.controller;


import com.grokonez.jwtauthentication.model.Order;
import com.grokonez.jwtauthentication.model.OrderStatus;
import com.grokonez.jwtauthentication.model.User;
import com.grokonez.jwtauthentication.repository.OrderRepository;
import com.grokonez.jwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity checkout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = userRepository.findByUsername(currentPrincipalName).get();

        Order order = new Order();
        order.setUserId(user.getId());

        order.setOrderedProducts(user.getShoppingCart());
        order.setStatus(OrderStatus.ORDER_STATUS_INPROCESS);

        orderRepository.save(order);

        user.setShoppingCart(null);
        userRepository.save(user);
        return ResponseEntity.ok().body("Checked out! wait for accepting order");
    }
}