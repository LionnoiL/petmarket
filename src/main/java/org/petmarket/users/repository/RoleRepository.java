package org.petmarket.users.repository;

import org.petmarket.users.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends RoleRepositoryBasic {

    Optional<Role> findByName(String name);
}
