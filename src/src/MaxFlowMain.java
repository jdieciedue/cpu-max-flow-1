import java.util.*;
import java.lang.*;
import java.io.*;

public class MaxFlowMain {

    private static SequentialMaxFlow s;
    private static ParallelMaxFlow p;

    public static void main (String[] args) {
        // parse the command line arguments
        parseArgs(args);

        // find the maxflow using the sequential algorithm and measure elapsed time
        long start = System.currentTimeMillis();
        int seq_max_flow = s.fordFulkerson();
        long end = System.currentTimeMillis();
        long seq_elapsed = end - start;

        // find the maxflow using the parallel algorithm and measure elapsed time
        start = System.currentTimeMillis();
        int par_max_flow = p.fordFulkerson();
        end = System.currentTimeMillis();
        long par_elapsed = end - start;

        // Print the results and the time elapsed
        if (par_max_flow == seq_max_flow) {
            System.out.println("The maxflow of the graph is " + par_max_flow);
            System.out.println("Sequential execution time : " + seq_elapsed);
            System.out.println("Parallel execution time : " + par_elapsed);
        } else {
            System.out.println(par_max_flow + " " + seq_max_flow);
            System.out.println("Error: Sequential and Parralel found different max flows");
        }
    }

    public static void parseArgs(String[] args) {
        // get the file name, number of threads, and write read variables
        String file_name = args[0];                 // indicates the file that contains the adjecency matrix
        int numThreads = Integer.parseInt(args[1]); // indicates the number of threads used in the parallel algo
        int write_read = Integer.parseInt(args[2]); // 1 indicates reading from file, 0 indicates writing random matrix to file

        // write_read being 1 indicates that the given file does not have an adequite adjecency matrix
        if (write_read == 1) {
            // get the size as the 4th argument passed
            int size = Integer.parseInt(args[3]);
            // create a new random matrix
            int[][] matrix = genRandomMatrix(size);
            // print the given matrix to the file
            writeMatrix(file_name, matrix);
        }

        // create new instances of the sequential and parallel maxflow calulcators
        s = new SequentialMaxFlow(file_name);
        p = new ParallelMaxFlow(file_name, numThreads);
    }

    // generates a random matrix with the following properties
    // edges that move backwards are less likely to appear
    // edges from a vertex to itself cannot happen
    // edges occuring between two verticies occurs at a lower probability, the farther away
    // from eachother they are
    public static int[][] genRandomMatrix(int size) {
        int[][] matrix = new int[size][size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ( i < j) {           // edges that move "backwards"
                    int diff = Math.abs(i - j);
                    int rand_int = rand.nextInt(size - diff);
                    if (rand_int != 0) {
                        rand_int = rand.nextInt(10);
                    }
                    matrix[i][j] = rand_int;
                } else if (i == j) {    // edges from vertex to itself
                    matrix[i][j] = 0;
                } else {                // all other edges
                    int rand_int = rand.nextInt(5*size);
                    if (rand_int == 0) {
                        rand_int = rand.nextInt(10);
                    } else {
                        rand_int = 0;
                    }
                    matrix[i][j] = rand_int;
                }
            }
        }
        return matrix;
    }

    // writes a matrix to the given text file
    public static void writeMatrix(String filename, int[][] matrix) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            int size = matrix.length;
            bw.write(size + "");
            bw.newLine();
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    bw.write(matrix[i][j] + ((j == matrix[i].length-1) ? "" : " "));
                }
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) { }
    }
}