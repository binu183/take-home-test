package com.binu.test.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
	private Integer columns;
	private Integer rows;

	private Map<String, Symbol> symbols;

	private Probability probabilities;
	@JsonProperty("win_combinations")
	private Map<WinCombinationFrequency, WinCombination> winCombinations;

	public Integer getColumns() {
		return columns;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Map<String, Symbol> getSymbols() {
		return symbols;
	}

	public void setSymbols(Map<String, Symbol> symbols) {
		this.symbols = symbols;
	}

	public Map<WinCombinationFrequency, WinCombination> getWinCombinations() {
		return winCombinations;
	}

	public void setWinCombinations(Map<WinCombinationFrequency, WinCombination> winCombinations) {
		this.winCombinations = winCombinations;
	}

	public Probability getProbabilities() {
		return probabilities;
	}

	public void setProbabilities(Probability probabilities) {
		this.probabilities = probabilities;
	}
}
