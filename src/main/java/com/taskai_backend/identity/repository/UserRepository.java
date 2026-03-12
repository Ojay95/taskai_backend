package com.taskai_backend.identity.repository;



import com.taskai_backend.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Used for login and finding the user context for task assignment.
     */
    Optional<User> findByEmail(String email);

    /**
     * Used during signup to prevent duplicate accounts.
     */
    Boolean existsByEmail(String email);
}