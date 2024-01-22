package org.petmarket.advertisements.advertisement.scheduler;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.petmarket.advertisements.advertisement.entity.AdvertisementTranslate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdvertisementHibernateIndexUpdateScheduler {

    private final EntityManager entityManager;

    @Scheduled(cron = "0 0 4 * * MON")
    public void updateIndex() {
        SearchSession searchSession = Search.session(entityManager);
        MassIndexer indexer = searchSession.massIndexer(AdvertisementTranslate.class)
                .threadsToLoadObjects(7);
        try {
            indexer.startAndWait();
        } catch (InterruptedException e) {
            log.error("Index update failed", e);
        }
    }
}
