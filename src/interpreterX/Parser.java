package interpreterX;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import commandX.Command;
import commandX.ConnectToClientCommand;
import commandX.DisconnectCommand;
import commandX.OpenDataServerCommand;
import commandX.PrintCommand;
import commandX.ReturnCommand;
import commandX.SleepCommand;
import commandX.DeclareVarCommand;
import commandX.WhileCommand;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class Parser {
	
	// Data Members
    private final Map<String, Command> commands;
    private final DataManager manager;
    private volatile boolean isStop;
    Thread parserThread = null;
    volatile List<String> tokens;
    
    // Constructors
    public Parser(DataManager manager) {
        isStop=true;
        this.manager = manager;

        commands = Stream.of(
                new PrintCommand(manager),
                new ConnectToClientCommand(manager),
                new OpenDataServerCommand(manager),
                new DeclareVarCommand(manager),
                new SleepCommand(manager),
                new WhileCommand(manager),
                new ReturnCommand(manager),
                new DisconnectCommand(manager))
        		.collect(toMap(Command::getName, identity()));
    }
    
    // Methods
    public void threadParse(final List<String> wordsfinal) {
    	
    	// If the parser thread already exists, stop it.
        if (parserThread!=null){
            try {
                parserThread.stop();
            }catch (SecurityException ignored){}
        }
        
        // Otherwise, initialize it.
        parserThread=new Thread(() -> {
            manager.closeAll();
            parse(wordsfinal);
        });
        
        // If the parser is not in "stop mode", start it.
        if (!isStop)
            parserThread.start();
    }

    public void parse(List<String> tokens) {
    	
    	// Loop through all the input and execute the commands
    	// while calculating the index of the next command.
    	while (!tokens.isEmpty() && !isStop) {
            Command command = commands.getOrDefault(tokens.get(0), commands.get(DeclareVarCommand.COMMAND_NAME));
            
            // execute the command while retrieving the index of the next one.
            int numOfArgs = command.execute(tokens);
            
            // Remove the command that has already been executed from the list.
            tokens = tokens.subList(numOfArgs, tokens.size());
        }
    }

    public void addCommand(Command cmd) {
        commands.put(cmd.getName(), cmd);
    }

    public void stop() {
        isStop=true;
        if (parserThread!=null && parserThread.isAlive()) {
                parserThread.suspend();
        }
    }

    public void Resume() {
        this.isStop = false;
        
        if (parserThread != null) {
            try{
                parserThread.start();
            } catch (IllegalThreadStateException e){
                try{
                    parserThread.resume();
                }catch (SecurityException e1){
                    e1.printStackTrace();
                }
            }
        }
    }
    
    public boolean isStop() {
        return isStop;
    }
}
