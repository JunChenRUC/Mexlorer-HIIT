package cn.edu.ruc.Mexplore.index;



public class Test {
	public static void main(String[] args) throws Exception {			
		IndexManager indexManager = new IndexManager();
	
		for(String result : indexManager.getResult("forrest gump"))
			System.out.println(result);
	}
}
