package org.petmarket.cart.servise;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.petmarket.cart.dto.CartItemRequestDto;
import org.petmarket.cart.dto.CartResponseDto;
import org.petmarket.cart.entity.Cart;
import org.petmarket.cart.entity.CartItem;
import org.petmarket.cart.mapper.CartMapper;
import org.petmarket.cart.repository.CartRepository;
import org.petmarket.errorhandling.ItemNotFoundException;
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
    private final UserService userService;
    private final AdvertisementService advertisementService;
    private final CartMapper cartMapper;

    public CartResponseDto getCurrentUserCart() {
        User user = userService.getCurrentUser();
        if (user == null) {
            throw new ItemNotFoundException(USER_NOT_FOUND);
        }
        return cartMapper.mapEntityToDto(getCart(user));
    }

    @Transactional
    public CartResponseDto addItemsToCart(List<CartItemRequestDto> items) {
        User user = userService.getCurrentUser();
        if (user == null) {
            throw new ItemNotFoundException(USER_NOT_FOUND);
        }
        Cart cart = getCart(user);
        for (CartItemRequestDto item : items) {
            Advertisement advertisement = advertisementService.getAdvertisement(item.getAdvertisementId());
            CartItem foundItem = cart.getItemByAdvertisement(advertisement);
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
        User user = userService.getCurrentUser();
        if (user == null) {
            throw new ItemNotFoundException(USER_NOT_FOUND);
        }
        Cart cart = getCart(user);
        cart.getItems().clear();
        cart.setUpdated(LocalDateTime.now());
        cartRepository.save(cart);
        return cartMapper.mapEntityToDto(cart);
    }

    private Cart getCart(User user) {
        Cart cart = user.getCart();
        if (cart == null){
            cart = new Cart();
            cart.setUser(user);
        }
        if (cart.getItems().isEmpty()){
            cart.setCreated(LocalDateTime.now());
        }
        return cart;
    }
}
