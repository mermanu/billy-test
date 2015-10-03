package com.billymob.common.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test2 {

	public void execute() throws IOException, InterruptedException, ExecutionException{
		long startTime = System.currentTimeMillis();
		List<Integer> numbers=Files.lines(Paths.get(Test.INPUT_PATH_RANGE_LARGE), Charset.defaultCharset())
				.flatMap(line-> Arrays.stream(line.split(" ")))
				.mapToInt(n -> Integer.parseInt(n))
				.boxed()
				.collect(Collectors.toList());
		
		ExecutorService executor = Executors.newCachedThreadPool();
		Set<Future<List<Integer>>> sets = new HashSet<Future<List<Integer>>>();
		
		//calculate all numbers into ranges
		getRangesFronList(numbers)
		.forEach(n-> getNumbersInRange(n, executor, sets));		
		
		List<List<Integer>> partialResults = new ArrayList<>();
		for (Future<List<Integer>> future : sets) {
			if (future.get().size() > 0) {
				partialResults.add(future.get());
			}
		}
		
		System.out.println("Total ranges:"+partialResults.size());
		executor.shutdown();
		
		int size=partialResults.stream().map(n-> n.size()).reduce(0, (a, b) -> a + b);
		
		List<Integer> results=new ArrayList<>(size);
		partialResults.forEach(n->results.addAll(n));
		
		
		System.out.println("Numbers in all ranges:"+results.stream().sorted().distinct().count());
		long endTime = System.currentTimeMillis();
		System.out.println("Execution: " + (endTime - startTime) + " ms");
	}
	
	/*
	 * retrieve all numbers inside a range
	 */
	private void getNumbersInRange(Test2.Range range, ExecutorService executor, Set<Future<List<Integer>>> sets ){
		Future<List<Integer>> future = executor.submit(() -> {	
			List<Integer> list=IntStream.rangeClosed(range.getA(), range.getB()).boxed().collect(Collectors.toList());
			//System.out.println(Thread.currentThread().getName() + " -- range: " + range.getA()+"-"+range.getB() +" -- numbers : " + list.size());
			return list;
		});
		sets.add(future);
	}
	
	/*
	 * set ranges in list
	 */
	private List<Range> getRangesFronList(List<Integer> numbers){
		List<Range> ranges=new ArrayList<>();
		for(int i=0;i<numbers.size();i++){			
			ranges.add(new Range(numbers.get(i), numbers.get(i+1)));			
			i++;
		}
		System.out.println("Ranges recovered");
		return ranges;
	}
	
	private class Range {
		int a;
		int b;	
		public Range(int a, int b) {
			super();
			this.a = a;
			this.b = b;
		}
		public int getA() {
			return a;
		}
		public int getB() {
			return b;
		}	
	}
}
