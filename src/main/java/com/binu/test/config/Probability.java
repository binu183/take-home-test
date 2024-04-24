package com.binu.test.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Probability {
	@JsonProperty("standard_symbols")
	private List<StandardSymbolProbability> standardSymbolProbabilities;
	@JsonProperty("bonus_symbols")
	private BonusSymbolProbability bonusSymbolProbability;

	public List<StandardSymbolProbability> getStandardSymbolProbabilities() {
		return standardSymbolProbabilities;
	}

	public void setStandardSymbolProbabilities(List<StandardSymbolProbability> standardSymbolProbabilities) {
		this.standardSymbolProbabilities = standardSymbolProbabilities;
	}

	public BonusSymbolProbability getBonusSymbolProbability() {
		return bonusSymbolProbability;
	}

	public void setBonusSymbolProbability(BonusSymbolProbability bonusSymbolProbability) {
		this.bonusSymbolProbability = bonusSymbolProbability;
	}
}
