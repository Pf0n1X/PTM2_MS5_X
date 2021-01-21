package interpreterX;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

import ServerX.Server;
import clientX.Client;

public class DataManager {
	
	// Data Members
    private volatile Map<String, String> bindingTable = new ConcurrentHashMap<>();
    private volatile Map<String, Double> symbolTable = new ConcurrentHashMap<>();
    private volatile Client client;
    private volatile Server server;
    private volatile Parser parser;
    private volatile int returnValue = 0;
    private volatile Queue<Double> defaultValues =new ConcurrentLinkedQueue<>();
    
    // Constructors
    public DataManager() {
    	
    }
    
    // Getters & Setters
    public Queue<Double> getDefaultValues() {
        return defaultValues;
    }

    public int getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(int returnValue) {
        this.returnValue = returnValue;
    }

    private Double getBind(String bindingTableKey) {
        String path = this.bindingTable.get(bindingTableKey);
        Double value = this.client.getValue(path);
        updateAllBindings(path,value);
        return value;
    }

    private void setBind(String bindingTableKey,Double value) {
        String path = this.bindingTable.get(bindingTableKey);
        this.client.set(path,value);
        updateAllBindings(path,value);
    }

    private Double getSymbol(String symbolTableKey) {
        return this.symbolTable.get(symbolTableKey);
    }

    private void setSymbol(String symbolTableKey,Double value) {
        this.symbolTable.put(symbolTableKey,value);
    }

    public void addBinding(String bindingTableKey,String path) {
        this.bindingTable.put(bindingTableKey,path);
        
        try {
            setValue(bindingTableKey, this.getDefaultValues().remove());
        } catch (NoSuchElementException e) {
        	
        }
    }

    public void addSymbol(String symboTableKey,Double value) {
        this.symbolTable.put(symboTableKey,value);
    }

    public Double getValue(String key) {
        if (bindingTable.containsKey(key) && !client.isClosed())
            return this.getBind(key);
        else
            if (symbolTable.containsKey(key))
                return this.getSymbol(key);
            throw new RuntimeException("key not found in tables for key="+key);
    }

    public void setValue(String key,Double value) {
        if (bindingTable.containsKey(key) && !client.isClosed())
            this.setBind(key,value);
        else
            if (symbolTable.containsKey(key))
                this.setSymbol(key,value);
            else
                this.addSymbol(key,value);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }
    
    // Methods
    public void closeAll() {
        defaultValues.clear();
        symbolTable.clear();
        bindingTable.clear();
        getClient().close();
        getServer().stop();
    }
    
    private void updateAllBindings(String path,Double value) {
        Stream<Map.Entry<String, String>> entryStream = this.bindingTable.entrySet().stream()
                .filter(stringStringEntry -> stringStringEntry.getValue().equals(path));
        entryStream.forEach(stringStringEntry -> {
                    setSymbol(stringStringEntry.getKey(), value);
                });
    }
}