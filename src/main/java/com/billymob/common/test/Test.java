package com.billymob.common.test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Test {
	public static final String INPUT_PATH_LIST="./input/int_list.txt";
	public static final String INPUT_PATH_SEARCH="./input/list_search.txt";
	public static final String INPUT_PATH_RANGE_SMALL="./input/range_small.txt";
	public static final String INPUT_PATH_RANGE_LARGE="./input/range_large.txt";
	
	
	public static final int[] sums = { 231552, 234756, 596873, 648219, 726312, 981237, 988331, 1277361, 1283379 };
	
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		
		System.out.println("-------------------------Test 1-------------------------");
		Test1 test1=new Test1();
		test1.execute(sums);
		
		System.out.println("-------------------------Test 2-------------------------");
		Test2 test2=new Test2();
		test2.execute();
	}
	
	

}
