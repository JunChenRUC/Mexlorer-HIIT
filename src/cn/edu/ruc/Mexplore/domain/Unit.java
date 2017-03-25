package cn.edu.ruc.Mexplore.domain;

public class Unit {
	private int type;
	private int id;
	private String name;
	private double weight;
	private double score;
	
	@Override
	public boolean equals(Object object){
		if(!(object instanceof Unit))
			return false;
		if(object == this)
			return true;
		return (id == ((Unit) object).getId());
	}
	
	public int hashCode(){
		return new Integer(id).hashCode();
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
}
