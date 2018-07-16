package pharmacycounting;

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

public class TopCostDrug {
	private String fullName, drugName;
	private double drugCost;
	ArrayList<OutputValues> outputValuesList = new ArrayList<OutputValues>();
	
	public static void main(String[] args){
		TopCostDrug topCostDrug = new TopCostDrug();
		topCostDrug.readFile(args[0]);
		topCostDrug.writeOutput(args[1]);
	}
	
	public void readFile(String path){
		String line = "";
		String splitBy = ",";
		try(BufferedReader br= new BufferedReader(new FileReader(path))) {			
			line=br.readLine();
			
//  Create a HashMap to store drug names taken by each individual to avoid multiple counts 
//  of same drugs by a single person 
			
			HashMap<String, ArrayList<String>> drugsByNames = new HashMap<String, ArrayList<String>>();
			while((line=br.readLine())!=null){
				String[] drugs = line.split(splitBy);
				fullName = drugs[1]+" "+drugs[2];
				drugName = drugs[3];
				drugCost = Double.parseDouble(drugs[4]);
// Adding values to the HashMap drugsByNames 				
				if(drugsByNames.containsKey(fullName)) {
			        if(drugsByNames.get(fullName).contains(drugName)){ //Repeated drug by a person, update only Cost
			        	updateOnlyCost();
			        }
			        else{         //Name of person is entered, but the drug name is new, add the drug name to 
			        	          //persons list and update count and cost for the drug
			        	drugsByNames.get(fullName).add(drugName);
			        	updateCountAndCost();		        	
			        }
			    }else{
			    	ArrayList<String> addValue = new ArrayList<String>();
			    	addValue.add(drugName);
					drugsByNames.put(fullName, addValue);
					updateCountAndCost();
			    }				
			}
			
			//Print Hashmap values
			for (String name: drugsByNames.keySet()){
				System.out.print("Key = "+name+" Values = ");
	            ArrayList<String> values = drugsByNames.get(name);
	            for(int i=0;i< values.size();i++){
	            	System.out.print(" "+values.get(i));  
	            }
			} 
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	public void updateCountAndCost(){
		int flag=0;
		for(int i=0;i<outputValuesList.size();i++){
			OutputValues outputValues = outputValuesList.get(i);
			if(outputValues.getDrug().equalsIgnoreCase(drugName)){
				outputValues.setCount(outputValues.getCount()+1);
				outputValues.setCost(outputValues.getCost()+drugCost);
				outputValuesList.set(i, outputValues);
				flag=1;
			}
		}		
		if(flag==0){
			OutputValues outputValues2= new OutputValues(1,drugName,drugCost);
			outputValuesList.add(outputValues2);
		}
	}
	public void updateOnlyCost(){
		for(int i=0;i<outputValuesList.size();i++){
			OutputValues outputValues = outputValuesList.get(i);
			if(outputValues.getDrug().equalsIgnoreCase(drugName)){
				outputValues.setCost(outputValues.getCost()+drugCost);
				outputValuesList.set(i, outputValues);
				break;
			}
		}
	}
	
	public void writeOutput(String path){
		FileWriter fw=null;
		File file= new File(path);
		try{
			if(file.exists()){
			   fw = new FileWriter(file,false);
			}else{
			   file.createNewFile();
			   fw = new FileWriter(file);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		try (BufferedWriter bw = new BufferedWriter(fw)) {
			for(int i=0;i<outputValuesList.size();i++){
				OutputValues out = outputValuesList.get(i);
				bw.write(out.getDrug()+","+out.getCount()+","+out.getCost());
			}
			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}


