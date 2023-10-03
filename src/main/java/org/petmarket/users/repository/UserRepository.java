package org.petmarket.users.repository;

import org.petmarket.users.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends UserRepositoryBasic {

    Optional<User> findByEmail(String email);
}
