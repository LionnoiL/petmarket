package org.petmarket.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Helper {

    public static String getRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
