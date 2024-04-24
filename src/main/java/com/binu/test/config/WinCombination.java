package com.binu.test.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WinCombination {
	private WhenCriteria when;
	private int count;
	private String group;
	@JsonProperty("reward_multiplier")
	private double rewardMultiplier;

	public WhenCriteria getWhen() {
		return when;
	}

	public void setWhen(WhenCriteria when) {
		this.when = when;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public double getRewardMultiplier() {
		return rewardMultiplier;
	}

	public void setRewardMultiplier(double rewardMultiplier) {
		this.rewardMultiplier = rewardMultiplier;
	}
}
