package io.mars.springsecuritybasics.data;

import io.mars.springsecuritybasics.model.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
