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

    public int fordFulkerson() {
        int max_flow = 0;
        for (int i = 0; i < size; i++) {
            parent[i] = 0;
        }
        while (sequentialBFS()) {
            int path_flow = Integer.MAX_VALUE;
            for (int v=t; v!=s; v=parent[v]) {
                int u = parent[v];
                path_flow = Math.min(path_flow, adjacencyMatrix[u][v]);
            }
            for (int v = t; v != s; v=parent[v]) {
                int u = parent[v];
                adjacencyMatrix[u][v] -= path_flow;
                adjacencyMatrix[v][u] += path_flow;
            }
            max_flow += path_flow;
        }
        return max_flow;
    }

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
                if (visited[v]==false && adjacencyMatrix[u][v] > 0) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        return (visited[t] == true);
    }

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