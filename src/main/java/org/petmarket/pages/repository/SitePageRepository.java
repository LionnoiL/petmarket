package org.petmarket.pages.repository;

import org.petmarket.pages.entity.SitePage;
import org.petmarket.pages.entity.SitePageType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SitePageRepository extends SitePageRepositoryBasic {

    Optional<SitePage> findByType(SitePageType type);
}
