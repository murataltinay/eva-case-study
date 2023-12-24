package com.eva.demo.service;

import com.eva.demo.model.BuyShareRequest;
import com.eva.demo.model.ShareSellRequest;
import com.eva.demo.model.ShareSellUpdatePriceRequest;

public interface ShareTradeService {
    void sellShare(ShareSellRequest sellRequest);

    void updatePrice(ShareSellUpdatePriceRequest updatePriceRequest);

    void buyShare(BuyShareRequest buyShareRequest);
}
