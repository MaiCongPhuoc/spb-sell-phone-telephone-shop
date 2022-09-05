package com.cg.service.cart;

import com.cg.model.Cart;
import com.cg.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<Cart> findAll() {
        return null;
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Cart getById(Long id) {
        return null;
    }

    @Override
    public Optional<Cart> findByCreatedBy(String createdBy) {
        return cartRepository.findByCreatedBy(createdBy);
    }

    @Override
    public Boolean existsByCreatedBy(String createdBy) {
        return cartRepository.existsByCreatedBy(createdBy);
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void delete(Cart cart) {
        cartRepository.delete(cart);
    }
}
