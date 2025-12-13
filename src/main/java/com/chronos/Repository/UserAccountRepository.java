package com.chronos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chronos.Entity.UserAccount;



@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount,Long> {

}
