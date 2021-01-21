package commandX;

import java.util.List;
import conditionX.Condition;
import interpreterX.DataManager;
import interpreterX.Lexer;

public class WhileCommand implements Command {
	
	// Constant Members
    public static final String COMMAND_NAME = "while";
    
    // Data Members
    private final DataManager manager;
    
    // Constructors
    public WhileCommand(DataManager manager) {
        this.manager = manager;
    }
    
    // Methods
    @Override
    public int execute(List<String> args) {
        Condition condition = new Condition(manager, args);

        int condsize = args.indexOf("{");
        List<String> block = args.subList(args.indexOf(Lexer.EOL)+1, args.indexOf("}"));
        while (condition.evaluate()) {
            manager.getParser().parse(block);
        }

        return 1 + condsize + 1 + block.size() + 1+1;// while cond { \n block } \n
    }

    private int findEndOfBlock(List<String> args) {
        return args.indexOf("}"); //TODO: check inner loop in loop
    }
    
    // Getters & Setters
    @Override
    public String getName() {
        return COMMAND_NAME;
    }
}