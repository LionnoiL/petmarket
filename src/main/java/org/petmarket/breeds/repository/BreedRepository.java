package org.petmarket.breeds.repository;

import org.petmarket.breeds.entity.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {

    @Query("SELECT b FROM Breed b WHERE b.category = :categoryId")
    List<Breed> findBreedByCategoryId(@Param("categoryId") Long categoryId);
}
