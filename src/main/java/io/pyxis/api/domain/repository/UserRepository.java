package io.pyxis.api.domain.repository;

import io.pyxis.api.domain.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String name);

}
