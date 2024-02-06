package org.petmarket.cart.servise;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.cart.dto.CartForCheckoutResponseDto;
import org.petmarket.cart.dto.CartItemRequestDto;
import org.petmarket.cart.dto.CartResponseDto;
import org.petmarket.cart.entity.Cart;
import org.petmarket.cart.entity.CartItem;
import org.petmarket.cart.mapper.CartMapper;
import org.petmarket.cart.repository.CartRepository;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.language.entity.Language;
import org.petmarket.users.entity.User;
import org.petmarket.users.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.petmarket.utils.MessageUtils.USER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartCheckoutService cartCheckoutService;
    private final UserService userService;
    private final AdvertisementService advertisementService;
    private final CartMapper cartMapper;

    private static void deleteCartItemByAdvertisementId(Long advertisementId, Cart cart) {
        cart.getItems().removeIf(item -> item.getAdvertisement().getId().equals(advertisementId));
    }

    public CartResponseDto getCurrentUserCart() {
        User user = getUser();
        return cartMapper.mapEntityToDto(getCart(user));
    }

    public CartForCheckoutResponseDto getUserCartForCheckout(Language language) {
        User user = getUser();
        Cart cart = getCart(user);
        return cartCheckoutService.getUserCartForCheckout(cart, language);
    }

    @Transactional
    public CartResponseDto addItemsToCart(List<CartItemRequestDto> items) {
        User user = getUser();
        Cart cart = getCart(user);
        for (CartItemRequestDto item : items) {
            Advertisement advertisement = advertisementService.getAdvertisement(
                    item.getAdvertisementId());
            CartItem foundItem = cart.getItemByAdvertisementId(item.getAdvertisementId());
            if (foundItem == null) {
                foundItem = CartItem.builder()
                        .cart(cart)
                        .advertisement(advertisement)
                        .title(item.getTitle())
                        .quantity(item.getQuantity())
                        .price(advertisement.getPrice())
                        .build();
                cart.getItems().add(foundItem);
            } else {
                foundItem.setQuantity(foundItem.getQuantity() + item.getQuantity());
            }
        }
        cart.setUpdated(LocalDateTime.now());
        cartRepository.save(cart);
        return cartMapper.mapEntityToDto(cart);
    }

    @Transactional
    public CartResponseDto clearCart() {
        User user = getUser();
        Cart cart = getCart(user);
        cart.getItems().clear();
        cart.setUpdated(LocalDateTime.now());
        cartRepository.save(cart);
        return cartMapper.mapEntityToDto(cart);
    }

    @Transactional
    public CartResponseDto deleteItemFromCart(Long advertisementId) {
        User user = getUser();
        Cart cart = getCart(user);
        deleteCartItemByAdvertisementId(advertisementId, cart);
        cart.setUpdated(LocalDateTime.now());
        cartRepository.save(cart);
        return cartMapper.mapEntityToDto(cart);
    }

    @Transactional
    public CartResponseDto changeItemQuantity(Long advertisementId, int quantity) {
        User user = getUser();
        Cart cart = getCart(user);
        if (quantity == 0) {
            deleteCartItemByAdvertisementId(advertisementId, cart);
        } else {
            CartItem cartItem = cart.getItemByAdvertisementId(advertisementId);
            cartItem.setQuantity(quantity);
        }
        cart.setUpdated(LocalDateTime.now());
        cartRepository.save(cart);
        return cartMapper.mapEntityToDto(cart);
    }

    private User getUser() {
        User user = userService.getCurrentUser();
        if (user == null) {
            throw new ItemNotFoundException(USER_NOT_FOUND);
        }
        return user;
    }

    private Cart getCart(User user) {
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        if (cart.getItems().isEmpty()) {
            cart.setCreated(LocalDateTime.now());
            cart.setUpdated(cart.getCreated());
        }
        return cart;
    }
}
