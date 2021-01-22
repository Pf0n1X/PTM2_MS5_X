package conditionX;

import java.util.List;
import java.util.function.Predicate;

import interpreterX.DataManager;
import interpreterX.Lexer;
import interpreterX.ShuntingYard;

public class Condition {
	
	// Data Members
    Predicate<Void> stringPredicate;
    
    // Constructors
    public Condition(DataManager environment, List<String> arguments)
    {
        List<String> stringList = arguments.subList(1, arguments.indexOf(Lexer.EOL)-1 );
        int indexsplit;
        
        if ((indexsplit = stringList.indexOf(">="))!=-1) {
            int finalIndexsplit = indexsplit;
            
            stringPredicate = s -> {
                List<String> leftOp = stringList.subList(0, finalIndexsplit );
                List<String> rightOp = stringList.subList(finalIndexsplit + 1, stringList.size());
                
                return ShuntingYard.calc(String.join("", leftOp), environment) >= ShuntingYard.calc(String.join("", rightOp), environment);
            };
        } else if ((indexsplit = stringList.indexOf("<=")) != -1) {
            int finalIndexsplit = indexsplit;
            
            stringPredicate = s -> {
                List<String> leftOp = stringList.subList(0, finalIndexsplit );
                List<String> rightOp = stringList.subList(finalIndexsplit + 1, stringList.size());
                
                return ShuntingYard.calc(String.join("", leftOp), environment) <= ShuntingYard.calc(String.join("", rightOp), environment);
            };
        } else if ((indexsplit = stringList.indexOf("=="))!=-1) {
            int finalIndexsplit = indexsplit;
            
            stringPredicate = s -> {
                List<String> leftOp = stringList.subList(0, finalIndexsplit );
                List<String> rightOp = stringList.subList(finalIndexsplit + 1, stringList.size());
                
                return ShuntingYard.calc(String.join("", leftOp), environment) == ShuntingYard.calc(String.join("", rightOp), environment);
            };
        } else if ((indexsplit = stringList.indexOf("!=")) != -1) {
            int finalIndexsplit = indexsplit;
            
            stringPredicate = s -> {
                List<String> leftOp = stringList.subList(0, finalIndexsplit );
                List<String> rightOp = stringList.subList(finalIndexsplit + 1, stringList.size());
                
                return ShuntingYard.calc(String.join("", leftOp), environment) != ShuntingYard.calc(String.join("", rightOp), environment);
            };
        } else if ((indexsplit = stringList.indexOf(">"))!=-1) {
            int finalIndexsplit = indexsplit;
            
            stringPredicate = s -> {
                List<String> leftOp = stringList.subList(0, finalIndexsplit );
                List<String> rightOp = stringList.subList(finalIndexsplit + 1, stringList.size());
                
                return ShuntingYard.calc(String.join("", leftOp), environment) > ShuntingYard.calc(String.join("", rightOp), environment);
            };
        } else if ((indexsplit = stringList.indexOf("<"))!=-1) {
            int finalIndexsplit = indexsplit;
            
            stringPredicate = s -> {
                List<String> leftOp = stringList.subList(0, finalIndexsplit );
                List<String> rightOp = stringList.subList(finalIndexsplit + 1, stringList.size());
                
                return ShuntingYard.calc(String.join("", leftOp), environment) < ShuntingYard.calc(String.join("", rightOp), environment);
            };
        } else {
            throw new RuntimeException("no operators founds");
        }
    }
    
    // Methods
    public boolean evaluate() {
        return stringPredicate.test(null);
    }
}
