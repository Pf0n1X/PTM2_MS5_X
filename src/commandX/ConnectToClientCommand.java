package commandX;

import java.util.List;
import clientX.SimpleClient;
import interpreterX.DataManager;

public class ConnectToClientCommand implements Command {
	// Constant Members
    public static final String COMMAND_NAME = "connect";
    
    // Data Members
    private final DataManager manager;
    
    // Constructors
    public ConnectToClientCommand(DataManager manager) {
        this.manager = manager;
    }
    
    // Methods
    @Override
    public int execute(List<String> arguments) {
        manager.setClient(new SimpleClient());
        manager.getClient().connect(arguments.get(1),Integer.parseInt(arguments.get(2)));
        return 4;
    }
    
    // Getters & Setters
    @Override
    public String getName() {
        return COMMAND_NAME;
    }
}