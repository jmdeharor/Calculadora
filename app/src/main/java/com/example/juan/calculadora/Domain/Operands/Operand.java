package com.example.juan.calculadora.Domain.Operands;

import com.example.juan.calculadora.Domain.DataStructures.Stack;

public abstract class Operand extends Component {
    int priority;
    public abstract double operate(double left, double right);

    @Override
    public void execute(Stack<Double> numStack, Stack<Component> componentStack) {

    }

    @Override
    public boolean isCompatibleWith(Component rightComponent) {
        return (rightComponent instanceof MyNumber) || (rightComponent instanceof OpenParenthesis);
    }

    public int getPriority() {
        return priority;
    }
}
