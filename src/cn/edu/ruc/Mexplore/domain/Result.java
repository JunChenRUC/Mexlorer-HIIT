package cn.edu.ruc.Mexplore.domain;

import java.util.ArrayList;

public class Result {
	private ArrayList<Entity> queryEntityList = new ArrayList<>();
	private ArrayList<Feature> queryFeatureList = new ArrayList<>();
	private ArrayList<Entity> resultEntityList = new ArrayList<>();
	private ArrayList<Feature> resultFeatureList = new ArrayList<>();
	
	public Result(ArrayList<Entity> queryEntityList, ArrayList<Feature> queryFeatureList){
		this.queryEntityList = queryEntityList;
		this.queryFeatureList = queryFeatureList;
	}

	public ArrayList<Entity> getQueryEntityList() {
		return queryEntityList;
	}

	public void setQueryEntityList(ArrayList<Entity> queryEntityList) {
		this.queryEntityList = queryEntityList;
	}

	public void addQueryEntity(Entity queryEntity){
		queryEntityList.add(queryEntity);
	}
	
	public ArrayList<Feature> getQueryFeatureList() {
		return queryFeatureList;
	}

	public void setQueryFeatureList(ArrayList<Feature> queryFeatureList) {
		this.queryFeatureList = queryFeatureList;
	}
	
	public void addQueryFeature(Feature queryFeature){
		queryFeatureList.add(queryFeature);
	}

	public ArrayList<Entity> getResultEntityList() {
		return resultEntityList;
	}

	public void setResultEntityList(ArrayList<Entity> resultEntityList) {
		this.resultEntityList = resultEntityList;
	}
	
	public void addResultEntity(Entity resultEntity){
		resultEntityList.add(resultEntity);
	}

	public ArrayList<Feature> getResultFeatureList() {
		return resultFeatureList;
	}

	public void setResultFeatureList(ArrayList<Feature> resultFeatureList) {
		this.resultFeatureList = resultFeatureList;
	}
	
	public void addResultFeature(Feature feature){
		resultFeatureList.add(feature);
	}
}
