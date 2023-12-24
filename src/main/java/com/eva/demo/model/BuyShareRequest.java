package com.eva.demo.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BuyShareRequest {

    @NotNull
    private Long userID;

    @Size(min = 3, max = 3)
    @Pattern(regexp = "([A-Z]{3})", message = "Only Big Letter")
    private String shareName;

    @NotNull
    private Integer quantity;
}
