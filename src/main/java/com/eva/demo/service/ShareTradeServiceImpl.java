package com.eva.demo.service;

import com.eva.demo.domain.ShareTrade;
import com.eva.demo.domain.UserPortfolio;
import com.eva.demo.domain.Users;
import com.eva.demo.exceptions.BusinessException;
import com.eva.demo.exceptions.ReasonType;
import com.eva.demo.model.BuyShareRequest;
import com.eva.demo.model.ShareSellRequest;
import com.eva.demo.model.ShareSellUpdatePriceRequest;
import com.eva.demo.repository.ShareTradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShareTradeServiceImpl implements ShareTradeService {

    private final UsersService usersService;
    private final UserPortfolioService userPortfolioService;
    private final ShareTradeRepository shareTradeRepository;

    @Override
    @Transactional
    public void sellShare(ShareSellRequest sellRequest) {
        Users user = usersService.findByID(sellRequest.getUserID());
        UserPortfolio userPortfolio = userPortfolioService.findByUserAndShareName(user, sellRequest.getShareName());
        if (sellRequest.getQuantity() > userPortfolio.getQuantity()) {
            throw new BusinessException(ReasonType.SELL_SHARE_QUANTITY_NOT_ENOUGH);
        }

        Integer oldQuantity = userPortfolio.getQuantity();
        Integer newQuantity = oldQuantity - sellRequest.getQuantity();
        userPortfolio.setQuantity(newQuantity);
        userPortfolioService.save(userPortfolio);

        ShareTrade newShareTrade = new ShareTrade(user, sellRequest.getShareName(), sellRequest.getQuantity(), sellRequest.getOneLotPrice(), OffsetDateTime.now());
        shareTradeRepository.save(newShareTrade);
    }

    @Override
    @Transactional
    public void updatePrice(ShareSellUpdatePriceRequest updatePriceRequest) {
        Optional<ShareTrade> optionalShareTrade = shareTradeRepository.findById(updatePriceRequest.getShareTradeID());
        if (optionalShareTrade.isEmpty()) {
            throw new BusinessException(ReasonType.SELL_TRADE_NOT_FOUND);
        }
        ShareTrade shareTrade = optionalShareTrade.get();
        long differenceHours = ChronoUnit.HOURS.between(OffsetDateTime.now(), shareTrade.getTransactionDate());
        if (differenceHours < 1) {
            throw new BusinessException(ReasonType.ONE_HOUR_LIMIT);
        }
        shareTrade.setOneLotPrice(updatePriceRequest.getNewPrice());
        shareTradeRepository.save(shareTrade);
    }

    @Override
    @Transactional
    public void buyShare(BuyShareRequest buyShareRequest) {
        Users user = usersService.findByID(buyShareRequest.getUserID());
        UserPortfolio userPortfolio = userPortfolioService.findByUserAndShareName(user, buyShareRequest.getShareName());
        List<ShareTrade> shareTrades = shareTradeRepository.findAllByShareNameAndDeletedFalseOrderByOneLotPrice(buyShareRequest.getShareName(), user);
        Integer totalQuantities = 0;
        for (ShareTrade shareTrade : shareTrades) {
            totalQuantities += shareTrade.getQuantity();
        }
        if (totalQuantities < buyShareRequest.getQuantity()) {
            throw new BusinessException(ReasonType.NOT_ENOUGH_SHARES_FOR_SALE);
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        Integer shareToBePurchased = 0;
        int quantityToBeReceivedFromLastShare = 0;
        for (ShareTrade shareTrade : shareTrades) {
            shareToBePurchased += shareTrade.getQuantity();
            totalPrice = totalPrice.add(shareTrade.getOneLotPrice().multiply(BigDecimal.valueOf(shareTrade.getQuantity()))).setScale(2, RoundingMode.HALF_UP);
            if (shareToBePurchased > buyShareRequest.getQuantity()) {
                shareToBePurchased -= shareTrade.getQuantity();
                totalPrice = totalPrice.subtract(shareTrade.getOneLotPrice().multiply(BigDecimal.valueOf(shareTrade.getQuantity())).setScale(2, RoundingMode.HALF_UP));
                quantityToBeReceivedFromLastShare = buyShareRequest.getQuantity() - shareToBePurchased;
                shareToBePurchased += quantityToBeReceivedFromLastShare;
                totalPrice = totalPrice.add(shareTrade.getOneLotPrice().multiply(BigDecimal.valueOf(quantityToBeReceivedFromLastShare)));
                break;
            }

        }

        if (totalPrice.compareTo(user.getBalance()) > 0) {
            throw new BusinessException(ReasonType.BALANCE_NOT_ENOUGH);
        }

        for (ShareTrade shareTrade : shareTrades) {
            if (shareToBePurchased >= shareTrade.getQuantity()) {
                shareToBePurchased -= shareTrade.getQuantity();
                buyShare(user, userPortfolio, shareTrade, shareTrade.getQuantity());
            } else {
                buyShare(user, userPortfolio, shareTrade, quantityToBeReceivedFromLastShare);
                break;
            }
        }

    }

    private void buyShare(Users user, UserPortfolio userPortfolio, ShareTrade shareTrade, Integer quantity) {
        BigDecimal price = shareTrade.getOneLotPrice().multiply(BigDecimal.valueOf(quantity));
        BigDecimal buyerUserNewBalance = user.getBalance().subtract(price);
        user.setBalance(buyerUserNewBalance);
        usersService.save(user);

        Users sellerUser = shareTrade.getUsers();
        BigDecimal sellerUserNewBalance = sellerUser.getBalance().add(price);
        sellerUser.setBalance(sellerUserNewBalance);
        usersService.save(sellerUser);

        Integer newQuantity = userPortfolio.getQuantity() + quantity;
        userPortfolio.setQuantity(newQuantity);
        userPortfolioService.save(userPortfolio);

        Integer newQuantityShareTrade = shareTrade.getQuantity() - quantity;
        shareTrade.setQuantity(newQuantityShareTrade);
        if (shareTrade.getQuantity() == 0) {
            shareTrade.setDeleted(true);
        }
        shareTradeRepository.save(shareTrade);
    }
}
