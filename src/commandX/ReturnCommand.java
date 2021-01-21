package commandX;

import java.util.List;
import interpreterX.DataManager;
import interpreterX.Lexer;
import interpreterX.ShuntingYard;

public class ReturnCommand implements Command {
	
	// Constant Members
    public static final String COMMAND_NAME = "return";
    
    // Data Members
    private final DataManager manager;
    
    // Constructors
    public ReturnCommand(DataManager manager) {
        this.manager = manager;
    }
    
    // Methods
    @Override
    public int execute(List<String> arguments) {
        List<String> stringList = arguments.subList(1, arguments.indexOf(Lexer.EOL) );
        String collect = String.join("", stringList);
        Double value = ShuntingYard.calc(collect, manager);
        manager.setReturnValue((int)Math.floor(value));
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        	
        }
        
        return arguments.indexOf(Lexer.EOL)+1;
    }
    
    // Getters & Setters
    @Override
    public String getName() {
        return COMMAND_NAME;
    }
}