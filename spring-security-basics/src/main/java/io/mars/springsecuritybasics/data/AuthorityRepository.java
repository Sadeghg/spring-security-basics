package io.mars.springsecuritybasics.data;

import io.mars.springsecuritybasics.model.entity.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {


}
