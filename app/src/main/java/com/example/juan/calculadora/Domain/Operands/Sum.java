package com.example.juan.calculadora.Domain.Operands;

public class Sum extends Operand {

    public Sum() {
        priority = 0;
    }

    @Override
    public double operate(double left, double right) {
        return left + right;
    }
}
