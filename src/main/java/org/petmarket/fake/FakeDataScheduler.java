package org.petmarket.fake;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.advertisements.advertisement.entity.AdvertisementStatus;
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

    @Scheduled(cron = "0 */10 * * * *")
    public void createData10() {
        Random random = new Random();
        fakeDataService.createUsers(random.nextInt(3));
        fakeDataService.createAdvertisements(random.nextInt(10));
    }

    @Scheduled(cron = "0 */3 * * * *")
    public void createData3() {
        Random random = new Random();
        fakeDataService.createAdvertisements(random.nextInt(4));
    }

    @Scheduled(cron = "0 */30 * * * *")
    public void approveAdvertisement() {
        advertisementRepository.updateStatus(
                AdvertisementStatus.DRAFT, AdvertisementStatus.ACTIVE
        );
    }
}
