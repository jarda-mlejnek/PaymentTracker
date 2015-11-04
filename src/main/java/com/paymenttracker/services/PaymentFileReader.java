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

package com.paymenttracker.services;

import com.paymenttracker.model.CurrencyHolder;
import com.paymenttracker.model.Payment;
import com.paymenttracker.utils.PaymentParserException;
import com.paymenttracker.utils.PaymentParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a file-read functionality. It reads payment records
 * from the specified file and stores them.
 * @author Jaromir Mlejnek
 */
public class PaymentFileReader extends Thread {

    private static final Logger log = Logger.getLogger(PaymentFileReader.class.getName());
    
    private final String pathToFile;
    
    public PaymentFileReader(String pathToFile) {        
        this.pathToFile = pathToFile;
    }

    @Override
    public void run() {
        // Open the file
        File file = new File(pathToFile);
        
        try {        
            // Check the file for existence
            checkFile(file);

            // Read the file
            readFile(file);
            
        } catch (IllegalArgumentException ex) {
            log.log(Level.WARNING, ex.getMessage());
        }
    }        
    
    /**
     * Checks if the input file exists and can be read.
     * @param file the input file
     */
    private void checkFile(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("File '" + pathToFile + "' does not exist");
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException("File '" + pathToFile + "' cannot be read");
        }        
    }
    
    /**
     * Reads the input file line by line, parses the lines, creates a payment records
     * and registers them into a payment register.
     * @param file the input file
     */
    private void readFile(File file) {
        BufferedReader br;
        String line;
        Payment payment;                
        
        try {
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {                
                
                try {
                    // Parse the line into a payment record
                    payment = PaymentParser.parse(line);
                    
                    // Register the payment record
                    CurrencyHolder.getInstance().registerPayment(payment);
                    
                } catch (PaymentParserException ex1) {
                    log.log(Level.WARNING, ex1.getMessage());
                }                                
            }
            
        } catch (IOException ex) {
            log.log(Level.WARNING, ex.getMessage());
        }        
    }
    
}
