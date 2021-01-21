package commandX;

import java.util.List;
import interpreterX.DataManager;

public class DisconnectCommand implements Command
{
    public static final String COMMAND_NAME = "disconnect";
    private final DataManager env;

    public DisconnectCommand(DataManager env)
    {
        this.env = env;
    }

    @Override
    public int execute(List<String> arguments)
    {
        env.getClient().sendLine("bye");
        env.getClient().close();
        return 2;
    }

    @Override
    public String getName() {
        return COMMAND_NAME;
    }
}