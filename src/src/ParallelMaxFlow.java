import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParallelMaxFlow {

    private static int numThreads;
    private static int size;
    private static int s;
    private static int t;
    private static boolean[] visited;
    private static int[] parent;
    private static int[][] adjacencyMatrix;
    private static ConcurrentLinkedQueue<Integer> queue;

    public ParallelMaxFlow(String file_name, int num_threads) {
        populateMatrix(file_name);
        this.numThreads = num_threads;
    }

    public int fordFulkerson() {
        int max_flow = 0;
        for (int i = 0; i < size; i++) {
            parent[i] = 0;
        }
        while (parallelBFS()) {
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

    boolean parallelBFS() {
        for(int i=0; i<size; ++i) {
            visited[i] = false;
        }
        queue = new ConcurrentLinkedQueue<>();
        queue.add(s);
        visited[s] = true;
        parent[s]=-1;

        while (queue.size() != 0) {
            int u = queue.poll();
            NeighborAdd.parallelAdd(u, numThreads);
        }

        return (visited[t] == true);
    }

    public static class NeighborAdd implements Runnable {

        private int start;
        private int end;
        private int row;

        private NeighborAdd(int start, int end, int row, int id) {
            this.start = start;
            this.end = end;
            this.row = row;
        }

        public static void parallelAdd(int row, int numThreads){
            int chunk_size = size / numThreads;
            int start = 0;
            int end = chunk_size;
            ArrayList<Thread> threads = new ArrayList<>();
            for (int i = 0; i < numThreads; i++){
                if(i == numThreads - 1) {
                    end = size;
                }
                Thread t = new Thread(new NeighborAdd(start, end, row, i));
                start += chunk_size;
                end += chunk_size;
                threads.add(t);
                t.start();
            }
            for (int i = 0; i < numThreads; i++) {
                try {
                    threads.get(i).join();
                } catch (Exception e) { }
            }
        }

        @Override
        public void run() {
            int i = this.start;
            while (i < end) {
                if (adjacencyMatrix[row][i] > 0 && !visited[i]) {
                    queue.add(i);
                    parent[i] = row;
                    visited[i] = true;
                }
                i++;
            }
        }

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