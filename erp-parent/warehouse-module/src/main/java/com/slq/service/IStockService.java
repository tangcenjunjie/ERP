package com.slq.service;

import java.util.List;

import com.slq.pojo.warehouse.Stock;

public interface IStockService {

	List<Stock> getStocks();

	Stock getStockById(String stockId);

	int addStock(Stock stock);

}
