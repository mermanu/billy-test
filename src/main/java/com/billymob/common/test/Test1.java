package com.billymob.common.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/*
 * Consiste en encontrar dos pares de numeros que sumen estos numeros
 * 231552,234756,596873,648219,726312,981237,988331,1277361,1283379
 */
public class Test1 {

	private int[] sums;

	public void execute(int[] sums) throws NumberFormatException, IOException, InterruptedException, ExecutionException {
		long startTime = System.currentTimeMillis();
		this.sums=sums;
		System.out.println("Filter duplicates and sort input...");
		final List<Integer> numbersToProcess = Files
				.lines(Paths.get(Test.INPUT_PATH_LIST), Charset.defaultCharset())
				.map(n -> Integer.parseInt(n))
				.sorted()
				.distinct()
				.collect(Collectors.toList());
		System.out.println("Final size to process: " + numbersToProcess.size());
		
		List<Set<String>>partialResults = getPairsBySum(numbersToProcess);

		
		System.out.println("Pairs that match all options: " + getFinalResults(partialResults).size());
		long endTime = System.currentTimeMillis();
		System.out.println("Execution: " + (endTime - startTime) + " ms");
	}
	
	/*
	 * Find and puts all the pairs that sum the matched criteria into lists
	 */
	private List<Set<String>> getPairsBySum(List<Integer> numbersToProcess) throws InterruptedException, ExecutionException{
		System.out.println("Pairs per sum value... ");
		ExecutorService executor = Executors.newCachedThreadPool();
		Set<Future<Set<String>>> sets = new HashSet<Future<Set<String>>>();

		System.out.println("Initializing parallel processing to find all pairs...");
		for (int i = 0; i < sums.length; i++) {
			final int sum = sums[i];
			Future<Set<String>> future = executor.submit(() -> {
				final FindPairs func = new FindPairs();
				Set<String> set = func.findPairs(numbersToProcess, sum);
				System.out.println(Thread.currentThread().getName() + " -- sum: " + sum + " -- pairs : " + set.size());
				return set;
			});
			sets.add(future);
		}

		List<Set<String>> partialResults = new ArrayList<>();
		for (Future<Set<String>> future : sets) {
			if (future.get().size() > 0) {
				partialResults.add(future.get());
			}
		}
		executor.shutdown();
		System.out.println("Partial results populated sets: " + partialResults.size() + " of " + sums.length);
		return partialResults;
	}

	/*
	 * In order to get all pairs that match in all the subsets Loop over all
	 * subsets in order to find which values matches the condition
	 */
	private  List<String> getFinalResults(List<Set<String>> partialResults) {
		List<String> results = new ArrayList<>();
		for (String value : partialResults.get(0)) {
			int currentSet = partialResults.size() - 1;
			while (partialResults.get(currentSet).contains(value)) {
				System.out.println("Found " + value + " [" + currentSet + "]");
				if (currentSet > 1) {
					currentSet--;
				} else {
					results.add(value);
					System.out.println("The sum of this pair can give all options " + value);
					break;
				}
			}
		}
		return results;
	}
}
