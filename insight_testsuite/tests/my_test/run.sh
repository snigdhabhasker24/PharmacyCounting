#!/bin/bash
javac ./src/TopCostDrug.java
java -classpath ./src TopCostDrug ./input/itcont.txt ./output/top_cost_drug.txt
