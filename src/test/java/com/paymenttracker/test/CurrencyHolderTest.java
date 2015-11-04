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

import com.paymenttracker.model.CurrencyHolder;
import com.paymenttracker.model.Payment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * This class tests the CurrencyHolder class. Tests are executed
 * in a name ascending way.
 * @author Jaromir Mlejnek
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CurrencyHolderTest {
    
    private static final List<Payment> payments = new ArrayList<>();
    private static final Map<String, Double> currenciesAmmount = new HashMap<>();
    private static final int NUMBER_OF_THREADS = 100;
    private static final double DELTA = 0.1;
    
    public CurrencyHolderTest() {        
    }
    
    /**
     * Prepares data for tests.
     */
    @BeforeClass
    public static void setUpClass() {  
        // List of payments
        payments.add(new Payment("USD", -1000));
        payments.add(new Payment("HKD", 100));
        payments.add(new Payment("USD", -100));
        payments.add(new Payment("RMB", 2000));
        payments.add(new Payment("CZK", 20000));
        payments.add(new Payment("USD", 1000));
        payments.add(new Payment("HKD", 100));
        payments.add(new Payment("USD", -100));
        payments.add(new Payment("RMB", 2000));
        payments.add(new Payment("HKD", 200));
        payments.add(new Payment("CZK", -2000));
        
        // Computes the net amount for each currency
        calculateCurrenciesAmmount();
    }
    
    /**
     * 
     */
    private static void calculateCurrenciesAmmount() {
        for (Payment payment : payments) {
            if (!currenciesAmmount.containsKey(payment.getCurrencyCode())) {
                currenciesAmmount.put(payment.getCurrencyCode(), payment.getAmount());
            } else {
                double currencyTotal = currenciesAmmount.get(payment.getCurrencyCode());
                currenciesAmmount.put(payment.getCurrencyCode(), currencyTotal + payment.getAmount());
            }
        }
    }
    
    /**
     * Tests that a new currency holder is empty.
     */
    @Test
    public void test1Create() {
        Map<String, Double> summary = CurrencyHolder.getInstance().getCurrenciesStatus();
        assertThat(summary.isEmpty(), is(true));
    }
    
    /**
     * Tests that the currency holder will have size equals to 1
     * after a payment is inserted.
     */
    @Test
    public void test2Update() {
        final Payment payment = new Payment("USD", 100);
        CurrencyHolder.getInstance().registerPayment(payment);
        
        Map<String, Double> summary = CurrencyHolder.getInstance().getCurrenciesStatus();
        assertThat(summary.size(), is(1));
        
        assertEquals("usd = 100", 100, summary.get("USD"), DELTA);
    } 
    
    /**
     * This test does following:
     *  - resets the currency holder and tests that is empty
     *  - creates {@link CurrencyHolderTest#NUMBER_OF_THREADS} tasks that 
     *  simultaneously iterates over payments {@link CurrencyHolderTest#payments}
     *  and register them in the currency holder. Finally checks that the currency
     * holder has a correct content.
     */
    @Test
    public void test3UpdateByMultipleThread() {
        // Empty the currency holder and test it
        CurrencyHolder.getInstance().resetRegister();
        Map<String, Double> summary = CurrencyHolder.getInstance().getCurrenciesStatus();
        assertThat(summary.isEmpty(), is(true));               
        
        // Create ExecuterService
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        
        List<Future<Boolean>> futureList = new ArrayList<>();        
        Callable<Boolean> callable = new ExecuteRegisterCall();
        
        // Creates NUMBER_OF_THREADS task that fill the currency holdes parallel
        for(int i = 0; i < NUMBER_OF_THREADS; i++){
            
            // Submit Callable tasks to be executed by thread pool
            Future<Boolean> future = executor.submit(callable);            
            futureList.add(future);
        }
        
        // Iterates over the future list ...
        for(Future<Boolean> fut : futureList){
            try {                               
                // .. and waits for task to get completed
                fut.get();
                
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        // Shutdown the executor service
        executor.shutdown();
        
        // Check that the currency holder has the same number of records 
        // like the pre-calculated "currenciesAmmount" map
        summary = CurrencyHolder.getInstance().getCurrenciesStatus();        
        assertEquals(currenciesAmmount.keySet().size(), summary.size());
        
        // Check that the currency holder has the net ammount for each currency
        // equal to the pre-calculated value in "currenciesAmmount"
        for (String key : currenciesAmmount.keySet()) {            
            assertEquals("currency: " + key, currenciesAmmount.get(key) * NUMBER_OF_THREADS, 
                    summary.get(key), DELTA);    
        }
    }
            
    private static class ExecuteRegisterCall implements Callable<Boolean> {

        public ExecuteRegisterCall() {
        }

        @Override
        public Boolean call() throws Exception {            
            // Iterate over payments and register them in the currency holder.
            for (Payment payment : payments) {                
                CurrencyHolder.getInstance().registerPayment(payment);
            }
            
            return true;
        }
        
        
    }
}
