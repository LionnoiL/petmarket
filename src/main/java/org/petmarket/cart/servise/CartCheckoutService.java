package org.petmarket.cart.servise;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.dto.AdvertisementDetailsResponseDto;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.advertisements.advertisement.mapper.AdvertisementResponseTranslateMapper;
import org.petmarket.cart.dto.CartForCheckoutResponseDto;
import org.petmarket.cart.dto.CartItemResponseDto;
import org.petmarket.cart.dto.CartOrderResponseDto;
import org.petmarket.cart.entity.Cart;
import org.petmarket.cart.entity.CartItem;
import org.petmarket.delivery.dto.DeliveryResponseDto;
import org.petmarket.delivery.entity.Delivery;
import org.petmarket.language.entity.Language;
import org.petmarket.payment.dto.PaymentResponseDto;
import org.petmarket.payment.entity.Payment;
import org.petmarket.users.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartCheckoutService {

    private final AdvertisementResponseTranslateMapper advertisementMapper;

    public CartForCheckoutResponseDto getUserCartForCheckout(Cart cart, Language language) {
        List<HashMap<User, List<Advertisement>>> checkoutCart = splitCartByOrders(cart);
        CartForCheckoutResponseDto checkoutResponseDto = createNewCartForCheckoutResponse(cart);

        for (HashMap<User, List<Advertisement>> userListHashMap : checkoutCart) {
            for (Map.Entry<User, List<Advertisement>> entry : userListHashMap.entrySet()) {
                CartOrderResponseDto order = getCartOrderResponseDto(
                        language,
                        entry.getValue(),
                        cart,
                        entry.getKey()
                );
                checkoutResponseDto.getOrders().add(order);
            }
        }
        checkoutResponseDto.setTotalNumberPositions(cart.getItems().size());
        checkoutResponseDto.setNumberOfOrders(checkoutCart.size());

        return checkoutResponseDto;
    }

    private List<HashMap<User, List<Advertisement>>> splitCartByOrders(Cart cart) {
        List<HashMap<User, List<Advertisement>>> checkoutGroups = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            Advertisement advertisement = item.getAdvertisement();
            HashMap<User, List<Advertisement>> group = findGroupByUserPayAndDelivery(checkoutGroups, advertisement);

            if (group == null) {
                createList(checkoutGroups, advertisement);
            } else {
                addAdvertisementToGroup(group, advertisement);
            }
        }

        return checkoutGroups;
    }

    private CartOrderResponseDto getCartOrderResponseDto(Language language, List<Advertisement> advertisements,
                                                         Cart cart, User seller) {
        List<AdvertisementDetailsResponseDto> advertisementsDtos = advertisementMapper.mapEntityToDto(
                advertisements, language
        );
        List<DeliveryResponseDto> deliveries = advertisementsDtos.get(0).getDeliveries();
        List<PaymentResponseDto> payments = advertisementsDtos.get(0).getPayments();

        List<CartItemResponseDto> cartCheckoutItemsByGroup = createCartCheckoutItemsByGroup(cart,
                advertisementsDtos
        );

        return CartOrderResponseDto.builder()
                .sellerId(seller.getId())
                .sellerFirstName(seller.getFirstName())
                .sellerLastName(seller.getLastName())
                .deliveries(deliveries)
                .payments(payments)
                .items(cartCheckoutItemsByGroup)
                .build();
    }

    private CartForCheckoutResponseDto createNewCartForCheckoutResponse(Cart cart) {
        CartForCheckoutResponseDto checkoutResponseDto = new CartForCheckoutResponseDto();
        checkoutResponseDto.setId(cart.getId());
        checkoutResponseDto.setCreated(cart.getCreated());
        checkoutResponseDto.setUpdated(cart.getUpdated());
        checkoutResponseDto.setTotalQuantity(cart.getTotalQuantity());
        checkoutResponseDto.setTotalSum(cart.getTotalSum());
        return checkoutResponseDto;
    }

    private List<CartItemResponseDto> createCartCheckoutItemsByGroup(Cart cart,
                                                                     List<AdvertisementDetailsResponseDto> advDtos) {
        List<CartItemResponseDto> items = new ArrayList<>();
        for (AdvertisementDetailsResponseDto advDto : advDtos) {
            CartItem cartItem = cart.getItemByAdvertisementId(advDto.getId());
            items.add(CartItemResponseDto.builder()
                    .id(cartItem.getId())
                    .advertisementId(cartItem.getAdvertisement().getId())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getPrice())
                    .title(cartItem.getTitle())
                    .build());
        }
        return items;
    }

    private HashMap<User, List<Advertisement>> findGroupByUserPayAndDelivery(List<HashMap<User,
            List<Advertisement>>> checkoutGroups, Advertisement advertisement
    ) {
        for (HashMap<User, List<Advertisement>> checkoutGroup : checkoutGroups) {
            if (checkoutGroup.containsKey(advertisement.getAuthor())
                    && equalsPayments(advertisement, checkoutGroup.get(advertisement.getAuthor()).get(0))
                    && equalsDeliveries(advertisement, checkoutGroup.get(advertisement.getAuthor()).get(0))
            ) {
                return checkoutGroup;
            }
        }
        return null;
    }

    private boolean equalsPayments(Advertisement advertisement1, Advertisement advertisement2) {
        List<Long> payments1 = advertisement1.getPayments().stream()
                .map(Payment::getId)
                .sorted()
                .toList();

        List<Long> payments2 = advertisement2.getPayments().stream()
                .map(Payment::getId)
                .sorted()
                .toList();

        return payments1.equals(payments2);
    }

    private boolean equalsDeliveries(Advertisement advertisement1, Advertisement advertisement2) {
        List<Long> deliveries1 = advertisement1.getDeliveries().stream()
                .map(Delivery::getId)
                .sorted()
                .toList();

        List<Long> deliveries2 = advertisement2.getDeliveries().stream()
                .map(Delivery::getId)
                .sorted()
                .toList();

        return deliveries1.equals(deliveries2);
    }

    private void createList(List<HashMap<User, List<Advertisement>>> checkoutGroups, Advertisement advertisement) {
        HashMap<User, List<Advertisement>> listMap = new HashMap<>();
        List<Advertisement> listAdv = new ArrayList<>();
        listAdv.add(advertisement);
        listMap.put(advertisement.getAuthor(), listAdv);
        checkoutGroups.add(listMap);
    }

    private void addAdvertisementToGroup(HashMap<User, List<Advertisement>> group, Advertisement advertisement) {
        group.get(advertisement.getAuthor()).add(advertisement);
    }
}
