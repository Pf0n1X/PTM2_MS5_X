package expressionX;

public class Minus extends BinaryExpression {
	
	// Constructors
    public Minus(Expression left, Expression right) {
        super(left, right);
    }
    
    // Methods
    @Override
    public Double calculate() {
        return left.calculate()-right.calculate();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        if (left != null) {
            builder.append(left.toString());
        }
        
        if (right != null) {
            builder.append(right.toString());
        }
        
        return builder.toString() + "-";
    }
}