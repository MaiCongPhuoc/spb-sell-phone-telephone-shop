package com.cg.controller.rest;


import com.cg.exception.NoCartException;
import com.cg.model.Cart;
import com.cg.model.CartItem;
import com.cg.model.Order;
import com.cg.model.OrderItem;
import com.cg.model.dto.CartItemListDTO;
import com.cg.service.cart.CartService;
import com.cg.service.cartItem.CartItemService;
import com.cg.service.order.OrderService;
import com.cg.service.orderitem.OrderItemService;
import com.cg.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartRestController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<?> getAllCartItems() {

        String createdBy = AppUtil.getPrincipalUsername();

        Optional<Cart> cartOptional = cartService.findByCreatedBy(createdBy);

        if (!cartOptional.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        Long cartId = cartOptional.get().getId();

        List<CartItemListDTO> cartItemListDTOS = cartItemService.findAllCartItemsDTO(cartId);

        if (cartItemListDTOS.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return new ResponseEntity<>(cartItemListDTOS, HttpStatus.OK);
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder() {

        String createdBy = AppUtil.getPrincipalUsername();

        Optional<Cart> cartOptional = cartService.findByCreatedBy(createdBy);

        if (!cartOptional.isPresent()) {
            throw new NoCartException("không có sản phẩm trong giỏ hàng");
        }

        Order order = new Order();
        order.setId(0L);
        order.setTotalAmount(cartOptional.get().getTotalAmount());
        order.setCreatedBy(createdBy);
        Order newOrder = orderService.save(order);

        List<CartItem> cartItems = cartItemService.findAllByCart(cartOptional.get());

        if (cartItems.isEmpty()) {
            throw new NoCartException("không có sản phẩm trong giỏ hàng");
        }

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(0L);
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setAmount(cartItem.getAmount());
            orderItem.setOrder(newOrder);
            orderItem.setCreatedBy(createdBy);
            orderItemService.save(orderItem);

            cartItemService.delete(cartItem);
        }

        cartService.delete(cartOptional.get());


        Map<String, Boolean> result = new HashMap<>();
        result.put("success", true);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
