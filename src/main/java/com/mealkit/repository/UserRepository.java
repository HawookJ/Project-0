package com.mealkit.repository;

import com.mealkit.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {

    UserAccount findByUserName(String userName);
    Boolean existsByUserName(String userName);
    Boolean existsByEmail(String email);
    Optional<UserAccount> findByEmail(String email);

    //Optional<UserAccount> findByUserName(String username);
}