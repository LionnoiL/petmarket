package org.petmarket.pages.repository;

import org.petmarket.pages.entity.SitePage;
import org.petmarket.pages.entity.SitePageType;
import org.springframework.stereotype.Repository;

@Repository
public interface SitePageRepository extends SitePageRepositoryBasic {

    SitePage findByType(SitePageType type);
}
