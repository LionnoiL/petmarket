package org.petmarket.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.cart.dto.CartForCheckoutResponseDto;
import org.petmarket.cart.dto.CartItemRequestDto;
import org.petmarket.cart.dto.CartResponseDto;
import org.petmarket.cart.servise.CartService;
import org.petmarket.language.entity.Language;
import org.petmarket.language.service.LanguageService;
import org.petmarket.utils.annotations.parametrs.ParameterLanguage;
import org.petmarket.utils.annotations.responses.ApiResponseBadRequest;
import org.petmarket.utils.annotations.responses.ApiResponseNotFound;
import org.petmarket.utils.annotations.responses.ApiResponseSuccessful;
import org.petmarket.utils.annotations.responses.ApiResponseUnauthorized;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cart")
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/v1/cart")
public class CartController {

    private final CartService cartService;
    private final LanguageService languageService;

    @Operation(summary = "Get cart for current user")
    @ApiResponseSuccessful
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public CartResponseDto getUserCart() {
        log.info("Received request to get Cart");
        return cartService.getCurrentUserCart();
    }

    @Operation(summary = "Get the current user's shopping cart for checkout")
    @ApiResponseSuccessful
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/checkout/{langCode}")
    public CartForCheckoutResponseDto getUserCartForCheckout(@ParameterLanguage @PathVariable String langCode) {
        log.info("Received request to get Cart for checkout");
        Language language = languageService.getByLangCode(langCode);
        return cartService.getUserCartForCheckout(language);
    }

    @Operation(summary = "Put items into the cart",
            description = "Put items into the cart for current user")
    @ApiResponseSuccessful
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @ApiResponseBadRequest
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/items")
    public CartResponseDto addItems(@Valid @RequestBody List<CartItemRequestDto> items) {
        log.info("Received request to add item to Cart");
        return cartService.addItemsToCart(items);
    }

    @Operation(summary = "Clear cart for current user",
            description = "Clear all items from the cart for current user")
    @ApiResponseSuccessful
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/items")
    public CartResponseDto clearCart() {
        return cartService.clearCart();
    }

    @Operation(summary = "Delete item from the cart",
            description = "Delete item from the cart by advertisement id")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/items/{advertisementId}")
    public CartResponseDto deleteItem(@PathVariable Long advertisementId) {
        return cartService.deleteItemFromCart(advertisementId);
    }

    @Operation(summary = "Set item quantity in cart",
            description = "If the quantity equals 0, the item will be deleted.")
    @ApiResponseSuccessful
    @ApiResponseBadRequest
    @ApiResponseNotFound
    @ApiResponseUnauthorized
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/items/{advertisementId}")
    public CartResponseDto changeItemQuantity(
            @PathVariable Long advertisementId,
            @RequestParam @Min(0) int quantity
    ) {
        return cartService.changeItemQuantity(advertisementId, quantity);
    }
}
