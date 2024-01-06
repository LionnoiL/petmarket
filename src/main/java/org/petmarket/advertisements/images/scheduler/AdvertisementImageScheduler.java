package org.petmarket.advertisements.images.scheduler;

import lombok.RequiredArgsConstructor;
import org.petmarket.advertisements.images.service.AdvertisementImageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdvertisementImageScheduler {
        private final AdvertisementImageService advertisementImageService;

        @Scheduled(cron = "0 0 0 * * *")
        public void deleteExpiredAdvertisementsImages() {
                advertisementImageService.deleteOldDraftsImages();
        }
}
