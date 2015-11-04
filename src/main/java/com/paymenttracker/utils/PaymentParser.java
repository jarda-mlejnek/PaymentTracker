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
package com.paymenttracker.utils;

import com.paymenttracker.model.Payment;

/**
 * This class parses Strings into Payment objects.
 * @author Jaromir Mlejnek
 */
public class PaymentParser {

    /**
     * The delimeter between currency code and amount.
     */
    private static final String DELIMITER   = " ";
    
    /**
     * The length of currency code field.
     */
    private static final int CODE_LENGTH    = 3;
    
    private PaymentParser() {
    }
    
    /**
     * Parses the String line on the input into an instance of Payment class.
     * If the line cannot be parsed into a Payment object then 
     * PaymentParserException is thrown.
     * @param line an input String line
     * @return The payment record for given input.
     * @throws PaymentParserException 
     */
    public static final Payment parse(String line) throws PaymentParserException {
        if (line == null || "".equals(line)) return null;
        
        // Remove white spaces
        line = line.replaceAll("\\s+", DELIMITER).trim();
        
        // Splip according to the delimiter
        String [] parts = line.split(DELIMITER);
        if (parts.length != 2) {
            throw new PaymentParserException("Invalid format of payment record", line);
        }
        
        try {
            String currencyCode = getCurrencyCode(parts[0]);
            double amount = getAmount(parts[1]);
            
            return new Payment(currencyCode, amount);
        
        } catch (IllegalArgumentException ex) {
            throw new PaymentParserException(ex.getMessage(), line);
        }        
    }
    
    /**
     * Returns a currency code for given input.
     * @param str
     * @return 3 letter String which represents a currency code if it's valid one.
     */
    private static String getCurrencyCode(String str) {       
        if (str.length() != CODE_LENGTH) {
            throw new IllegalArgumentException("Only 3 char's code is supported");
        }
        if (!str.equals(str.toUpperCase())) {
            throw new IllegalArgumentException("Only upper-case code is supported");
        }
        
        return str;
    }
    
    /**
     * Returns an amount for for given input.
     * @param str
     * @return Double representation of a amount if it's a valid one.
     */
    private static double getAmount(String str) {
        double value = 0;
        
        try {
            value = Double.parseDouble(str);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid format for amount");
        }
        
        return value;
    }
}
