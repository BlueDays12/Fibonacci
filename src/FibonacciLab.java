import java.util.Scanner;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.Math;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;


public class FibonacciLab {
    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
    static String ResultsFolderPath = "/home/matt/Results/"; // pathname to results folder
    static FileWriter resultsFile;
    static PrintWriter resultsWriter;
    static int numberOfTrials = 20;

    public static void main(String[] args) {
        // Get integer for testing purposes
        /*
        System.out.println("Input x");
        Scanner fibNum = new Scanner(System.in);
        long x = fibNum.nextLong();
         */

        System.out.println("X     N     Result         Time");

        program("fib.txt");
    }


    static void program(String resultsFileName) {
        long N = 0;
        long result = 0;

        // To open a file to write to
        try {
            resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);
            resultsWriter = new PrintWriter(resultsFile);
        } catch(Exception e) {
            System.out.println("*****!!!!!  Had a problem opening the results file "+ResultsFolderPath+resultsFileName);
            return;
        }

        ThreadCpuStopWatch stopwatch = new ThreadCpuStopWatch(); // for timing an entire set of trials

        resultsWriter.println("#X     N     Result         AvgTime"); // # marks a comment in gnuplot data
        resultsWriter.flush();

        for (int i = 0; i < 40; ++i) {
            long elapsedTime = 0;
            System.gc();
            stopwatch.start(); // Start timer in nano secs

            for (int trial = 0; trial < numberOfTrials; ++trial) {
                //stopwatch.start(); // Start timer in nano secs
                //for (int trial = 0; trial < numberOfTrials; ++trial) {
                if (i == 1)
                    result = 1;
                else
                    //result = fibLoop(i);
                    //result = fibRecur(i);
                    //result = fibRecurDP(i);
                    result = fibMatrix(i);

                if (result <= 1)
                    N = 1;
                else
                    N = (int) (Math.log(result) / Math.log(2) + 1);

                // Output for testing
                //elapsedTime = stopwatch.elapsedTime(); // Get the elapsed time from ThreadCpuStopWatch class
                //System.out.format("%-5d %-5d %-5d %15d \n", i, N, result, elapsedTime);
            }
            elapsedTime = stopwatch.elapsedTime();
            double averageTimePerTrialInBatch = (double) elapsedTime / (double)numberOfTrials;
            resultsWriter.printf("%-5d %-5d %-5d %20f \n", i, N, result, averageTimePerTrialInBatch);
            resultsWriter.flush();
        }
    }

    public static long fibLoop (long x) {
        // Declare variables
        int sum = 0, first = 0, next = 1;

        // Create a loop for (n-1)+(n-2) and put it in sum
        // and replace the first with the next element and make
        // next the sum
        for (int i = 1; i < x; ++i) {
            sum = first + next;
            first = next;
            next = sum;
        }
        return sum;  // return the value sum
    }

    public static long fibRecur (long x) {
        if (x<=1)
            return x;
        else
            return fibRecur(x-1) + fibRecur(x-2);
    }

    public static long fibRecurDP (long x) {
        // Need to create array starting at x+2 because we need to
        // initialize our first two elements in the array
        long myArray[] = new long[(int) (x+2)];
        int i;

        // Initialize the first and second element of the array to 0 and 1
        myArray[0] = 0;
        myArray[1] = 1;

        // Start for loop at 3rd element since elements 0 and 1 have been declared
        for (i = 2; i <= x; ++i) {
            // Add the number x-1 and x-2 together
            myArray[i] = myArray[i-1] + myArray[i-2];
        }
        return myArray[(int) x];
    }

    public static long fibMatrix (long x) {
        long myArray[][] = new long[][]{{1,1},{1,0}};
        if (x == 0)
            return 0;
        multi(myArray, x-1); // call multi function to multiply the values to get x-1 and x-2
                                // then adding them together

        return myArray[0][0];
    }

    static void multi (long myArray[][], long x) {
        int i;
        long myArray2[][] = new long[][] {{1,1},
                                          {1,0}};

        // n-1 times multiply the matrix to {1,0}
        //                                  {0,1}
        for (i = 2; i <= x; ++i) {
            long a = myArray[0][0]*myArray2[0][0] + myArray[0][1]*myArray2[1][0];
            long b = myArray[0][0]*myArray2[0][1] + myArray[0][1]*myArray2[1][1];
            long c = myArray[1][0]*myArray2[0][0] + myArray[1][1]*myArray2[1][0];
            long d = myArray[1][0]*myArray2[0][1] + myArray[1][1]*myArray2[1][1];

            myArray[0][0] = a;
            myArray[0][1] = b;
            myArray[1][0] = c;
            myArray[1][1] = d;
        }
    }
}

