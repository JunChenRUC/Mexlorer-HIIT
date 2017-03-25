package cn.edu.ruc.Mexplore.utility;

import java.util.ArrayList;

import cn.edu.ruc.Mexplore.domain.Unit;

public class QuickSort<T extends Unit> {
	private ArrayList<T> data; 
	private int length;
	
	public QuickSort(){
		
    } 
	
	public ArrayList<T> getSorted(ArrayList<T> data, int k){  	
		k = (k > data.size() ? data.size() : k);
		
		ArrayList<T> topk = new ArrayList<T>(k);
        		
		for(int i = 0; i < k; i++)
			topk.add(data.get(i));   
          
        initialSort(topk);
           
        for(int i = k; i < data.size();i++){   
            if(data.get(i).getScore() > getMin().getScore())
            	replace(data.get(i)); 
        }  
        
        
        return topk;  
	}
	
	private void initialSort(ArrayList<T> data){
		this.data = data;  
        this.length = data.size();
        buildSort(); 
	}
	
	private void buildSort(){
		while(true) {
	        boolean isEnd = true;
	        for(int i = 0; i < length - 1 ; i++) {
	        	T before = data.get(i);
	        	T behind = data.get(i + 1);
	        	
	        	if(before.getScore() < behind.getScore()) {
	        		data.set(i, behind);
	        		data.set(i + 1, before);
	        		isEnd = false; 
	        		continue;
	        	} else if (i == length - 1) {
	        		isEnd = true;
	        	}
	        }
	        if(isEnd) {
	        	break;
        	}
		}
	}
	
	private T getMin(){
		return data.get(length - 1);
	}
	
	private void replace(T tmp){
		data.set(length - 1, tmp);
		buildSort();
	}
}
