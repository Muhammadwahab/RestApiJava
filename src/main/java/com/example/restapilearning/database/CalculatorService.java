package com.example.restapilearning.database;

import javax.ejb.Stateless;

@Stateless
public class CalculatorService {

    public int add(int a, int b) {
        return a + b;
    }
}
