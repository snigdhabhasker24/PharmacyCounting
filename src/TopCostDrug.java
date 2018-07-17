
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
 
public class TopCostDrug {
	private String fullName, drugName;
	private double drugCost;
	private int index;
	static ArrayList<OutputValues> outputValuesList = new ArrayList<OutputValues>();
	
	public static void main(String[] args){
		TopCostDrug topCostDrug = new TopCostDrug();
		topCostDrug.readFile(args[0]);

		Collections.sort(outputValuesList, OutputValues.sortValues);  // To sort the values based on cost and drug names.
		topCostDrug.writeOutput(args[1]);    // Write the output to a file.
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
				String[] drug = line.split(splitBy);
				
//  The below code is to process the data where additional commas in between names of persons or drugs, as it is a CSV additional element will be created.
				String drugs[]=new String[10];
				if(drug.length > 5){
					for(int i=0;i<drug.length-1;i++){
						if(drug[i].contains("\"") && drug[i+1].contains("\""))
						{
							drug[i] = drug[i]+drug[i+1];	// Two strings before and after the additional comma will be concatenated.
							index=i+1;     // Index of the concatenated additional string which has to be removed.
							int k=0;
							for(int j=0;j<drug.length-1;j++){	// Removing the additional element
								if(j==index){
									k+=1;
								}
								drugs[j]=drug[k];
								k+=1;
							}
						}
						else{
							continue;
						}
					}
				}else{
					drugs=drug;
				}
				fullName = drugs[1]+" "+drugs[2];
				drugName = drugs[3];
				drugCost = Double.parseDouble(drugs[4]);
// Adding values to the HashMap drugsByNames 				
				if(drugsByNames.containsKey(fullName)) {
			        if(drugsByNames.get(fullName).contains(drugName)){ //Repeated drug by a person, update only Cost
			        	updateOnlyCost();
			        }
			        else{         //Name of person is entered, but if the drug name is new, add the drug name to 
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
		}catch (IOException e){
			e.printStackTrace();
		}
	}
// Method to update the count of drugs and cost	
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
//Method to update only cost	
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
//Method to write output to a file	
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
				bw.newLine();
			}
			System.out.println("Done. Please check the given output file for results.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
//The class OutputValues is used to store the output values of different types 
class OutputValues{
	private int count;
	private String drug;
	private double cost;
	
	public OutputValues(int count,String drug,double cost){
		this.count=count;
		this.drug=drug;
		this.cost=cost;
	}
	public int getCount(){
		return count;
	}
	public String getDrug(){
		return drug;
	}
	public double getCost(){
		return cost;
	}
	public void setCount(int count){
		this.count =count;
	}
	public void setDrug(String drug) {
		this.drug = drug;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	
// The below method is used to sort the arraylist outputValuesList based on cost and 
	//drug names using the Comparator interface  
	public static Comparator<OutputValues> sortValues = new Comparator<OutputValues>(){
		public int compare(OutputValues out1,OutputValues out2){
			double cost1=out1.getCost();
			double cost2=out2.getCost();
		    
			int cmp= (int)Math.round(cost2-cost1);
			if (cmp != 0) {    //If both costs are different return the difference,
	            return cmp;    // else return the difference of drug names to sort by drugnames.
	        }
			String drugName1 = out1.getDrug();
			String drugName2 = out2.getDrug();
 		
			return drugName1.compareTo(drugName2);
		}
	};
}