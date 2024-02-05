package org.petmarket.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.cart.dto.CartResponseDto;
import org.petmarket.cart.servise.CartService;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.petmarket.utils.annotations.responses.ApiResponseUnauthorized;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cart")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/v1/cart")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Get cart for current user")
    @ApiResponseSuccessful
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @GetMapping
    public CartResponseDto getUserCart() {
        log.info("Received request to get Cart");
        return cartService.getCurrentUserCart();
    }
}
