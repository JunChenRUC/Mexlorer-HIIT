package cn.edu.ruc.Mexplore.utility;

import java.util.ArrayList;

import cn.edu.ruc.Mexplore.data.DataManager;
import cn.edu.ruc.Mexplore.domain.Entity;
import cn.edu.ruc.Mexplore.domain.Feature;

public class QueryParser {
	private DataManager dataManager;
	
	public QueryParser(DataManager dataManager){
		this.dataManager = dataManager;
	}
	
	public ArrayList<Entity> parseQueryEntity(ArrayList<String> queryString){
		ArrayList<Entity> queryEntityList = new ArrayList<>();
		
		for(String queryEntityString : queryString){
			if(queryEntityString.contains("_"))
				continue;
			double weight = 1;
			if(queryEntityString.contains("##")){
				weight = Double.parseDouble(queryEntityString.split("##")[0]);
				queryEntityString = queryEntityString.split("##")[1];
			}
			Entity queryEntity = dataManager.getEncodedEntity(queryEntityString);
			if(queryEntity != null){
				queryEntity.setWeight(weight);
				queryEntityList.add(queryEntity);
			}
			else
				System.out.println("error: " + queryEntityString);
		}
		
		return queryEntityList;
	}
	
	public ArrayList<Feature> parseQueryFeature(ArrayList<String> queryString){
		ArrayList<Feature> queryFeatureList = new ArrayList<>();
		
		for(String queryFeatureString : queryString){
			if(!queryFeatureString.contains("_"))
				continue;
			double weight = 1;
			if(queryFeatureString.contains("##")){
				weight = Double.parseDouble(queryFeatureString.split("##")[0]);;
				queryFeatureString = queryFeatureString.split("##")[1];
			}
			Feature queryFeature = dataManager.getEncodedFeature(queryFeatureString);
			if(queryFeature != null){
				queryFeature.setWeight(weight);
				queryFeatureList.add(queryFeature);
			}
			else
				System.out.println("error: " + queryFeatureString);
		}
		
		return queryFeatureList;
	}
}
