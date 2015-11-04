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

import java.util.Objects;

/**
 * This class is a POJO class that represents a payment record.
 * @author Jaromir Mlejnek
 */
public class Payment {
    
    /**
     * Currency code
     */
    private String currencyCode;
    
    /**
     * Currency amount
     */
    private double amount;

    public Payment(String currencyCode, double amount) {
        this.currencyCode = currencyCode;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        return String.format("%s %.0f", this.currencyCode, this.amount);
    } 

    @Override
    public boolean equals(Object obj) {        
        if (obj instanceof Payment) {
            Payment pObj = (Payment) obj;
            
            boolean isCodeEq = (this.getCurrencyCode() == null  && pObj.getCurrencyCode() == null)
                    || (this.getCurrencyCode() != null && this.getCurrencyCode().equals(pObj.getCurrencyCode()));
            
            return isCodeEq && (this.getAmount() == pObj.getAmount());
        }
        
        return super.equals(obj);
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.currencyCode);
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.amount) ^ (Double.doubleToLongBits(this.amount) >>> 32));
        return hash;
    }
    
}
