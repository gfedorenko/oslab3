// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;
import java.io.*;
import java.util.Random;

public class SchedulingAlgorithm {


    public static Results Run(int runtime, Vector processVector, Results result, int ticketssum) {

        result.schedulingType = "Interactive (Preemptive)";
        result.schedulingName = "Lottery";

        String resultsFile = "Summary-Processes";
        int comptime = 0;
        int currentProcess;
        int size = processVector.size();
        int completed = 0;

        try {

            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            sProcess process = (sProcess) processVector.elementAt(0);
            Random rand = new Random();

            currentProcess = 0;
            while (comptime < runtime) {

                if (process.cpudone == process.cputime) {

                    completed++;
                    out.println("process: " + currentProcess + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + ")");

                    processVector.remove(process);
                    ticketssum -= process.ticketsnum;

                    if (completed == size) {
                        result.compuTime = comptime;
                        out.close();
                        return result;
                    }


                    currentProcess = 0;
                    int counter = 0;
                    int winner = rand.nextInt(ticketssum);
                    out.println(winner);
                    while (counter < winner) {
                        process = (sProcess) processVector.elementAt(currentProcess);
                        counter += process.ticketsnum;
                        currentProcess++;
                    }
                    --currentProcess;
                    process = (sProcess) processVector.elementAt(currentProcess);


                    out.println("process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " "+ process.ionext+")");
                }

//                if (process.ioblocking == process.ionext) {
//
//                    out.println("process: " + currentProcess + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + ")");
//
//                    process.numblocked++;
//                    process.ionext = 0;
//
//                    if (processVector.size() > 1) {
//
//                        currentProcess = 0;
//                        int counter = 0;
//                        int winner = rand.nextInt(ticketssum);
//                        while (counter < winner) {
//                            process = (sProcess) processVector.elementAt(currentProcess);
//                            counter += process.ticketsnum;
//                            currentProcess++;
//                        }
//                        --currentProcess;
//                        process = (sProcess) processVector.elementAt(currentProcess);
//
//                        out.println("process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + ")");
//                    }
//                }

                if (process.ioblocking > 0) {
                    process.ionext++;
                }

                process.cpudone++;
                comptime++;
            }

            out.close();

        } catch (IOException e) {  }
        result.compuTime = comptime;
        return result;
    }

}

