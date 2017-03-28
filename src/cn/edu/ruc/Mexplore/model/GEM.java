package cn.edu.ruc.Mexplore.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import cn.edu.ruc.Mexplore.data.DataManager;
import cn.edu.ruc.Mexplore.domain.Entity;
import cn.edu.ruc.Mexplore.domain.Feature;
import cn.edu.ruc.Mexplore.domain.FeatureKey;
import cn.edu.ruc.Mexplore.domain.Relation;
import cn.edu.ruc.Mexplore.domain.Result;
import cn.edu.ruc.Mexplore.utility.QuickSort;

public class GEM {
	private DataManager dataManager;
	
	public GEM(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	
	public Result getResult(ArrayList<Entity> queryEntityList, ArrayList<Feature> queryFeatureList){			
		Result result = new Result(queryEntityList, queryFeatureList);
		
		result.setResultEntityList(getEntity(result.getQueryEntityList(), result.getQueryFeatureList()));
		
		result.setResultFeatureList(getFeature(result.getResultEntityList()));
			
		dataManager.decodeResult(result);
		
		return result;
	}
	
	public ArrayList<Entity> getEntity(ArrayList<Entity> queryEntityList, ArrayList<Feature> queryFeatureList){
		HashMap<Integer, Entity> entityMap = new HashMap<>();
		
		for(Entity queryEntity : queryEntityList){
			for(Entity resultEntity : getEntity2Entity(queryEntity)){
				if(!entityMap.containsKey(resultEntity.getId()))
					entityMap.put(resultEntity.getId(), resultEntity);
				else
					entityMap.get(resultEntity.getId()).setScore(entityMap.get(resultEntity.getId()).getScore() + resultEntity.getScore());
			}
		}
		for(Feature queryFeature : queryFeatureList){
			for(Entity resultEntity : getFeature2Entity(queryFeature)){
				if(!entityMap.containsKey(resultEntity.getId()))
					entityMap.put(resultEntity.getId(), resultEntity);
				else
					entityMap.get(resultEntity.getId()).setScore(entityMap.get(resultEntity.getId()).getScore() + resultEntity.getScore());
			}
		}
		
		return filter(queryEntityList, queryFeatureList, entityMap);
	}
	
	private ArrayList<Entity> filter(ArrayList<Entity> queryEntityList, ArrayList<Feature> queryFeatureList, HashMap<Integer, Entity> entityMap){
		for(Entity queryEntity : queryEntityList)		
			entityMap.remove(queryEntity.getId());
		for(Feature queryFeature : queryFeatureList)		
			entityMap.remove(queryFeature.getTarget().getId());
		
		return new QuickSort<Entity>().getSorted(new ArrayList<Entity>(entityMap.values()), DataManager.output_size_entity);	
	}
	
	private ArrayList<Entity> getEntity2Entity(Entity queryEntity){
		ArrayList<Entity> resultEntityList = new ArrayList<>();
		
		double[] queryEntityVector = dataManager.getVector(queryEntity.getId(), queryEntity.getType());		
		for(int resultEntityId : dataManager.getEntity2Vector().keySet()){
			Entity resultEntity = new Entity(DataManager.entity_type, resultEntityId);		
			double[] resultEntityVector = dataManager.getVector(resultEntity.getId(), DataManager.entity_type);			
			double score =  queryEntity.getWeight() * getScore(queryEntityVector, resultEntityVector);
			
			resultEntity.setScore(score);			
			resultEntityList.add(resultEntity);
		}	
		
		return resultEntityList;
	}
	
	private ArrayList<Entity> getFeature2Entity(Feature queryFeature){
		ArrayList<Entity> resultEntityList = new ArrayList<>();
		
		double[] queryEntityVector = dataManager.getVector(queryFeature.getTarget().getId(), queryFeature.getTarget().getType());
		double[] queryRelationVector = dataManager.getVector(queryFeature.getRelation().getId(), queryFeature.getRelation().getType());		
		for(int resultEntityId : dataManager.getEntity2Vector().keySet()){
			Entity resultEntity = new Entity(DataManager.entity_type, resultEntityId);			
			double[] resultEntityVector = dataManager.getVector(resultEntity.getId(), DataManager.entity_type);			
			double score = queryFeature.getWeight() * getScore(queryEntityVector, resultEntityVector, queryRelationVector, queryFeature.getRelation().getDirection());
			
			resultEntity.setScore(score);		
			resultEntityList.add(resultEntity);
		}
		
		return resultEntityList;
	}
	
	public ArrayList<Feature> getFeature(ArrayList<Entity> resultEntityList){
		HashMap<FeatureKey, Feature> featureMap = new HashMap<>();
		
		ArrayList<Relation> relationList = getRelation(resultEntityList);	
		for(Entity resultEntity : resultEntityList){
			for(Relation relation : relationList){
				for(Feature feature : getFeature(resultEntity, relation)){
					resultEntity.addFeature(feature.clone());
					
					FeatureKey featureKey = new FeatureKey(feature.getRelation().getId(), feature.getTarget().getId());
					if(!featureMap.containsKey(featureKey))
						featureMap.put(featureKey, feature);
					else
						featureMap.get(featureKey).setScore(featureMap.get(featureKey).getScore() + feature.getScore());
					
					featureMap.get(featureKey).addEntity(resultEntity.clone());
				}
			}
		}
		
		Iterator<Entry<FeatureKey, Feature>> iterator = featureMap.entrySet().iterator();  
        while(iterator.hasNext()){  
        	Entry<FeatureKey, Feature> featureEntry = iterator.next();  
            if(featureEntry.getValue().getEntitySet().size() < 2)
                iterator.remove();    
        }  
		
		return new QuickSort<Feature>().getSorted(new ArrayList<Feature>(featureMap.values()), DataManager.output_size_feature);
	}
	
	public ArrayList<Relation> getRelation(Entity resultEntity){	
		HashMap<Integer, Relation> relationMap = new HashMap<>();
		
		int triple_size_max = 1, triple_size_min = Integer.MAX_VALUE;		
		for(int i = 0; i < 2; i ++){
			if(dataManager.getTripleHash().get(i).containsKey(resultEntity.getId())){
				for(HashSet<Integer> targetSet : dataManager.getTripleHash().get(i).get(resultEntity.getId()).values())	{
					if(targetSet .size() > triple_size_max)
						triple_size_max = targetSet .size();
					if(targetSet .size() < triple_size_min)
						triple_size_min = targetSet .size();	
				}
			}
		}
		
		for(int i = 0; i < 2; i ++){
			if(dataManager.getTripleHash().get(i).containsKey(resultEntity.getId())){
				for(Entry<Integer, HashSet<Integer>> relation2entityEntry : dataManager.getTripleHash().get(i).get(resultEntity.getId()).entrySet()){
					Relation relation = new Relation(DataManager.relation_type, relation2entityEntry.getKey(), i);
					
					double score = (double)relation2entityEntry.getValue().size() / (triple_size_min + triple_size_max);;
					score = resultEntity.getScore() * (- score * Math.log(score));
					relation.setScore(score);

					if(!relationMap.containsKey(relation.getId()))
						relationMap.put(relation.getId(), relation);
					else
						relationMap.get(relation.getId()).setScore(relationMap.get(relation.getId()).getScore() + relation.getScore());
				}
			}
		}

		return new QuickSort<Relation>().getSorted(new ArrayList<Relation>(relationMap.values()), relationMap.size());
	}
	
	public ArrayList<Relation> getRelation(ArrayList<Entity> resultEntityList){	
		HashSet<Relation> relationSet = new HashSet<>();
		
		HashMap<Integer, ArrayList<Integer>> relation2size_source = new HashMap<>();		
		HashMap<Integer, ArrayList<Integer>> relation2size_target = new HashMap<>();
		for(Entity resultEntity : resultEntityList){
			for(int i = 0; i < 2; i ++){
				if(dataManager.getTripleHash().get(i).containsKey(resultEntity.getId())){
					for(Entry<Integer, HashSet<Integer>> relation2entityEntry : dataManager.getTripleHash().get(i).get(resultEntity.getId()).entrySet()){
						Relation relation = new Relation(DataManager.relation_type, relation2entityEntry.getKey(), i);
						
						relationSet.add(relation);
												
						if(!relation2size_source.containsKey(relation.getId()))
							relation2size_source.put(relation.getId(), new ArrayList<Integer>());
						relation2size_source.get(relation.getId()).add(relation2entityEntry.getValue().size());	
						
						if(!relation2size_target.containsKey(relation.getId()))
							relation2size_target.put(relation.getId(), new ArrayList<Integer>());
						for(int target : relation2entityEntry.getValue())
							relation2size_target.get(relation.getId()).add(dataManager.getTripleHash().get((i + 1)%2).get(target).get(relation.getId()).size());
					}
				}
			}
		}
		
		for(Relation relation : relationSet){
			double top = 0, bottom = 0;
			for(int size : relation2size_source.get(relation.getId()))
				top += size;
			for(int size : relation2size_target.get(relation.getId()))
				bottom += size;
			
			double score = (top / relation2size_source.get(relation.getId()).size()) / (bottom / relation2size_target.get(relation.getId()).size());
			
			relation.setScore(score);
		}

		return new QuickSort<Relation>().getSorted(new ArrayList<Relation>(relationSet), relationSet.size());
	}
	
	public ArrayList<Feature> getFeature(Entity resultEntity, Relation relation){
		HashMap<FeatureKey, Feature> featureMap = new HashMap<>();
		
		if(dataManager.getTripleHash().get(relation.getDirection()).containsKey(resultEntity.getId())){
			if(dataManager.getTripleHash().get(relation.getDirection()).get(resultEntity.getId()).containsKey(relation.getId())){
				for(int targetId : dataManager.getTripleHash().get(relation.getDirection()).get(resultEntity.getId()).get(relation.getId())){
					Entity target = dataManager.getEncodedEntity(targetId);
					target.setScore(getSimilarity(resultEntity, relation, target));
					
					Feature feature = new Feature(relation, target);
					feature.setScore(resultEntity.getScore() * relation.getScore() * target.getScore());					
					
					FeatureKey featureKey = new FeatureKey(feature.getRelation().getId(), feature.getTarget().getId());
					if(!featureMap.containsKey(featureKey))
						featureMap.put(featureKey, feature);
					else
						featureMap.get(featureKey).setScore(featureMap.get(featureKey).getScore() + feature.getScore());												
				}
			}
		}
		
		return new QuickSort<Feature>().getSorted(new ArrayList<Feature>(featureMap.values()), featureMap.size());
	}
	
	public double getSimilarity(Entity entity1, Relation relation, Entity entity2){			
		double[] eVector1 = dataManager.getVector(entity1.getId(), entity1.getType());
		double[] eVector2 = dataManager.getVector(entity2.getId(), entity2.getType());
		double[] rVector = dataManager.getVector(relation.getId(), relation.getType());
		
		double score = getScore(eVector1, eVector2, rVector, relation.getDirection());
		
		return score;
	}
		
	private double[] getHead(double[] eVector_tail, double[] rVector){
		double[] eVector_head = new double[DataManager.D_relation];
		for(int i = 0; i < DataManager.D_relation; i ++)
			eVector_head[i] = eVector_tail[i] - rVector[i];
		return eVector_head;
	}
	
	private double[] getTail(double[] eVector_head, double[] rVector){
		double[] vector_tail = new double[DataManager.D_relation];
		for(int i = 0; i < DataManager.D_relation; i ++)
			vector_tail[i] = eVector_head[i] + rVector[i];
		return vector_tail;
	}
	
	private double getScore(double[] eVector1, double[] eVector2){
		double score = 0;
		int dimension = eVector1.length;
		for(int i = 0; i < dimension; i ++)
			score += Math.pow(eVector1[i] - eVector2[i], 2);
		return (3 - Math.sqrt(score)) / 3;
	}

	private double getScore(double[] eVector1, double[] eVector2, double[] rVector, int direction){
		double score = 0;
		if(direction == 0)
			score = getScore(getTail(eVector1, rVector), eVector2);
		else
			score = getScore(getHead(eVector1, rVector), eVector2);
		return score;
	}
}
