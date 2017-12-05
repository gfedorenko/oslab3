// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;
import java.io.*;
import java.util.Random;

class SchedulingAlgorithm {

    static Results Run(int quantum, int runtime, Vector processVec, Results result, int ticketssum) {

        result.schedulingType = "Interactive";
        result.schedulingName = "Lottery";

        Vector<sProcess> processVector = (Vector<sProcess>) processVec.clone();

        String resultsFile = "Summary-Processes";
        Random rand = new Random();
        int comptime = 0;
        int currentProcess;
        int size = processVector.size();
        int tempsize = size;
        int completed = 0;
        sProcess process = (sProcess) processVector.elementAt(0);
        try {

            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));

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

            while (comptime < runtime) {

                if (process.cpudone == process.cputime) {

                    completed++;
                    out.println("process: " + currentProcess + " completed... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.ionext + ")");

                    // remove process and update number of tickets
                    ticketssum -=process.ticketsnum;
                    tempsize--;
                    processVector.remove(process);
                    if (completed == size) {
                        result.compuTime = comptime;
                        out.close();
                        return result;
                    }

                    while (true) {
                        int current = 0;
                        counter = 0;
                        winner = rand.nextInt(ticketssum)+1;
                        // out.println(winner);
                        while (counter < winner) {
                            process = (sProcess) processVector.elementAt(current);
                            counter += process.ticketsnum;
                            current++;
                        }
                        --current;
                        process = (sProcess) processVector.elementAt(current);
                        if (process.cpudone < process.cputime) {
                            currentProcess = current;
                            break;
                        }
                    }

                    process = processVector.elementAt(currentProcess);
                    out.println("process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.ionext + ")");
                }

                if (process.ioblocking == process.ionext) {

                    out.println("process: " + currentProcess + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.ionext + ")");

                    process.numblocked++;
                    process.ionext = 0;
                    process.next = 0;

                    if (processVector.size() > 1) {
                        while (true) {
                            int current = 0;
                            counter = 0;
                            winner = rand.nextInt(ticketssum)+1;
                            // out.println(winner);
                            while (counter < winner) {
                                process = (sProcess) processVector.elementAt(current);
                                counter += process.ticketsnum;
                                current++;
                            }
                            --current;
                            process = (sProcess) processVector.elementAt(current);
                            if (process.cpudone < process.cputime) {
                                currentProcess = current;
                                break;
                            }
                        }

                        process = processVector.elementAt(currentProcess);;
                        out.println("process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.ionext + ")");
                    }
                }

                if (process.ioblocking > 0) {
                    process.ionext++;
                }

                if (process.next == quantum){
                    process.next = 0;

                    if (processVector.size() == 1) currentProcess = 0; // if only one process left, start it

                    else {
                        while (true) {
                            int current = 0;
                            counter = 0;
                            winner = rand.nextInt(ticketssum)+1;
                            //out.println(winner);
                            while (counter < winner) {
                                process = (sProcess) processVector.elementAt(current);
                                counter += process.ticketsnum;
                                current++;
                            }
                            --current;
                            process = (sProcess) processVector.elementAt(current);
                            if (process.cpudone < process.cputime) {
                                currentProcess = current;
                                break;
                            }
                        }
                    }

                    process = processVector.elementAt(currentProcess);
                    out.println("process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + process.ionext + ")");
                }
                process.cpudone++;
                process.next++;
                comptime++;
            }

            out.close();
            System.out.println(processVector.size());

        } catch (IOException e) { }
        result.compuTime = comptime;
        return result;
    }

}

