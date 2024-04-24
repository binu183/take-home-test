package com.binu.test.config;

import java.util.Map;

public class StandardSymbolProbability {
	private Integer row;
	private Integer column;
	private Map<String,Double> symbols;

	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public Integer getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	public Map<String, Double> getSymbols() {
		return symbols;
	}

	public void setSymbols(Map<String, Double> symbols) {
		this.symbols = symbols;
	}
}
