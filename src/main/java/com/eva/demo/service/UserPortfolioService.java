package com.eva.demo.service;

import com.eva.demo.domain.UserPortfolio;
import com.eva.demo.domain.Users;

public interface UserPortfolioService {
    UserPortfolio findByUserAndShareName(Users user, String shareName);

    void save(UserPortfolio userPortfolio);
}
