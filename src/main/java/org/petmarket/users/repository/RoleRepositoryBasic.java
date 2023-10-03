package org.petmarket.users.repository;

import org.petmarket.users.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepositoryBasic extends JpaRepository<Role, Long> {
}
