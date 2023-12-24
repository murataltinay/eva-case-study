package com.eva.demo.config;

import com.eva.demo.domain.UserPortfolio;
import com.eva.demo.domain.Users;
import com.eva.demo.repository.UserPortfolioRepository;
import com.eva.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DbInitializeComponent {

    private final UsersRepository usersRepository;
    private final UserPortfolioRepository userPortfolioRepository;


    @EventListener({ContextRefreshedEvent.class})
    public void contextRefreshedEvent() {

        Users user1 = new Users("Murat", "Altinay", new BigDecimal("100000"));
        Users user2 = new Users("Test-Name-1", "Test-Surname-1", new BigDecimal("100000"));
        Users user3 = new Users("Test-Name-2", "Test-Surname-2", new BigDecimal("100000"));
        Users user4 = new Users("Test-Name-3", "Test-Surname-3", new BigDecimal("100000"));
        Users user5 = new Users("Test-Name-4", "Test-Surname-4", new BigDecimal("100000"));
        usersRepository.saveAll(List.of(user1, user2, user3, user4, user5));

        UserPortfolio userPortfolio1 = new UserPortfolio(user1, "AAA", 30000);
        UserPortfolio userPortfolio2 = new UserPortfolio(user2, "AAA", 30000);
        UserPortfolio userPortfolio3 = new UserPortfolio(user3, "AAA", 30000);
        UserPortfolio userPortfolio4 = new UserPortfolio(user4, "AAA", 30000);
        UserPortfolio userPortfolio5 = new UserPortfolio(user5, "AAA", 30000);

        UserPortfolio userPortfolio6 = new UserPortfolio(user1, "BBB", 30000);
        UserPortfolio userPortfolio7 = new UserPortfolio(user2, "BBB", 30000);
        UserPortfolio userPortfolio8 = new UserPortfolio(user3, "BBB", 30000);
        UserPortfolio userPortfolio9 = new UserPortfolio(user4, "BBB", 30000);
        UserPortfolio userPortfolio10 = new UserPortfolio(user5, "BBB", 30000);

        UserPortfolio userPortfolio11 = new UserPortfolio(user1, "CCC", 30000);
        UserPortfolio userPortfolio12 = new UserPortfolio(user2, "CCC", 30000);
        UserPortfolio userPortfolio13 = new UserPortfolio(user3, "CCC", 30000);
        UserPortfolio userPortfolio14 = new UserPortfolio(user4, "CCC", 30000);
        UserPortfolio userPortfolio15 = new UserPortfolio(user5, "CCC", 30000);
        userPortfolioRepository.saveAll(List.of(userPortfolio1, userPortfolio2, userPortfolio3,
                userPortfolio4, userPortfolio5, userPortfolio6,userPortfolio7,
                userPortfolio8,userPortfolio9,userPortfolio10,userPortfolio11,
                userPortfolio12,userPortfolio13,userPortfolio14,userPortfolio15));


    }
}
