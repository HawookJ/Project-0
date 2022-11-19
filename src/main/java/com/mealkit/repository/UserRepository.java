package com.mealkit.repository;

import com.mealkit.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {

    UserAccount findByUserName(String userName);
    Boolean existsByUserName(String userName);
    Boolean existsByUserEmail(String userEmail);
    UserAccount findByUserEmail(String userEmail);



    //Optional<UserAccount> findByUserName(String username);
}