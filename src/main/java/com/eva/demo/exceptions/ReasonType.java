package com.eva.demo.exceptions;

import lombok.Getter;

@Getter
public enum ReasonType {
    PORTFOLIO_NOT_DEFINED("This username already taken !"),
    USER_NOT_FOUND("User not defined!"),
    USER_PORTFOLIO_NOT_FOUNT("You don't have related share"),
    SELL_SHARE_QUANTITY_NOT_ENOUGH("You don't have enough shares"),
    SELL_TRADE_NOT_FOUND("Related sell process not found"),
    ONE_HOUR_LIMIT("At least one hour must be kept for the transaction to occur"),
    NOT_ENOUGH_SHARES_FOR_SALE("Not enough shares for sale"),
    BALANCE_NOT_ENOUGH("Not enough balance you have related buy share operation");


    private final String message;
    ReasonType(String message) {
        this.message = message;
    }
}
