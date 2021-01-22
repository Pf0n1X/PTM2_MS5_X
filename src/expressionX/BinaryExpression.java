package expressionX;

public abstract class BinaryExpression implements Expression {
	
	// Data Members
    protected Expression left;
    protected Expression right;
    
    // Constructors
    public BinaryExpression(Expression left, Expression right)
    {
        this.left = left;
        this.right = right;
    }
    
    // Getters & Setters
    public void setLeft(Expression left)
    {
        this.left = left;
    }

    public void setRight(Expression right)
    {
        this.right = right;
    }
}