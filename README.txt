Evaluator - evaluation of effectiveness measures
------------------------------------------------
This is an implementation of an Evaluation algortihms that calculate the below effectiveness measures:
Precision
Recall
P@K - precision at rank K
MAP - Mean Average Precision
NDCG - Normalized Distributive Cumulative Gain
It works on the given result set of a query and the binary relevance measure of the documents in the corpus for the query in the specified format.
Then it generates the result files for each query with the specifed measures like precision, recall,NDCG etc. and a file with the aggreate measures like MAP.

Build
-----
Developed in Java using jdk1.8.0_05.
Compiled for jre8.
No other libraries used in the implementation

Compile and Run
----------------
For windows:
1. execute executeEval.bat
2. Provide the file path of relevance file 
e.g. C:/IR/Assignment/Rajul_Kumar_CS6200_HW5/rel_file/cacm.rel
3. Provide the queryID of the query in the relevance file
e.g. 12
4. Provide the file path of the result file
e.g. C:/IR/Assignment/Rajul_Kumar_CS6200_HW5/hw3_results/q1_pos.eval
5. Provide the queryID and file paths of other queries as well or press 'n' to continue for evaluation
6. Provide the location where the result files will be generated
e.g. C:/IR/Assignment/Rajul_Kumar_CS6200_HW5/results
7. The results will be generated the given location

For linux/unix*:
1. execute executeEval.sh
2. Provide the file path of relevance file 
e.g. /IR/Assignment/Rajul_Kumar_CS6200_HW5/rel_file/cacm.rel
3. Provide the queryID of the query in the relevance file
e.g. 12
4. Provide the file path of the result file
e.g. /IR/Assignment/Rajul_Kumar_CS6200_HW5/hw3_results/q1_pos.eval
5. Provide the queryID and file paths of other queries as well or press 'n' to continue for evaluation
6. Provide the location where the result files will be generated
e.g /IR/Assignment/Rajul_Kumar_CS6200_HW5/results
7. The results will be generated the given location

* not tested for linux/unix 

Alternatively,
The java files Evaluator.java could be compiled from here by:
javac ./src/com/eval/Evaluator.java

This should be executed from this location by:
java com/eval/Evaluator

Provide the paths required as given above.

* Results for the queries on BM25 engine(HW3) is available at ./hw3_results
  and the relevance file for CACM corpus at ./rel_file

Results/response of execution
-----------------------------
1. The result file with rank, documentID, document score, relevance level, precision, recal and NDCG score is generated for each query at the given result location in the given sequence 
with the file name <queryID>_<MM-dd-yy_HH:mm:ss>.out

2. The result file with P@20 and Mean Average Precision(MAP) is generated at the same given location with the file name precision_<MM-dd-yy_HH:mm:ss>.out

3. The evaluation results are also avaliable in a .pdf file at this location.

References
-----------
1. JavaSE Documentation: https://docs.oracle.com/javase/8/docs/
