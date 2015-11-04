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

package com.paymenttracker.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This class is used as a thread-safe register for currency payments.
 * It implements a thread-safe modification of Singleton pattern.
 * @author Jaromir Mlejnek
 */
public final class CurrencyHolder {        
    
    /**
     * Payment register where keys are currency codes and values
     * are the net amounts for given currency.
     */
    private ConcurrentMap<String, Double> currenciesStatus;
    
    private CurrencyHolder() {
        if (CurrencyLoader.INSTANCE != null) {
            throw new IllegalStateException("CurrencyLoader already instantiated");
        }
        
        currenciesStatus = new ConcurrentHashMap<>();
    }
    
    /**
     * Registers a currency payment to the register
     * @param payment the payment for given currency
     */
    public synchronized void registerPayment(Payment payment) {
        if (!currenciesStatus.containsKey(payment.getCurrencyCode())) {
            currenciesStatus.put(payment.getCurrencyCode(), payment.getAmount());
        } else {
            double currencyTotal = currenciesStatus.get(payment.getCurrencyCode());
            currenciesStatus.put(payment.getCurrencyCode(), currencyTotal + payment.getAmount());
        }
    }
    
    /**
     * Resets the register.
     */
    public void resetRegister() {
        currenciesStatus.clear();
    }

    /**
     * Returns actual payment register map.
     * @return The payment register map.
     */
    public Map<String, Double> getCurrenciesStatus() {
        return currenciesStatus;
    }    
    
    // Thread-safe singleton implementation
    private static class CurrencyLoader {
        private static final CurrencyHolder INSTANCE = new CurrencyHolder();
    }
    
    public static CurrencyHolder getInstance() {
        return CurrencyLoader.INSTANCE;
    }
        
}
