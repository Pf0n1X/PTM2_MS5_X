package ServerX;

import java.util.Map;

public interface Server
{
    void listen(int port,int timeout);
    void stop();
    Thread getThread();
    Double getValue(String key);
    void setValue(String key, double value);
    Map<String, Double> getData();
}