package org.petmarket.fake;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class FakeDataScheduler {

    private final FakeDataService fakeDataService;

    @Value("${fake.data.enabled}")
    private boolean fakeDataEnabled;

    @Scheduled(cron = "0 */7 * * * *")
    public void createData() {
        if (fakeDataEnabled) {
            Random random = new Random();
            fakeDataService.createAdvertisements(random.nextInt(4));
        }
    }
}
