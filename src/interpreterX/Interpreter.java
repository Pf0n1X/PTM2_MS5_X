package interpreterX;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import ServerX.DataReaderServer;
import clientX.DefaultClient;

public class Interpreter {
	
	// Data Members
    private final DataManager manager = new DataManager();
    
    // Constructors
    public Interpreter () {
        Parser parser = new Parser(manager);
        manager.setParser(parser);
        manager.setServer(new DataReaderServer(manager));
        manager.setClient(new DefaultClient());
    }
    
    // Methods
    public void runSimulator(String line){
        InputStream is = new ByteArrayInputStream(line.getBytes());
        List<String> lexer = Lexer.Lexer(new InputStreamReader(is));
        this.manager.getParser().threadParse(lexer);
    }

    public void runSimulator(String[] lines) {
        String text = String.join(System.lineSeparator(), lines);
        runSimulator(text);
    }

    public void setAutoPilot(boolean autoPilot){
        if (autoPilot) {
            this.manager.getParser().Resume();
        } else if (!autoPilot) {
            this.manager.getParser().stop();
        }
    }
    
    // Getters & Setters
    public DataManager getManager() {
        return this.manager;
    }
}
