package commandX;

import java.util.List;
import interpreterX.DataManager;

public class SleepCommand implements Command {
	
	// Constant Members
    public static final String COMMAND_NAME = "sleep";
    
    // Data Members
    private final DataManager manager;
    
    // Constructors
    public SleepCommand(DataManager manager) {
        this.manager = manager;
    }
    
    // Methods
    @Override
    public int execute(List<String> words)
    {
        try {
            Thread.sleep(Long.parseLong(words.get(1)));
        } catch (InterruptedException ignored) {
        	
        }
        
        return 3;
    }
    
    // Getters & Setters
    @Override
    public String getName()
    {
        return COMMAND_NAME;
    }
}