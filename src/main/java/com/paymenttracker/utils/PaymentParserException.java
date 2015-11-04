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

/**
 * Thrown to indicate an exception during parsing a payment record.
 * @author Jaromir Mlejnek
 */
public class PaymentParserException extends Exception {

    private final String line;
    
    public PaymentParserException() {
        super();
        this.line = "";
    }        
    
    public PaymentParserException(String message) {
        super(message);
        this.line = "";
    }

    public PaymentParserException(String message, String line) {
        super(message);
        this.line = line;
    }

    @Override
    public String getMessage() {        
        return String.format("%s - thrown for line '%s'", super.getMessage(), this.line);
    }
    
}
