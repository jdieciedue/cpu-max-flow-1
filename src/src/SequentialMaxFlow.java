import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.LinkedList;

public class SequentialMaxFlow {
    private static int size;
    private static int s;
    private static int t;
    private static boolean[] visited;
    private static int[] parent;
    private static int[][] adjacencyMatrix;
    private static LinkedList<Integer> queue;

    public SequentialMaxFlow(String file_name) {
        populateMatrix(file_name);
    }

    // generic ford fulkerson algorithm
    public int fordFulkerson() {
        int max_flow = 0;
        // clear the parent list
        for (int i = 0; i < size; i++) {
            parent[i] = 0;
        }
        // iterate while a path to the sink exists from the source
        while (sequentialBFS()) {
            int path_flow = Integer.MAX_VALUE;
            // sequentialBFS populates parent with the node preceding node at given vertex
            // this lets us use the loop below to back track from the sink to the source, and find the maximum flow along the path
            for (int v=t; v!=s; v=parent[v]) {
                int u = parent[v];
                path_flow = Math.min(path_flow, adjacencyMatrix[u][v]);
            }
            // once the maximum new flow of the path is found, we update the path and residual
            for (int v = t; v != s; v=parent[v]) {
                int u = parent[v];
                adjacencyMatrix[u][v] -= path_flow;
                adjacencyMatrix[v][u] += path_flow;
            }
            // add path flow to the maxflow
            max_flow += path_flow;
        }
        return max_flow;
    }

    // generic bfs
    boolean sequentialBFS() {
        for(int i=0; i<size; ++i) {
            visited[i] = false;
        }
        queue = new LinkedList<>();
        queue.add(s);
        visited[s] = true;
        parent[s]=-1;

        while (queue.size()!=0) {
            int u = queue.poll();
            for (int v=0; v<size; v++) {
                // only adds a vertex if it is not visited and there is a positive edge weight
                if (visited[v]==false && adjacencyMatrix[u][v] > 0) {
                    // parent is updated with its sources node
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        return (visited[t] == true);
    }

    // populates the local matrix from the text file
    public static void populateMatrix(String file_name) {
        Scanner input = null;
        try {
            input = new Scanner(new File(file_name));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        size = input.nextInt();
        adjacencyMatrix = new int [size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                adjacencyMatrix[i][j] = input.nextInt();
            }
        }
        s = 0;
        t = size-1;
        parent = new int[size];
        visited = new boolean[size];
    }
}