package cn.edu.ruc.Mexplore.domain;

public class Relation extends Unit{
	private int direction;
	
	public Relation(){
		
	}
	
	public Relation(int type, int id){
		setType(type);
		setId(id);
	}
	
	public Relation(int type, int id, String name){
		setType(type);
		setId(id);
		setName(name);
	}
	
	public Relation(int type, int id, String name, int direction){
		setType(type);
		setId(id);
		setName(name);
		setDirection(direction);
	}
	
	public Relation(int type, int id, int direction){
		setType(type);
		setId(id);
		setDirection(direction);
	}
	
	public Relation(int type, int id, int direction, double score){
		setType(type);
		setId(id);
		setDirection(direction);
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}
