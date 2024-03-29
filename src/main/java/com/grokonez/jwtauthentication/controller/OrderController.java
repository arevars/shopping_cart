package com.grokonez.jwtauthentication.controller;


import com.grokonez.jwtauthentication.model.OrderProduct;
import com.grokonez.jwtauthentication.model.OrderStatus;
import com.grokonez.jwtauthentication.model.User;
import com.grokonez.jwtauthentication.repository.OrderRepository;
import com.grokonez.jwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

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

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setUserId(user.getId());

        orderProduct.getOrderedProducts().addAll(user.getShoppingCart());
        orderProduct.setStatus(OrderStatus.ORDER_STATUS_INPROCESS);

        orderRepository.save(orderProduct);

//        user.setShoppingCart(null);
//        userRepository.save(user);
        return ResponseEntity.ok().body("Checked out! wait for accepting order");
    }

    @GetMapping("orders")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderProduct> getOrders() {
        return orderRepository.findAll();
    }

    @PostMapping("orders/{id}/accept")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity acceptOrder(@PathVariable(value = "id") Long orderId) {

        OrderProduct order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order Not Found with -> orderId : " + orderId)
                );

        order.setStatus(OrderStatus.ORDER_STATUS_APPROVED);
        orderRepository.save(order);

        return ResponseEntity.ok().body("Order Accepted");
    }

    @PostMapping("orders/{id}/decline")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity declineOrder(@PathVariable(value = "id") Long orderId) {

        OrderProduct order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order Not Found with -> orderId : " + orderId)
                );

        order.setStatus(OrderStatus.ORDER_STATUS_REJECTED);
        orderRepository.save(order);

        return ResponseEntity.ok().body("Order Declined");
    }


}
