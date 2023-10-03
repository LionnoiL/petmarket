package org.petmarket.users.repository;

import org.petmarket.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryBasic extends JpaRepository<User, Long> {
}
