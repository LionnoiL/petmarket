package org.petmarket.fake;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.repository.AdvertisementRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class FakeDataScheduler {

    private final FakeDataService fakeDataService;
    private final AdvertisementRepository advertisementRepository;

    @Scheduled(cron = "0 */7 * * * *")
    public void createData() {
        Random random = new Random();
        fakeDataService.createAdvertisements(random.nextInt(4));
    }
}
