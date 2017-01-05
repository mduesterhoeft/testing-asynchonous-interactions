/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package com.epages.checkout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@ExposesResourceFor(Cart.class)
public class CartController {

    private final CartRepository cartRepository;
    private final ProductRefRepository productRefRepository;
    private final EntityLinks entityLinks;

    @PostMapping
    public ResponseEntity<Void> createCart() {
        Cart cart = cartRepository.saveAndFlush(new Cart());

        URI location = entityLinks.linkForSingleResource(cart).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Resource<Cart>> getCart(@PathVariable Long cartId) {
        return Optional.ofNullable(cartRepository.findOne(cartId))
                .map(cart -> new Resource<>(cart, entityLinks.linkForSingleResource(cart).withSelfRel()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<Resource<Cart>> addLineItem(@PathVariable Long cartId,
                                                      @RequestBody @Valid AddLineItemRequest addLineItemRequest) {
        return Optional.ofNullable(cartRepository.findOne(cartId))
                .map(cart -> addLineItemOrIncrementQuantity(cart, addLineItemRequest))
                .map(cart -> cartRepository.saveAndFlush(cart))
                .map(cart -> new Resource<>(cart, entityLinks.linkForSingleResource(cart).withSelfRel()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private Cart addLineItemOrIncrementQuantity(Cart cart, AddLineItemRequest addLineItemRequest) {
        Optional<ProductLineItem> existingLineItemForProduct = cart.getLineItems().stream()
                .filter(item -> item.getProduct().getId().equals(addLineItemRequest.getProductId()))
                .findFirst();

        return existingLineItemForProduct.map(item -> {
            item.setQuantity(item.getQuantity() + addLineItemRequest.getQuantity());
            return cart;
        }).orElseGet(() -> {
            ProductRef product = productRefRepository.findOne(addLineItemRequest.getProductId());
            cart.getLineItems().add(ProductLineItem.builder()
                    .product(product)
                    .quantity(addLineItemRequest.getQuantity()).build());
            return cart;
        });
    }

    @AllArgsConstructor
    @Getter
    static class AddLineItemRequest {
        @NotNull
        private final int quantity;
        @NotNull
        private final Long productId;
    }
}
