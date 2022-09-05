package com.cg.service.cartItem;

import com.cg.model.Cart;
import com.cg.model.CartItem;
import com.cg.model.dto.CartItemListDTO;
import com.cg.service.IGeneralService;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CartItemService extends IGeneralService<CartItem> {

    Optional<CartItem> findByProductId(String productId);

    List<CartItem> findAllByCart(Cart cart);

    BigDecimal getSumAmountByCartId(Long cartId);

    List<CartItemListDTO> findAllCartItemsDTO(@Param("cartId") Long cartId);

    void delete(CartItem cartItem);
}
