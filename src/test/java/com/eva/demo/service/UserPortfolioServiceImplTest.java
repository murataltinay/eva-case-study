package com.eva.demo.service;

import com.eva.demo.domain.Users;
import com.eva.demo.exceptions.BusinessException;
import com.eva.demo.repository.UserPortfolioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserPortfolioServiceImplTest {
    @InjectMocks
    private UserPortfolioServiceImpl userPortfolioService;

    @Mock
    private UserPortfolioRepository userPortfolioRepository;

    @Test
    void findByUserAndShareName_Not_Found_Exception() {
        //Arrange
        Users user = new Users("murat", "altinay", new BigDecimal(100));
        when(userPortfolioRepository.findByUsersAndShareName(user,"AAA")).thenReturn(null);

        // Act & Assert
        Throwable exception = assertThrows(BusinessException.class, () -> userPortfolioService.findByUserAndShareName(user,"AAA"));
        assertEquals("You don't have related share", exception.getMessage());
    }
}