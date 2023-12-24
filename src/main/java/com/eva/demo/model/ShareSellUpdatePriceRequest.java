package com.eva.demo.model;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ShareSellUpdatePriceRequest {

    @NotNull
    private Long shareTradeID;

    @Digits(integer = 20, fraction = 2)
    private BigDecimal newPrice;

}
