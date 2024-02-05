package org.petmarket.cart.servise;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.cart.dto.CartResponseDto;
import org.petmarket.cart.mapper.CartMapper;
import org.petmarket.cart.repository.CartRepository;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.users.entity.User;
import org.petmarket.users.service.UserService;
import org.springframework.stereotype.Service;

import static org.petmarket.utils.MessageUtils.USER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartMapper cartMapper;

    public CartResponseDto getCurrentUserCart() {
        User user = userService.getCurrentUser();
        if (user == null) {
            throw new ItemNotFoundException(USER_NOT_FOUND);
        }
        return cartMapper.mapEntityToDto(user.getCart());
    }
}
