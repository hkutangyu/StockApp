package com.zhirui.stock.stockapp;

import org.litepal.crud.DataSupport;

/**
 * Created by super on 2017-01-13.
 */

public class Portfolios extends DataSupport{
    private int id;

    private String stockCode; // 股票代码

    private String username; // 用户名
    public  Portfolios(String stockCode,String username){
        setStockCode(stockCode);
        setUsername(username);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
