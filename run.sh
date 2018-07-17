#!/bin/bash
javac ./src/TopCostDrug.java
java -classpath ./src TopCostDrug ./Input/itcont.txt ./Output/top_cost_drug.txt
