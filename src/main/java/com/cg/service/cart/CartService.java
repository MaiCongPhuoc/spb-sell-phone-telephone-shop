package com.cg.service.cart;

import com.cg.model.Cart;
import com.cg.service.IGeneralService;

import java.util.Optional;

public interface CartService extends IGeneralService<Cart> {

    Optional<Cart> findByCreatedBy(String createdBy);

    Boolean existsByCreatedBy(String createdBy);

    void delete(Cart cart);
}
