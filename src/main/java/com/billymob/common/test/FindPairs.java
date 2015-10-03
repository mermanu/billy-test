package com.billymob.common.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FindPairs {
    
    private static final String COMMA=",";

    public Set<String> findPairs(List<Integer> inputList, int sum) {
    	Set<String> list=new HashSet<String>();
        Map<Integer,Integer> map=new HashMap<Integer,Integer>();
        
        for(int i=0; i<inputList.size(); i++){
        	map.put(inputList.get(i), inputList.get(i));
        }
        
        for (int i=0; i<inputList.size(); i++) {
            int tempInt = sum - inputList.get(i);
            
            if (map.get(tempInt)!=null) {
            	String pair = "";
            	if(inputList.get(i)<=map.get(tempInt)){
            		 pair = String.valueOf(inputList.get(i) + COMMA + map.get(tempInt)); 
            	}else{
            		 pair = String.valueOf(map.get(tempInt) + COMMA + inputList.get(i)); 
            	}            
                //System.out.println(pair);
                list.add(pair);
            }
        }
        return list;
    }
}