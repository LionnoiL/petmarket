package org.petmarket.cart.servise;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.entity.Advertisement;
import org.petmarket.cart.entity.Cart;
import org.petmarket.cart.entity.CartItem;
import org.petmarket.delivery.entity.Delivery;
import org.petmarket.payment.entity.Payment;
import org.petmarket.users.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartCheckoutService {

    public List<HashMap<User, List<Advertisement>>> splitCartByUser(Cart cart) {
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
