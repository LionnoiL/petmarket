package org.petmarket.advertisements.advertisement.scheduler;

import lombok.RequiredArgsConstructor;
import org.petmarket.advertisements.advertisement.service.AdvertisementService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdvertisementScheduler {
        private final AdvertisementService advertisementService;

        @Scheduled(cron = "0 0 0 * * *")
        public void deleteExpiredAdvertisementsImages() {
                advertisementService.deleteOldDraftsImages();
        }
}
