package com.eva.demo.model;

import com.eva.demo.exceptions.ReasonType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessErrorResponse {
    ReasonType reasonType;
    String message;
}
