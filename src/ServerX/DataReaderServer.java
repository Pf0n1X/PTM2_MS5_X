package ServerX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import interpreterX.DataManager;

public class DataReaderServer implements Server {
	
	// Data Members
	// The simulatotr's symbol table.
    private HashMap<String, Double> data = new HashMap<>();
    private boolean stop = false;
    private Thread thread;
    private final DataManager manager;
    
    // Constructors
    public DataReaderServer(DataManager manager) {
        this.manager = manager;
    }
    
    // Methods
    @Override
    public void listen(int port,int timeout) {
        thread = new Thread(() -> {
            try {
                ServerSocket server = new ServerSocket(port,timeout);
                server.setSoTimeout(5000);
                
                while (!stop) {
                    try {
                        Socket aClient = server.accept(); // blocking call
                        try
                        {
                            InputStream clientInputStream = aClient.getInputStream();
                            handleClient(clientInputStream,timeout);
                            clientInputStream.close();
                            aClient.close();
                        } catch (IOException ignored) {
                        	
                        }
                    } catch (SocketTimeoutException ignored) {
                    	
                    }
                }
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        thread.start();
    }

    private void handleClient(InputStream inputStream,int timeout) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = in.readLine()) != null)
        {
            String[] split = line.split(",");
            for (String s : split)
            {
                Double d =Double.parseDouble(s);
                manager.getDefaultValues().add(d);
            }
        }
    }

    @Override
    public void stop () {
        stop = true;
        
        if (thread !=null)
            thread.interrupt();
    }
    
    // Getters & Setters
    @Override
    public Thread getThread() {
        return this.thread;
    }

    @Override
    public Double getValue(String key) {
        return getData().get(key);
    }

    @Override
    public void setValue(String key,double value) {
        getData().put(key,value);
    }

    @Override
    public Map<String, Double> getData() {
        return this.data;
    }
}