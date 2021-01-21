package expressionX;

import interpreterX.DataManager;

public class Number implements Expression {
	
	// Data Members
    private DataManager manager;
    private String value;
    
    // Constructors
    public Number(String value,DataManager manager) {
        this.manager = manager;
        this.value = value;
    }
    
    // Methods
    @Override
    public Double calculate() {
        return this.getValue();
    }

    @Override
    public String toString() {
        return this.value;
    }
    
    // Getters & Setters
    public Double getValue() {
        try {
            return this.manager.getValue(this.value);
        } catch (RuntimeException ignore) {
            return Double.parseDouble(value);
        }
    }
}