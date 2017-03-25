package cn.edu.ruc.Mexplore.model;

import java.util.ArrayList;

import cn.edu.ruc.Mexplore.data.DataManager;
import cn.edu.ruc.Mexplore.domain.Entity;
import cn.edu.ruc.Mexplore.domain.Feature;
import cn.edu.ruc.Mexplore.domain.Result;
import cn.edu.ruc.Mexplore.utility.QueryParser;
import net.sf.json.JSONArray;

public class Test {
	private static GEM gem;
	private static QueryParser queryParser;
	
	public static void main(String[] args) {
		initial();
		
		getResult();
	}
	
	public static void initial(){
		DataManager dataManager = new DataManager();
		gem = new GEM(dataManager);
		queryParser = new QueryParser(dataManager);
	}
	
	public static void getResult(){
		long beginTime = System.currentTimeMillis();
		
		ArrayList<String> queryStringList = new ArrayList<>();
		queryStringList.add("forrest gump");
		queryStringList.add("category:films whose director won the best director academy award_subject_1");
		
		Result result = null;
		result = gem.getResult(queryParser.parseQueryEntity(queryStringList), queryParser.parseQueryFeature(queryStringList));
		
		System.out.println("Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
		
		System.out.println("\n--------------------------\n");
		System.out.println("Query entity list: ");
		for(Entity queryEntity : result.getQueryEntityList())
			System.out.println(queryEntity);
		
		System.out.println("\n--------------------------\n");
		System.out.println("Query feature list: ");
		for (Feature queryFeature : result.getQueryFeatureList())
			System.out.println(queryFeature);

		System.out.println("\n--------------------------\n");
		System.out.println("Result entity list: ");
		for (Entity resultEntity : result.getResultEntityList())
			System.out.println(resultEntity);
		
		System.out.println("\n--------------------------\n");
		System.out.println("Result feature list: ");
		for(Feature resultFeature : result.getResultFeatureList())
			System.out.println(resultFeature);
		
		JSONArray jsonArray = JSONArray.fromObject(result);
		System.out.println(jsonArray.toString());
	}
}
