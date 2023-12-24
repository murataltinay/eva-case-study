package com.eva.demo.service;

import com.eva.demo.domain.UserPortfolio;
import com.eva.demo.domain.Users;
import com.eva.demo.exceptions.BusinessException;
import com.eva.demo.exceptions.ReasonType;
import com.eva.demo.repository.UserPortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPortfolioServiceImpl implements UserPortfolioService {

    private final UserPortfolioRepository userPortfolioRepository;
    @Override
    public UserPortfolio findByUserAndShareName(Users user, String shareName) {
        UserPortfolio userPortfolio = userPortfolioRepository.findByUsersAndShareName(user, shareName);
        if(userPortfolio == null) {
            throw new BusinessException(ReasonType.USER_PORTFOLIO_NOT_FOUNT);
        }
        return userPortfolio;
    }

    @Override
    public void save(UserPortfolio userPortfolio) {
        userPortfolioRepository.save(userPortfolio);
    }
}
