package cn.edu.ruc.Mexplore.domain;

import java.text.DecimalFormat;
import java.util.LinkedHashSet;

public class Feature extends Unit implements Cloneable {
	private Relation relation;
	private Entity target;
	private String model;
	private LinkedHashSet<Entity> entitySet = new LinkedHashSet<>();
	
	public Feature(Relation relation, Entity target) {
		setRelation(relation);
		setTarget(target);
	}
	
	public Feature(Relation relation, Entity target, double weight) {
		setRelation(relation);
		setTarget(target);
		setWeight(weight);
	}
	
	@Override
	public boolean equals(Object object){
		if(!(object instanceof Feature))
			return false;
		if(object == this)
			return true;
		return (relation.getId() == ((Feature) object).getRelation().getId()) && (target.getId() == ((Feature) object).getTarget().getId());
	}

	public int hashCode(){
		return new Integer(relation.getId() + target.getId()).hashCode();
	}
	
	@Override
	public Feature clone(){
		Feature feature = null;
		try {
			feature = (Feature) super.clone();
			feature.setEntitySet(new LinkedHashSet<Entity>());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return feature;
	}
	
	
	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}
	
	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}
	
	public LinkedHashSet<Entity> getEntitySet() {
		return entitySet;
	}

	public void setEntitySet(LinkedHashSet<Entity> entitySet) {
		this.entitySet = entitySet;
	}
	
	public void addEntity(Entity entity){
		entitySet.add(entity);
	}
	
	public void addEntitySet(LinkedHashSet<Entity> entitySet){
		for(Entity entity : entitySet)
			this.entitySet.add(entity);
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	public String toString(){
		String string = "";
		
		DecimalFormat df = new DecimalFormat("0.000" ); 
		
		string += "relation: [" + relation.getName() + " " + df.format(relation.getScore()) + "]";
		string += "; target: [" + target.getName() + " " + df.format(target.getScore()) + "]";
		string += "; score: " + df.format(getScore());
		
		string += "\n";
		for(Entity entity : entitySet)
			string += "[" + entity.getName() + " " + df.format(entity.getScore()) + "]";
		
		return string;
	}
}
