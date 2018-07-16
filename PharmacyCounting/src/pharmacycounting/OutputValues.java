package pharmacycounting;
public class OutputValues {
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