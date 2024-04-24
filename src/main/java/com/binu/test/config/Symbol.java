package com.binu.test.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Symbol {
	@JsonProperty("reward_multiplier")
	private double rewardMultiplier;
	private Type type;
	private Impact impact;
	private double extra;

	public double getRewardMultiplier() {
		return rewardMultiplier;
	}

	public void setRewardMultiplier(double rewardMultiplier) {
		this.rewardMultiplier = rewardMultiplier;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Impact getImpact() {
		return impact;
	}

	public void setImpact(Impact impact) {
		this.impact = impact;
	}

	public double getExtra() {
		return extra;
	}

	public void setExtra(double extra) {
		this.extra = extra;
	}
}
