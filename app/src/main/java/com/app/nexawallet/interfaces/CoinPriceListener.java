package com.app.nexawallet.interfaces;

import com.app.nexawallet.models.CoinTicker;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface CoinPriceListener {
    void onResponse(BigDecimal price);
    void onErrorResponse(String msg);
}
