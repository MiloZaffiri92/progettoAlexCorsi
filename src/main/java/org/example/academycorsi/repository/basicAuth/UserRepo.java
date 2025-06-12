package org.example.academycorsi.repository.basicAuth;

import org.example.academycorsi.data.entityAuthSecurity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    boolean existsByUsername(String username);
}
