# PharmacyCounting
This task is implemented in Java. The run commands are specified in the run.sh file. The input and output files are passed as command line arguments along with the folder name.

javac ./src/TopCostDrug.java
java -classpath ./src TopCostDrug ./Input/itcont.txt ./Output/top_cost_drug.txt

Approach:

- HashMaps<customer_name, List<DrugNames>> are used to store the drug names taken by each customer, with customer full name as key to avoid recounting them. The output values are stored as the members of a class with drug name, count and the drug cost.

- When a new line is read from the file, the HashMap is checked if the customer name (first_name + last_name) is already present in it. If it is not present a new entry is made to the HashMap with the customer name and the drug name. 

- If it is present then the list of drug names is checked if the drug name is present in the list. If the drug name is present, then only the cost of that drug is updated ( in the output list), else the drug name is added to the values list of the  customer name and both the cost and count are updated.

- All these operations are performed in different methods for modularity.

- Comparator interface is used to sort the output values based on cost and drug names. The list is sorted in descending order of cost values, if the cost values are same, then it is sorted in ascending order of drug names.

- Data processing is done to handle the additional commas in between the values.
