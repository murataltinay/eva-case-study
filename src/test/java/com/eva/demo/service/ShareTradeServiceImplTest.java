package com.eva.demo.service;

import com.eva.demo.domain.ShareTrade;
import com.eva.demo.domain.UserPortfolio;
import com.eva.demo.domain.Users;
import com.eva.demo.exceptions.BusinessException;
import com.eva.demo.model.BuyShareRequest;
import com.eva.demo.model.ShareSellRequest;
import com.eva.demo.model.ShareSellUpdatePriceRequest;
import com.eva.demo.repository.ShareTradeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ShareTradeServiceImplTest {

    @InjectMocks
    private ShareTradeServiceImpl shareTradeService;

    @Mock
    private UsersService usersService;

    @Mock
    private ShareTradeRepository shareTradeRepository;

    @Mock
    private UserPortfolioService userPortfolioService;

    @Test
    void sellShare_Quantity_Not_Enough_Exception() {
        //Arrange
        Users user = new Users("murat", "altinay", new BigDecimal(100));
        UserPortfolio userPortfolio = new UserPortfolio(user, "AAA", 5);
        when(usersService.findByID(1L)).thenReturn(user);
        when(userPortfolioService.findByUserAndShareName(user, "AAA")).thenReturn(userPortfolio);
        ShareSellRequest request = new ShareSellRequest(1L, "AAA", 10, new BigDecimal(10));

        // Act & Assert
        Throwable exception = assertThrows(BusinessException.class, () -> shareTradeService.sellShare(request));
        assertEquals("You don't have enough shares", exception.getMessage());

    }

    @Test
    void sellShare_Success() {
        //Arrange
        Users user = new Users("murat", "altinay", new BigDecimal(100));
        UserPortfolio userPortfolio = new UserPortfolio(user, "AAA", 15);
        when(usersService.findByID(1L)).thenReturn(user);
        when(userPortfolioService.findByUserAndShareName(user, "AAA")).thenReturn(userPortfolio);
        when(shareTradeRepository.save(any())).thenReturn(any());
        ShareSellRequest request = new ShareSellRequest(1L, "AAA", 10, new BigDecimal(10));

        //Act
        shareTradeService.sellShare(request);

        //Assert
        verify(userPortfolioService, times(1)).save(userPortfolio);
        verify(shareTradeRepository, times(1)).save(any());
        assertEquals(5, userPortfolio.getQuantity());

    }

    @Test
    void updatePrice_one_hour_limit_exception() {
        //Arrange
        Users user = new Users("murat", "altinay", new BigDecimal(100));
        ShareTrade shareTrade = new ShareTrade(user, "AAA", 100, new BigDecimal(10), OffsetDateTime.now());
        when(shareTradeRepository.findById(1L)).thenReturn(Optional.of(shareTrade));
        ShareSellUpdatePriceRequest request = new ShareSellUpdatePriceRequest(1L, new BigDecimal(1000));

        // Act & Assert
        Throwable exception = assertThrows(BusinessException.class, () -> shareTradeService.updatePrice(request));
        assertEquals("At least one hour must be kept for the transaction to occur", exception.getMessage());
    }


    @Test
    void updatePrice_success() {
        //Arrange
        Users user = new Users("murat", "altinay", new BigDecimal(100));
        OffsetDateTime transactionDate = OffsetDateTime.of(2023, 12, 24, 12, 12, 12, 12, ZoneOffset.UTC);
        ShareTrade shareTrade = new ShareTrade(user, "AAA", 100, new BigDecimal(10), transactionDate);
        when(shareTradeRepository.findById(1L)).thenReturn(Optional.of(shareTrade));
        ShareSellUpdatePriceRequest request = new ShareSellUpdatePriceRequest(1L, new BigDecimal(1000));

        // Act
        shareTradeService.updatePrice(request);

        //Assert
        assertEquals(new BigDecimal(1000), shareTrade.getOneLotPrice());
        verify(shareTradeRepository, times(1)).save(shareTrade);
    }

    @Test
    void buyShare_not_enough_share_for_sale_exception() {
        //Arrange
        Users buyerUser = new Users("murat", "altinay", new BigDecimal(100));
        Users sellerUser_1 = new Users("testName", "testSurname", new BigDecimal(100));
        Users sellerUser_2 = new Users("testName_1", "testSurname_1", new BigDecimal(100));
        UserPortfolio userPortfolio = new UserPortfolio(buyerUser, "AAA", 0);
        ShareTrade shareTrade_1 = new ShareTrade(sellerUser_1, "AAA", 100, new BigDecimal(10), OffsetDateTime.now());
        ShareTrade shareTrade_2 = new ShareTrade(sellerUser_2, "AAA", 100, new BigDecimal(20), OffsetDateTime.now());
        when(shareTradeRepository.findAllByShareNameAndDeletedFalseOrderByOneLotPrice("AAA", buyerUser)).thenReturn(List.of(shareTrade_1, shareTrade_2));
        when(usersService.findByID(1L)).thenReturn(buyerUser);
        when(userPortfolioService.findByUserAndShareName(buyerUser, "AAA")).thenReturn(userPortfolio);
        BuyShareRequest request = new BuyShareRequest(1L,"AAA", 10000);

        // Act & Assert
        Throwable exception = assertThrows(BusinessException.class, () -> shareTradeService.buyShare(request));
        assertEquals("Not enough shares for sale", exception.getMessage());

    }

    @Test
    void buyShare_not_enough_balance_exception() {
        //Arrange
        Users buyerUser = new Users("murat", "altinay", new BigDecimal(0));
        Users sellerUser_1 = new Users("testName", "testSurname", new BigDecimal(100));
        Users sellerUser_2 = new Users("testName_1", "testSurname_1", new BigDecimal(100));
        UserPortfolio userPortfolio = new UserPortfolio(buyerUser, "AAA", 0);
        ShareTrade shareTrade_1 = new ShareTrade(sellerUser_1, "AAA", 100, new BigDecimal(10), OffsetDateTime.now());
        ShareTrade shareTrade_2 = new ShareTrade(sellerUser_2, "AAA", 100, new BigDecimal(20), OffsetDateTime.now());
        when(shareTradeRepository.findAllByShareNameAndDeletedFalseOrderByOneLotPrice("AAA", buyerUser)).thenReturn(List.of(shareTrade_1, shareTrade_2));
        when(usersService.findByID(1L)).thenReturn(buyerUser);
        when(userPortfolioService.findByUserAndShareName(buyerUser, "AAA")).thenReturn(userPortfolio);
        BuyShareRequest request = new BuyShareRequest(1L,"AAA", 100);

        // Act & Assert
        Throwable exception = assertThrows(BusinessException.class, () -> shareTradeService.buyShare(request));
        assertEquals("Not enough balance you have related buy share operation", exception.getMessage());
    }

    @Test
    void buyShare_not_enough_balance_success() {
        //Arrange
        Users buyerUser = new Users("murat", "altinay", new BigDecimal(2000));
        Users sellerUser_1 = new Users("testName", "testSurname", new BigDecimal(100));
        Users sellerUser_2 = new Users("testName_1", "testSurname_1", new BigDecimal(100));
        UserPortfolio userPortfolio = new UserPortfolio(buyerUser, "AAA", 0);
        ShareTrade shareTrade_1 = new ShareTrade(sellerUser_1, "AAA", 100, new BigDecimal(10), OffsetDateTime.now());
        ShareTrade shareTrade_2 = new ShareTrade(sellerUser_2, "AAA", 100, new BigDecimal(20), OffsetDateTime.now());
        when(shareTradeRepository.findAllByShareNameAndDeletedFalseOrderByOneLotPrice("AAA", buyerUser)).thenReturn(List.of(shareTrade_1, shareTrade_2));
        when(usersService.findByID(1L)).thenReturn(buyerUser);
        when(userPortfolioService.findByUserAndShareName(buyerUser, "AAA")).thenReturn(userPortfolio);
        BuyShareRequest request = new BuyShareRequest(1L,"AAA", 150);

        // Act
        shareTradeService.buyShare(request);

        //Assert
        assertEquals(150, userPortfolio.getQuantity());
        assertEquals(BigDecimal.ZERO, buyerUser.getBalance());
        assertEquals(0, shareTrade_1.getQuantity());
        assertTrue(shareTrade_1.isDeleted());
        assertFalse(shareTrade_2.isDeleted());
        assertEquals(50, shareTrade_2.getQuantity());

    }
}