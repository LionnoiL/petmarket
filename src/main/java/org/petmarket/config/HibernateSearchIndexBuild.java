package org.petmarket.config;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class HibernateSearchIndexBuild implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private EntityManager entityManager;

    @Value("${hibernate.search.indexing.startup.enabled}")
    private boolean indexOnStartup;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!indexOnStartup) {
            return;
        }

        SearchSession searchSession = Search.session(entityManager);
        MassIndexer indexer = searchSession.massIndexer()
                .idFetchSize(150)
                .batchSizeToLoadObjects(25)
                .threadsToLoadObjects(12);
        try {
            indexer.startAndWait();
        } catch (InterruptedException e) {
            log.warn("Failed to load data from database");
            Thread.currentThread().interrupt();
        }
        log.info("Completed Indexing");
    }
}
