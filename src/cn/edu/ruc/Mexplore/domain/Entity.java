package cn.edu.ruc.Mexplore.domain;

import java.text.DecimalFormat;
import java.util.LinkedHashSet;

public class Entity extends Unit implements Cloneable {
	private LinkedHashSet<Feature> featureSet= new LinkedHashSet<>();
	
	public Entity(int type, int id){
		setType(type);
		setId(id);
	}
	
	public Entity(int type, int id, String name){
		setType(type);
		setId(id);
		setName(name);
	}
	
	public Entity(int type, int id, double score){
		setType(type);
		setId(id);
		setScore(score);
	}
	
	@Override
	public Entity clone(){
		Entity entity = null;
		try {
			entity = (Entity) super.clone();
			entity.setFeatureSet(new LinkedHashSet<Feature>());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return entity;
	}
	
	public LinkedHashSet<Feature> getFeatureSet() {
		return featureSet;
	}

	public void setFeatureSet(LinkedHashSet<Feature> featureSet) {
		this.featureSet = featureSet;
	}
	
	public void addFeature(Feature feature){
		featureSet.add(feature);
	}
	
	public void addFeatureSet(LinkedHashSet<Feature> featureSet){
		for(Feature feature : featureSet)
			featureSet.add(feature);
	}
	
	public String toString(){
		String string = "";
		
		DecimalFormat df = new DecimalFormat("0.0000" ); 
		string += getName() + "\t" + df.format(getScore());
		
		string += "\n";
		for(Feature feature : featureSet)
			string += "["+ feature.getRelation().getName() + "_" + feature.getTarget().getName() + " " + df.format(feature.getScore()) + "]";
		
		return string;
	}
}
