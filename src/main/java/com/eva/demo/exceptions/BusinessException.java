package com.eva.demo.exceptions;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ReasonType reasonType;

    public BusinessException(ReasonType reasonType) {
        super(reasonType.getMessage());
        this.reasonType = reasonType;
    }

}
