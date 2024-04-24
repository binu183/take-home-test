package com.binu.test.game;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Response {
	private double betAmount;
	private String [] [] matrix;
	private double reward;
	@JsonProperty("applied_bonus_symbol")
	private String appliedBonusSymbol;
	@JsonProperty("applied_winning_combinations")
	private Map<String, List<String>> appliedWiningCombination;

	public double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(double betAmount) {
		this.betAmount = betAmount;
	}

	public String[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(String[][] matrix) {
		this.matrix = matrix;
	}

	public double getReward() {
		return reward;
	}

	public void setReward(double reward) {
		this.reward = reward;
	}

	public String getAppliedBonusSymbol() {
		return appliedBonusSymbol;
	}

	public void setAppliedBonusSymbol(String appliedBonusSymbol) {
		this.appliedBonusSymbol = appliedBonusSymbol;
	}

	public Map<String, List<String>> getAppliedWiningCombination() {
		return appliedWiningCombination;
	}

	public void setAppliedWiningCombination(Map<String, List<String>> appliedWiningCombination) {
		this.appliedWiningCombination = appliedWiningCombination;
	}
}
