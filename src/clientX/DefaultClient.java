package clientX;

import java.io.*;
import java.net.Socket;

public class DefaultClient implements Client {
	
	// Data Members
    private Socket socket;
    private BufferedReader in;
    private PrintWriter printWriter;
    
    // Methods
    @Override
    public void connect(String ip, int port) {
    	
    	// Run the client in a separate thread in order to not stop the GUI from running.
        try {
        	
        	// Initialize the socket.
            socket = new Socket(ip, port);
            Thread.sleep(1000);
            
            // Get the socket's streams.
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            in = new BufferedReader(new InputStreamReader(inputStream));
            printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void set(String path, Double value) {
    	
    	// Send a set request to the simulator's server.
        this.sendLine("set " + path + " " + value);
    }

    @Override
    public Double getValue(String path) {
    	String substring;
    	
    	// Send a request for retrieving the data from the server.
        this.sendLine("get " + path);
        
        try {
            String s = null;
            
            // Wait for a response.
            while (!(s = in.readLine()).contains(path));
            
            // Retrieve the response.
            substring = s.substring(s.indexOf("'")+1, s.lastIndexOf("'"));
            
            // Parse the response to a double type.
            return Double.parseDouble(!substring.isEmpty() ? substring : "0");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public void sendLine(String line) {
    	
    	// Send the "line" argument to the server.
        printWriter.println(line);
        printWriter.flush();
    }

    @Override
    public void close() {
    	
    	// Attempt closing the server.
        try {
            in.close();
            printWriter.close();
            socket.close();
        } catch (IOException|NullPointerException e) {
        	
        }
    }

    @Override
    public boolean isClosed() {
    	
    	// Check if the server is closed and return a response according to it.
        if (socket == null) {
        	return true;
    	}
    
        return socket.isClosed();
    }
}