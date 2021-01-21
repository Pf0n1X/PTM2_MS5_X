package commandX;

import java.util.List;
import interpreterX.DataManager;

import static java.lang.Integer.parseInt;

public class OpenDataServerCommand implements Command {
	
	// Constant Members
    public static final String COMMAND_NAME = "openDataServer";
    
    // Data Members
    private final DataManager env;
    
    // Constructors
    public OpenDataServerCommand(DataManager env) {
        this.env = env;
    }
    
    // Methods
    @Override
    public int execute(List<String> args)
    {
        env.getServer().listen(parseInt(args.get(1)), parseInt(args.get(2)));
        return 4;
    }
    
    // Getters & Setters
    @Override
    public String getName()
    {
        return COMMAND_NAME;
    }
}