package com.example.restapilearning.database;

import org.jboss.weld.context.ejb.Ejb;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;

@Stateless
@RequestScoped
public class CalculatorService {

    public int add(int a, int b) {
        return a + b;
    }
}
