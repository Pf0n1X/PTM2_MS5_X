package commandX;

import java.util.List;
import interpreterX.DataManager;

public class PrintCommand implements Command {
	
	// Constant Members
    public static final String COMMAND_NAME = "print";
    
    // Data Members
    private final DataManager manager;
    
    // Methods
    public PrintCommand(DataManager manager) {
        this.manager = manager;
    }

    @Override
    public int execute(List<String> arguments)
    {
        String word = arguments.get(1);
        if (word.startsWith("\"") && word.endsWith("\""))
            word =word.substring(word.indexOf("\"")+1,word.lastIndexOf("\""));
        else
            word = manager.getValue(word)+"";
        System.out.println(word);
        return 3;
    }
    
    // Getters & Setters
    @Override
    public String getName() {
        return COMMAND_NAME;
    }
}