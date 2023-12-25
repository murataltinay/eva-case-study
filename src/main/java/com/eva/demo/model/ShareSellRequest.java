package com.eva.demo.model;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ShareSellRequest {

    @NotNull
    private Long userID;

    @Size(min = 3, max = 3)
    @Pattern(regexp = "([A-Z]{3})", message = "Only Big Letter")
    private String shareName;

    @NotNull
    private Integer quantity;

    @Digits(integer = 20, fraction = 2)
    private BigDecimal oneLotPrice;

}
