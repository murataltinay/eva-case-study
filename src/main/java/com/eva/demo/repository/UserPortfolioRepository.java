package com.eva.demo.repository;

import com.eva.demo.domain.Users;
import com.eva.demo.domain.UserPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPortfolioRepository extends JpaRepository<UserPortfolio, Long> {

    UserPortfolio findByUsersAndShareName(Users user, String shareName);
}
