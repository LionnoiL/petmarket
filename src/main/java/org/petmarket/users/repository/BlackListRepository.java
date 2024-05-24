package org.petmarket.users.repository;

import org.petmarket.users.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    Optional<BlackList> findByOwnerIdAndUserId(Long ownerId, Long userId);
}
