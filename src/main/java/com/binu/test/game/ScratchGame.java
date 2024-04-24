package com.binu.test.game;

import com.binu.test.config.Config;
import com.binu.test.config.Probability;
import com.binu.test.config.Symbol;
import com.binu.test.config.Type;
import com.binu.test.config.WhenCriteria;
import com.binu.test.config.WinCombination;
import com.binu.test.config.WinCombinationFrequency;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class ScratchGame {
	public static void main(String args[]) throws IOException {
		if (args.length < 4) {
			System.out.println("Run using following pattern java -jar <your-jar-file> --config config.json " +
							   "--betting-amount 100");
			return;
		}
		String configFile = args[1];
		double bettingAmount = Double.parseDouble(args[3]);
		ObjectMapper mapper = configureObjectMapper();
		Config config = parseConfigFile(mapper,
										configFile);
		Map<String, Integer> symbolCountInMatrixMap = new HashMap<>();
		Response response = new Response();
		String[][] matrix = generateMatrix(config,
										   symbolCountInMatrixMap,
										   response);
		Map<Integer, WinCombinationFrequency> sameSymbolWinCountMap = new HashMap<>();
		config
				.getWinCombinations()
				.forEach((key, value) -> {
					if (value.getWhen() == WhenCriteria.same_symbols) {
						sameSymbolWinCountMap.put(value.getCount(),
												  key);
					}
				});
		Map<String, WinCombination> winCombinations = new HashMap<>();
		Map<String, List<String>> appliedWinCombinationsMap = new HashMap<>();
		List<String> winSymbols = new ArrayList<>();
		checkForWinCombinations(symbolCountInMatrixMap,
								sameSymbolWinCountMap,
								config,
								winCombinations,
								winSymbols,
								appliedWinCombinationsMap);
		buildResponse(matrix,
										  bettingAmount,
										  winSymbols,
										  config,
										  winCombinations,
										  appliedWinCombinationsMap,
										  response);
		System.out.println(mapper.writeValueAsString(response));
	}

	private static Response buildResponse(
			String[][] matrix,
			double bettingAmount,
			List<String> winSymbols,
			Config config,
			Map<String, WinCombination> winCombinations,
			Map<String, List<String>> appliedWinCombinationsMap,
			Response response
	) {
		response.setMatrix(matrix);
		response.setBetAmount(bettingAmount);
		if (!winSymbols.isEmpty()) {
			double reward = bettingAmount;
			boolean initialIteration = true;
			for (String winSymbolName : winSymbols) {
				Symbol winSymbol = config
						.getSymbols()
						.get(winSymbolName);
				WinCombination winCombination = winCombinations.get(winSymbolName);
				double winRewards = bettingAmount;
				winRewards = applySymbolReward(winSymbolName,
											   winSymbol,
											   winRewards);
				winRewards = winRewards * winCombination.getRewardMultiplier();
				Symbol bonusSymbol = config.getSymbols()
										   .get(response.getAppliedBonusSymbol());
				winRewards = applySymbolReward(winSymbolName,
											   bonusSymbol,
											   winRewards);
				if (initialIteration) {
					reward = winRewards;
					initialIteration = false;
				} else {
					reward += winRewards;
				}

			}
			response.setAppliedWiningCombination(appliedWinCombinationsMap);
			response.setReward(reward);
		} else {
			response.setAppliedBonusSymbol(null);
			response.setReward(0);
		}
		return response;
	}

	private static double applySymbolReward(
			String winSymbolName,
			Symbol winSymbol,
			double winRewards
	) {
		switch (winSymbol.getType()) {
			case standard:
				winRewards = winRewards * winSymbol.getRewardMultiplier();
				break;
			case bonus:
				switch (winSymbol.getImpact()) {
					case extra_bonus:
						winRewards = winRewards + winSymbol.getExtra();
						break;
					case multiply_reward:
						winRewards = winRewards * winSymbol.getRewardMultiplier();
						break;
				}
				break;
		}
		return winRewards;
	}

	private static void checkForWinCombinations(
			Map<String, Integer> symbolCountInMatrixMap,
			Map<Integer, WinCombinationFrequency> sameSymbolWinCountMap,
			Config config,
			Map<String, WinCombination> winCombinations,
			List<String> winSymbols,
			Map<String, List<String>> appliedWinCombinationsMap
	) {
		for (Map.Entry<String, Integer> entry : symbolCountInMatrixMap.entrySet()) {
			if (sameSymbolWinCountMap.get(entry.getValue()) != null) {
				WinCombination winCombination = config
						.getWinCombinations()
						.get(sameSymbolWinCountMap.get(entry.getValue()));
				winCombinations.put(entry.getKey(),
									winCombination);
				winSymbols.add(entry.getKey());
				List<String> appliedWinningCombination;
				if (appliedWinCombinationsMap.get(entry.getKey()) == null) {
					appliedWinningCombination = new ArrayList<>();
				} else {
					appliedWinningCombination = appliedWinCombinationsMap.get(entry.getKey());
				}
				appliedWinningCombination.add(sameSymbolWinCountMap
													  .get(entry.getValue())
													  .name());
				appliedWinCombinationsMap.put(entry.getKey(),
											  appliedWinningCombination);
			}
		}
	}

	private static Config parseConfigFile(
			ObjectMapper mapper,
			String configFile
	) throws IOException {
		try {
			return mapper.readValue(new File(configFile),
									Config.class);
		} catch (IOException exp) {
			exp.printStackTrace();
			throw exp;
		}
	}

	private static ObjectMapper configureObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
						 false);
		mapper.configure(JsonParser.Feature.ALLOW_COMMENTS,
						 true);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		return mapper;
	}

	private static String[][] generateMatrix(
			Config config,
			Map<String, Integer> symbolCountMap,
			Response response
	) {
		int rows = Optional
				.ofNullable(config.getRows())
				.orElse(3);
		int columns = Optional
				.ofNullable(config.getColumns())
				.orElse(3);
		String[][] matrix = new String[rows][columns];
		List<String> symbols = new ArrayList<>();
		config
				.getSymbols()
				.forEach((key, value) -> {
					if (value.getType() == Type.standard) {
						symbols.add(key);
					}
				});
		fillWithProbabilitiesFromConfig(matrix,
										config.getProbabilities(),
										response);
		fillWithRandomValues(symbolCountMap,
							 rows,
							 columns,
							 symbols,
							 matrix);
		return matrix;
	}

	private static void fillWithRandomValues(
			Map<String, Integer> symbolCountMap,
			int rows,
			int columns,
			List<String> symbols,
			String[][] matrix
	) {
		String symbol;
		Random random = new Random();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				int randomSymbolPosition = random.nextInt(symbols.size());
				symbol = symbols.get(randomSymbolPosition);
				if (matrix[i][j] == null) {
					matrix[i][j] = symbol;
				} else {
					symbol = matrix[i][j];
				}
				if (symbolCountMap.get(symbol) == null) {
					symbolCountMap.put(symbol,
									   1);
				} else {
					symbolCountMap.put(symbol,
									   symbolCountMap.get(symbol) + 1);
				}
			}
		}
	}

	private static void fillWithProbabilitiesFromConfig(
			String[][] matrix,
			Probability probabilities,
			Response response
	) {
		probabilities
				.getStandardSymbolProbabilities()
				.forEach(obj -> {
					String symbol = getSymbolWithHighestProbability(obj.getSymbols());
					matrix[obj.getRow()][obj.getColumn()] = symbol;
				});
		String bonusSymbol = getSymbolWithHighestProbability(probabilities
															  .getBonusSymbolProbability()
															  .getSymbols());
		response.setAppliedBonusSymbol(bonusSymbol);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == null) {
					matrix[i][j] = bonusSymbol;
					return;
				}
			}
		}

	}

	private static String getSymbolWithHighestProbability(Map<String, Double> symbols) {
		Map<String, Double> probabilityMap = new HashMap<>();
		double sumOfAllProbabilities = symbols
				.values()
				.stream()
				.mapToDouble(Double::doubleValue)
				.sum();
		for (Map.Entry<String, Double> entry : symbols.entrySet()) {
			probabilityMap.put(entry.getKey(),
							   sumOfAllProbabilities / entry.getValue());
		}
		return probabilityMap
				.entrySet()
				.stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey)
				.get();
	}
}
