package commandX;

import java.util.List;

import commandX.Command;
import interpreterX.DataManager;
import interpreterX.Lexer;
import interpreterX.ShuntingYard;

public class DeclareVarCommand implements Command {
	
	// Constant Members
    public static final String COMMAND_NAME = "var";
    
    // Data Members
    private final DataManager manager;
    
    // Constructors
    public DeclareVarCommand(DataManager manager) {
        this.manager = manager;
    }
    
    // Methods
    @Override
    public int execute(List<String> arguments) {
        int tokenIndex = 0;
        
        // Try finding the the index of the next command:
        // advance one step when the "var" key word is found.
        if(arguments.get(0).equals("var")) {
            tokenIndex++;
        }
        
        // In case an End Of Line appears advance two.
        if (arguments.get(1 + tokenIndex).equals(Lexer.EOL)) {
            return tokenIndex + 2;
        }
        
        // When the "bind" key word appears find it's path and add
        // a binding entry.
        if (arguments.get(2 + tokenIndex).equals("bind")) {
        	
        	// Get the key and the path(IE the local variable name and the simulator's variable path)
            String key = arguments.get(0 + tokenIndex);
            String path = arguments.get(3 + tokenIndex);

            if (path.startsWith("\"") && path.endsWith("\""))
                path =path.substring(path.indexOf("\"")+1,path.lastIndexOf("\""));
            manager.addBinding(key,path);
            
            return 5 + tokenIndex;
        } else {
	        String key =arguments.get(0 + tokenIndex);
	        List<String> stringList = arguments.subList(2 + tokenIndex, arguments.indexOf(Lexer.EOL) );
	        String collect = String.join("", stringList);
	        Double value = ShuntingYard.calc(collect, manager);
	        manager.setValue(key,value);
	                
	        return 3 + tokenIndex + stringList.size();
        }
    }
    
    // Getters & Setters
    @Override
    public String getName() {
        return COMMAND_NAME;
    }
}