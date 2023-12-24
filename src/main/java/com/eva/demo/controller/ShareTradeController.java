package com.eva.demo.controller;

import com.eva.demo.model.BuyShareRequest;
import com.eva.demo.model.ShareSellRequest;
import com.eva.demo.model.ShareSellUpdatePriceRequest;
import com.eva.demo.service.ShareTradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "/api/share-trade-management")
public class ShareTradeController {

    private final ShareTradeService shareTradeService;

    @PostMapping(value = "/sell-share")
    ResponseEntity<String> sellShare(@Validated @RequestBody ShareSellRequest sellRequest) {
        shareTradeService.sellShare(sellRequest);
        return ResponseEntity.ok("SUCCESS");
    }

    @PutMapping(value = "/new-share-price")
    ResponseEntity<String> updatePrice(@Validated @RequestBody ShareSellUpdatePriceRequest updatePriceRequest) {
        shareTradeService.updatePrice(updatePriceRequest);
        return ResponseEntity.ok("SUCCESS");
    }

    @PutMapping(value = "/buy-share")
    ResponseEntity<String> buyShare(@Validated @RequestBody BuyShareRequest buyShareRequest) {
        shareTradeService.buyShare(buyShareRequest);
        return ResponseEntity.ok("SUCCESS");
    }

}
