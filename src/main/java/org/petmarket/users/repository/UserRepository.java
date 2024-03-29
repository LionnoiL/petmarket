package org.petmarket.users.repository;

import org.petmarket.users.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends UserRepositoryBasic {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailConfirmCode(String code);

    @Query(value = "SELECT * FROM users ORDER BY RAND() LIMIT 1", nativeQuery = true)
    User findRandomEntity();

    @Modifying
    @Query("UPDATE User u SET u.status = org.petmarket.users.entity.UserStatus.DELETED WHERE u.id = :id")
    void setUserStatusToDeleted(@Param("id") Long id);
}
