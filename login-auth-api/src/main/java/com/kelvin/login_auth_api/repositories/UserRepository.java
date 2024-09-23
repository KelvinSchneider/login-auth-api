package com.kelvin.login_auth_api.repositories;

import com.kelvin.login_auth_api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

}
