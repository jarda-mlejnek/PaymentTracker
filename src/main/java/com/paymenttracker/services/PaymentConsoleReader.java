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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a console-read functionality. It reads payment records
 * from the specified input stream and stores them.
 * @author Jaromir Mlejnek
 */
public class PaymentConsoleReader extends Thread {

    private static final Logger log = Logger.getLogger(PaymentConsoleReader.class.getName());
        
    private final String exitSequence; 
    private final InputStream inputStream;
    private volatile boolean exited = false;

    public PaymentConsoleReader(String exitSequence, InputStream inputStream) {
        this.exitSequence = exitSequence;        
        this.inputStream = inputStream;
    }        
    
    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(this.inputStream));
        Payment payment;
        String line;
        
        while (true) {
            try {
                line = br.readLine();
                if (line != null && line.equalsIgnoreCase(this.exitSequence)) {

                    // The exit sequence was read - break
                    this.exited = true;                    
                    break;
                }                

                // Parse the line into a payment record
                payment = PaymentParser.parse(line);

                // Register the payment record
                CurrencyHolder.getInstance().registerPayment(payment);
            } catch (IOException | PaymentParserException ex) {            
                log.log(Level.WARNING, ex.getMessage());
            }
        }                
    }

    public boolean isExited() {
        return exited;
    }
}

