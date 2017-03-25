package cn.edu.ruc.Mexplore.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String inputPath_triple = "D://DBpedia_2014/instance_processed_backup/train.txt";
		
		loadFilmTriple(inputPath_triple);
	}

	private static void loadFilmTriple(String inputPath) throws IOException{
		HashSet<String> subjectSet = new HashSet<>(); 
		HashSet<String> objectSet = new HashSet<>(); 
		HashSet<String> categorySet = new HashSet<>();
		HashSet<String> predicateSet = new HashSet<>();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPath)), "UTF-8"));
        String tmpString = null;
        while ((tmpString = reader.readLine()) != null){
        	String[] tokens = tmpString.toLowerCase().split("\t");
        	
        	if(!tokens[2].equals("subject"))
        		subjectSet.add(tokens[0]);
        	predicateSet.add(tokens[2]);
        	if(tokens[1].startsWith("category:"))
				categorySet.add(tokens[1]);
			else 
				objectSet.add(tokens[1]);
        }
        reader.close();
        
        System.out.println("movie size: " + subjectSet.size() + "\tpredicate size: " + predicateSet.size() + "\tobject size: " + objectSet.size() + "\tcategory: " + categorySet.size());
	}
}
