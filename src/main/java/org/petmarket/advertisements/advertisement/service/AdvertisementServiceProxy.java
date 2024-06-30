package org.petmarket.advertisements.advertisement.service;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdvertisementServiceProxy {
    private final AdvertisementService advertisementService;

    @Autowired
    public AdvertisementServiceProxy(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }
}
