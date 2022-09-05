package com.cg.repository;


import com.cg.model.Cart;
import com.cg.model.CartItem;
import com.cg.model.dto.CartItemListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByProductId(String productId);

    List<CartItem> findAllByCart(Cart cart);


    @Query("SELECT SUM(ci.amount) " +
            "FROM CartItem AS ci " +
            "WHERE ci.cart.id = :cartId")
    BigDecimal getSumAmountByCartId(@Param("cartId") Long cartId);


    @Query("SELECT NEW com.cg.model.dto.CartItemListDTO(" +
            "ci.id, " +
            "ci.productId, " +
            "ci.productName, " +
            "ci.price, " +
            "ci.quantity, " +
            "ci.amount, " +
            "pm.fileUrl" +
            ") FROM CartItem AS ci " +
            "JOIN Product AS p " +
            "ON p.id = ci.productId " +
            "JOIN ProductMedia AS pm " +
            "ON pm.product.id = ci.productId " +
            "WHERE ci.cart.id = :cartId"
    )
    List<CartItemListDTO> findAllCartItemsDTO(@Param("cartId") Long cartId);
}
