package com.zhirui.stock.stockapp;

/**
 * Created by super on 2017-01-12.
 */

public class StockInfo {
    private String stockName;
    private String stockCode;
    private double currentPrice;
    private double currentPercent;
    private double buyPrice;
    private double buyPercent;
    private double sellPrice;
    private double sellPercent;

    public StockInfo(String stockName,String stockCode,double currentPercent,double currentPrice,
                     double buyPercent,double buyPrice, double sellPercent, double sellPrice){
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.currentPercent = currentPercent;
        this.currentPrice = currentPrice;
        this.buyPercent = buyPercent;
        this.buyPrice = buyPrice;
        this.sellPercent = sellPercent;
        this.sellPrice = sellPrice;
    }
    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getCurrentPercent() {
        return currentPercent;
    }

    public void setCurrentPercent(double currentPercent) {
        this.currentPercent = currentPercent;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getBuyPercent() {
        return buyPercent;
    }

    public void setBuyPercent(double buyPercent) {
        this.buyPercent = buyPercent;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getSellPercent() {
        return sellPercent;
    }

    public void setSellPercent(double sellPercent) {
        this.sellPercent = sellPercent;
    }
}
