package org.petmarket.users.repository;

import org.petmarket.users.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends UserRepositoryBasic {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailConfirmCode(String code);

    @Query(value = "SELECT * FROM users ORDER BY RAND() LIMIT 1", nativeQuery = true)
    User findRandomEntity();
}
