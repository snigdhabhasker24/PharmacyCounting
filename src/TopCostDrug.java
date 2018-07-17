
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
 
public class TopCostDrug {
	private String fullName, drugName;
	private double drugCost;
	private int index;
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
				
				String[] drug = line.split(splitBy);
				
//  The below code is to process additional commas in between names of persons or drugs, as it is a CSV additional element will be created.
				String drugs[]=new String[10];
				if(drug.length > 5){
					for(int i=0;i<drug.length-1;i++){
						if(drug[i].contains("\"") && drug[i+1].contains("\""))
						{
							//System.out.println("drug[i]="+drug[i]+" drug[i+1] = "+drug[i+1]);
							drug[i] = drug[i]+drug[i+1];	// Two strings before and after the additional comma will be concatenated.
							//System.out.println("Edited drug[i] = "+drug[i]);
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
							//System.out.println("A line skipped");
							continue;
						}
					}
				}else{
					drugs=drug;
				}
				/*for(int i=0;i<drugs.length;i++){
					System.out.println("drugs["+i+"]="+drugs[i]);
				}*/
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
				//System.out.print("Key = "+name+" Values = ");
	            ArrayList<String> values = drugsByNames.get(name);
	            for(int i=0;i< values.size();i++){
	            	//System.out.print(" "+values.get(i));  
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
		
		File theDir = new File(path);
		//System.out.println("Path = "+path);
		// if the directory does not exist, create it
		/*if (!theDir.exists()) {
			System.out.println("creating directory: " + theDir.getName());
			boolean result = false;
			try{
				theDir.mkdir();
				result = true;
			} 
			catch(SecurityException se){
				//handle it
			}        
			if(result) {    
				System.out.println("DIR created");  
			}
		}*/
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
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


class OutputValues {
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
}


