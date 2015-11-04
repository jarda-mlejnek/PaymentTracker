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

package com.paymenttracker.test;

import static org.junit.Assert.assertEquals;
import com.paymenttracker.model.Payment;
import com.paymenttracker.utils.PaymentParserException;
import com.paymenttracker.utils.PaymentParser;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This class tests the PaymentParser class.
 * @author Jaromir Mlejnek
 */
@RunWith(value = Parameterized.class)
public class PaymentParserTest {
    
    private static final Payment PLUS_PAYMENT = new Payment("USD", 100);
    private static final Payment MINUS_PAYMENT = new Payment("USD", -100);
    
    private final String line;
    private final Payment payment;
    
    public PaymentParserTest(String line, Payment payment) {
        this.line = line;
        this.payment = payment;
    }
    
    @Parameters
    public static Iterable<Object[]> testData() {        
        return Arrays.asList(new Object [][] {
            { "USD 100", PLUS_PAYMENT }, 
            { "USD -100", MINUS_PAYMENT },
            { "USDD 100", null },
            { "usd 100", null },
            { "usd -100", null },
            { "  USD   100   ", PLUS_PAYMENT },
            { "  USD   -100   ", MINUS_PAYMENT },
            { "  usd   100   ", null },
            { "  USD   100   sa", null },
            { "  USD a  100", null },
            { "  USD \t  100", PLUS_PAYMENT },
            { "  USD \t  -100", MINUS_PAYMENT },
            { "  USD \t  -dasdas", null },
        });
    }

    /**
     * Tests that the parser returns correct results.
     */
    @Test
    public void parserTest() {
        Payment parsedPayment;
        
        try {
            parsedPayment = PaymentParser.parse(line);                        
            
        } catch (PaymentParserException ex) {            
            parsedPayment = null;            
        }
                
        assertEquals(payment, parsedPayment);        
    }
}
