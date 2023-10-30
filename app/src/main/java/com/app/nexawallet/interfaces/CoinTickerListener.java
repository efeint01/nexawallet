package com.app.nexawallet.interfaces;

import com.app.nexawallet.models.CoinTicker;

import java.util.ArrayList;

public interface CoinTickerListener {
    void onResponse(ArrayList<CoinTicker> arrayList);
    void onErrorResponse(String msg);
}
