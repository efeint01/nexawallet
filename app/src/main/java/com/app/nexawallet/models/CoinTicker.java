package com.app.nexawallet.models;

import java.math.BigDecimal;

public class CoinTicker {

    String base;
    String target;
    String marketName;
    String marketLogoUrl;
    BigDecimal lastPrice;
    String tradeUrl;

    public CoinTicker() {
    }

    public CoinTicker(String base, String target, String marketName, String marketLogoUrl, BigDecimal lastPrice, String tradeUrl) {
        this.base = base;
        this.target = target;
        this.marketName = marketName;
        this.marketLogoUrl = marketLogoUrl;
        this.lastPrice = lastPrice;
        this.tradeUrl = tradeUrl;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketLogoUrl() {
        return marketLogoUrl;
    }

    public void setMarketLogoUrl(String marketLogoUrl) {
        this.marketLogoUrl = marketLogoUrl;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getTradeUrl() {
        return tradeUrl;
    }

    public void setTradeUrl(String tradeUrl) {
        this.tradeUrl = tradeUrl;
    }
}

