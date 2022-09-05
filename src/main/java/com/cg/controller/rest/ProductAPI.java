package com.cg.controller.rest;

import com.cg.model.Cart;
import com.cg.model.CartItem;
import com.cg.model.ProductMedia;
import com.cg.model.dto.CartItemListDTO;
import com.cg.model.dto.ProductRequestCartDTO;
import com.cg.service.cart.CartService;
import com.cg.service.cartItem.CartItemService;
import com.cg.service.productMedia.ProductMediaService;
import com.cg.util.AppUtil;
import com.cg.exception.DataInputException;
import com.cg.model.Product;
import com.cg.model.dto.IProductDTO;
import com.cg.model.dto.ProductDTO;
import com.cg.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductAPI {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMediaService productMediaService;

    @Autowired
    private AppUtil appUtils;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;


    @GetMapping
    public ResponseEntity<Iterable<?>> findAll() {
        try {
            Iterable<IProductDTO> iProductDTOS = productService.findAllIProductDTO();

            if (((List) iProductDTOS).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(iProductDTOS, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> findProductById(@PathVariable("id") String id) {
//
//        try {
//            Optional<Product> optionalProductDTO = productService.findById(id);
//
//            return new ResponseEntity<>(optionalProductDTO.get(), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Validated ProductDTO productDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        try {
            BigDecimal price = productDTO.getPrice();
            productDTO.setPrice(price);
            int quantity = productDTO.getQuantity();
            productDTO.setQuantity(quantity);
            String description = productDTO.getDescription();
            productDTO.setDescription(description);

            Product createdProduct = productService.create(productDTO);

            IProductDTO iProductDTO = productService.findIProductDTOById(createdProduct.getId());

            return new ResponseEntity<>(iProductDTO, HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Product creation information is not valid, please check the information again");
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<?> doEdit(@Validated ProductDTO productDTO, @PathVariable String id, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return appUtils.mapErrorToResponse(bindingResult);

        try {
            BigDecimal price = productDTO.getPrice();
            productDTO.setPrice(price);
            int quantity = productDTO.getQuantity();
            productDTO.setQuantity(quantity);
            String description = productDTO.getDescription();
            productDTO.setDescription(description);

            Product editProduct = productService.edit(productDTO, id);

            IProductDTO iProductDTO = productService.findIProductDTOById(editProduct.getId());

            return new ResponseEntity<>(iProductDTO, HttpStatus.OK);

        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Product creation information is not valid, please check the information again");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) throws IOException {

        Optional<Product> product = productService.findById(id);

        if (product.isPresent()) {
            productService.delete(product.get());

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            throw new DataInputException("Invalid product information");
        }
    }

    @PostMapping("/add-cart")
    public ResponseEntity<?> addCart(@RequestBody ProductRequestCartDTO productRequestCartDTO) {

        String createdBy = AppUtil.getPrincipalUsername();

        System.out.println(createdBy);

        Optional<Cart> cartOptional = cartService.findByCreatedBy(createdBy);

//        Boolean existsByCreatedBy = cartService.existsByCreatedBy(createdBy);

        String productId = productRequestCartDTO.getId();

        Optional<Product> product = productService.findById(productId);

        if (!product.isPresent()) {
            throw new DataInputException("Thông tin sản phẩm không hợp lệ");
        }

        if (!cartOptional.isPresent()) {
            Cart cart = new Cart();
            cart.setCreatedBy(createdBy);
            cart.setTotalAmount(new BigDecimal(0L));

            Cart newCard = cartService.save(cart);

            CartItem cartItem = new CartItem();
            cartItem.setId(0L);
            cartItem.setProductId(productId);
            cartItem.setProductName(product.get().getProductName());
            cartItem.setPrice(product.get().getPrice());
            cartItem.setQuantity(1);
            cartItem.setAmount(product.get().getPrice());
            cartItem.setCart(newCard);
            cartItemService.save(cartItem);

            Long cartId = newCard.getId();
            newCard.setTotalAmount(cartItem.getPrice());
            cartService.save(newCard);

            List<CartItemListDTO> cartItemListDTOS = cartItemService.findAllCartItemsDTO(cartId);
            return new ResponseEntity<>(cartItemListDTOS, HttpStatus.CREATED);
        }

        Optional<CartItem> cartItemOptional = cartItemService.findByProductId(productId);

        if (!cartItemOptional.isPresent()) {
            CartItem cartItem = new CartItem();
            cartItem.setId(0L);
            cartItem.setProductId(productId);
            cartItem.setProductName(product.get().getProductName());
            cartItem.setPrice(product.get().getPrice());
            cartItem.setQuantity(1);
            cartItem.setAmount(product.get().getPrice());
            cartItem.setCart(cartOptional.get());
            cartItemService.save(cartItem);

            Long cartId = cartOptional.get().getId();
            BigDecimal totalAmount = cartItemService.getSumAmountByCartId(cartId);
            Cart cart = cartOptional.get();
            cart.setTotalAmount(totalAmount);
            cartService.save(cart);

            List<CartItemListDTO> cartItemListDTOS = cartItemService.findAllCartItemsDTO(cartId);
            return new ResponseEntity<>(cartItemListDTOS, HttpStatus.CREATED);
        }

        BigDecimal price = product.get().getPrice();
        int oldQuantity = cartItemOptional.get().getQuantity();
        int newQuantity = oldQuantity + 1;
        BigDecimal newAmount = price.multiply(new BigDecimal(newQuantity));

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemOptional.get().getId());
        cartItem.setProductId(productId);
        cartItem.setProductName(product.get().getProductName());
        cartItem.setPrice(product.get().getPrice());
        cartItem.setQuantity(newQuantity);
        cartItem.setAmount(newAmount);
        cartItem.setCart(cartOptional.get());
        cartItemService.save(cartItem);

        BigDecimal totalAmount = cartItemService.getSumAmountByCartId(cartOptional.get().getId());


        Long cartId = cartOptional.get().getId();

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setCreatedBy(createdBy);
        cart.setTotalAmount(totalAmount);
        cartService.save(cart);

        List<CartItemListDTO> cartItemListDTOS = cartItemService.findAllCartItemsDTO(cartId);
        return new ResponseEntity<>(cartItemListDTOS, HttpStatus.CREATED);
    }

    @PostMapping("/minus-cart")
    public ResponseEntity<?> minusCart(@RequestBody ProductRequestCartDTO productRequestCartDTO) {

        String createdBy = AppUtil.getPrincipalUsername();

        System.out.println(createdBy);

        Optional<Cart> cartOptional = cartService.findByCreatedBy(createdBy);

//        Boolean existsByCreatedBy = cartService.existsByCreatedBy(createdBy);

        String productId = productRequestCartDTO.getId();

        Optional<Product> product = productService.findById(productId);

        if (!product.isPresent()) {
            throw new DataInputException("Thông tin sản phẩm không hợp lệ");
        }

        Optional<CartItem> cartItemOptional = cartItemService.findByProductId(productId);

        BigDecimal price = product.get().getPrice();
        int oldQuantity = cartItemOptional.get().getQuantity();
        int newQuantity = oldQuantity - 1;
        if (newQuantity <= 0) {
            newQuantity = 1;
        }
        BigDecimal newAmount = price.multiply(new BigDecimal(newQuantity));

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemOptional.get().getId());
        cartItem.setProductId(productId);
        cartItem.setProductName(product.get().getProductName());
        cartItem.setPrice(product.get().getPrice());
        cartItem.setQuantity(newQuantity);
        cartItem.setAmount(newAmount);
        cartItem.setCart(cartOptional.get());
        cartItemService.save(cartItem);

        BigDecimal totalAmount = cartItemService.getSumAmountByCartId(cartOptional.get().getId());


        Long cartId = cartOptional.get().getId();

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setCreatedBy(createdBy);
        cart.setTotalAmount(totalAmount);
        cartService.save(cart);

        List<CartItemListDTO> cartItemListDTOS = cartItemService.findAllCartItemsDTO(cartId);
        return new ResponseEntity<>(cartItemListDTOS, HttpStatus.CREATED);

    }

    @GetMapping("/search/{search}")
    public ResponseEntity<Iterable<?>> searchByProductname(@PathVariable String search) {
        try {
            Iterable<IProductDTO> iProductDTOS = productService.findProductByProductName(search);

            if (((List) iProductDTOS).isEmpty()) {
                Iterable<IProductDTO> iProductDTO = productService.findAllIProductDTO();
                return new ResponseEntity<>(iProductDTO, HttpStatus.OK);
            }

            return new ResponseEntity<>(iProductDTOS, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
