package org.petmarket.media.storage.repository;

import org.petmarket.media.storage.dto.MediaResponseDto;
import org.petmarket.media.storage.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    @Query("""
            SELECT new org.petmarket.media.storage.dto.MediaResponseDto(m.id, m.name, m.url, m.urlSmall)
            FROM Media m
            """)
    Page<MediaResponseDto> findAllMediaResponse(Pageable pageable);

    @Query("""
            SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END
            FROM Media m
            WHERE m.name LIKE CONCAT(:id, ' %')
            """)
    boolean existsByStartingIdInName(Long id);
}
