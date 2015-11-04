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
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Set;

/**
 * This class implements a payment printer which prints
 * the net amounts of each currency into the print stream.
 * @author Jaromir Mlejnek
 */
public class PaymentPrinterStreamWriter implements Runnable {
    
    private final PrintStream printStream;        
    
    public PaymentPrinterStreamWriter(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void run() {                
        print();
    }
    
    /**
     * Prints the net amounts of each currency into the print stream.
     */
    private void print() {
        Set<String> currencies = CurrencyHolder.getInstance().getCurrenciesStatus().keySet();
        Iterator<String> iterator = currencies.iterator();
        String output;
        
        while (iterator.hasNext()) {
            String currency = iterator.next();
            double ammount =  CurrencyHolder.getInstance().getCurrenciesStatus().get(currency);            
            if (ammount != 0) {
                output = String.format("%s %.0f", currency, ammount);
                this.printStream.println(output);
            }
        }
    }
}
