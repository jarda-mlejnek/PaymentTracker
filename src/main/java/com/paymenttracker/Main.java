/*
 * Copyright 2015 Jaromir Mlejnek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paymenttracker;

import com.paymenttracker.services.PaymentConsoleReader;
import com.paymenttracker.services.PaymentFileReader;
import com.paymenttracker.services.PaymentPrinterStreamWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Payment-tracker main class which creates threads for parallel reading
 * currency payments from the input file (if specified) and from the command line.
 * It's also crates a thread which periodically prints the net amounts of each currency
 * to the line.
 * @author Jaromir Mlejnek
 */
public class Main {
    
    /**
     * Number of threads for the ScheduledExecutorService
     */
    private static final int NUMBER_OF_THREADS  = 1;
    
    /**
     * The time to delay first execution of the output scheduler
     */
    private static final long JOB_INITIAL_DELAY = 60;
    
    /**
     * The period between successive executions of the output scheduler
     */
    private static final long JOB_PRINT_DELAY   = 60;
    
    /**
     * Exit keyword
     */
    private static final String QUIT_SEQUENCE   = "quit";    
    
    
    public static void main(String[] args) throws IOException {
        
        if (args != null && args.length == 1) {
            // Read payment records from the file on input
            PaymentFileReader fileReader = new PaymentFileReader(args[0]);
            fileReader.start();
        }
        
        // Initialize the console reader which reads payment records from the standard input
        PaymentConsoleReader consoleReader = new PaymentConsoleReader(QUIT_SEQUENCE, System.in);
        consoleReader.start();
                
        // Create the scheduler which periodically prints the net amounts of each currency        
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(NUMBER_OF_THREADS);
        final ScheduledFuture<?> printHandler = scheduler.scheduleAtFixedRate(
                new PaymentPrinterStreamWriter(System.out), JOB_INITIAL_DELAY, JOB_PRINT_DELAY, SECONDS
        );
        
        // A loop which checks if the quit sequece was read from the command line
        while (true) {
            if (consoleReader.isExited()) {
                
                // Cancel the print task
                printHandler.cancel(false);
                
                // Shutdown the scheduler
                scheduler.shutdown();
                break;
            }
        }
    }
    
}
