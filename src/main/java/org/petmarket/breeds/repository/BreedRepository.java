package org.petmarket.breeds.repository;

import org.petmarket.breeds.entity.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {
    List<Breed> findBreedByCategoryId(@Param("categoryId") Long categoryId);

    @Query(value = "SELECT * FROM animal_breeds ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Breed findRandomEntity();

    @Query(value = "SELECT * FROM animal_breeds WHERE category_id = :id ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Breed findRandomEntityByCategoryId(@Param("id") long id);
}
