# cpu-max-flow-1
Implemented parallel CPU based algorithms to compute max-flow

In order to test this program you will need to clone the directory first. Then navigate to the src directory containing MaxFlowMain.java, ParallelMaxFlow.java, SequentialMaxFlow.java, and txt files used to store the graphs. Use the following commands to compile and run the code.

- javac MaxFlowMain.java ParallelMaxFlow.java SequentialMaxFlow.java
- java MaxFlowMain (file-name) (num-threads) 0
  
For example  
% javac MaxFlowMain.java ParallelMaxFlow.java SequentialMaxFlow.java  
% java MaxFlowMain graph6.txt 3 0  
  
This will run the two algorithms on the given graph which is stored as an adjacency matrix. If you wanted to create and test your own graph they require the input to look like the following.  

(size)  
[size x size]  
  
Where size is an integer and size x size is a square matrix of numbers where each number is separated by a space and only a space. For example  

3  
0 1 2  
3 0 4  
0 0 0  

Notice two things. First, the diagonal representing an edge from a vertex to itself is always 0. Second, the bottom row corresponds to the sink vertex, and must only be 0's.  

Alternatively you can use a built in function which will generate a graph the follows the above two parameters and will then run the max flow algorithm on it. To do this use the following commands  

- javac MaxFlowMain.java ParallelMaxFlow.java SequentialMaxFlow.java  
- java MaxFlowMain (file-name) (num-threads) 1 (size)  
  
For example  
% javac MaxFlowMain.java ParallelMaxFlow.java SequentialMaxFlow.java  
% java MaxFlowMain graph.txt 3 1 100  

This will create a new 100 x 100 graph, store that graph in graph.txt, and then run both algorithms on them and give the result.  

Regardless of which way you run the code, the output will be in the form:  
The maxflow of the graph is (flow)  
Sequential execution time : (time elapsed)  
Parallel execution time : (time elapsed)  
  
For example if you run the commands  
% javac MaxFlowMain.java ParallelMaxFlow.java SequentialMaxFlow.java  
% java MaxFlowMain graph6.txt 3 0  

The output should be similar to  
The maxflow of the graph is 23  
Sequential execution time : 0  
Parallel execution time : 11  
  
Where the exact elapsed time may be differnt.  

If for whatever reason, the maxflow return by Parallel and Sequential algorithms is not the same, an error message will be displayed. This likely means the given inputs are invalid. For large matrix sizes, the code my take several minutes to exectute.  
