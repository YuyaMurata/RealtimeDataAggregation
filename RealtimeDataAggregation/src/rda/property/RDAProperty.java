package rda.property;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import rda.agent.client.AgentConnection;
import rda.agent.mq.AgentMessageQueue;
import rda.control.flow.WindowController;
import rda.server.ServerConnectionManager;

public class RDAProperty {
    private static final String filename ="/agent.properties";
    private static RDAProperty rdaProp = new RDAProperty(filename);
    private Map propMap = new HashMap();
    private Properties prop = new Properties();
    
    private RDAProperty(String filename) {
        // TODO 自動生成されたコンストラクター・スタブ
        load(prop, filename);
        setPropertyToMap();
    }
    
    public static RDAProperty getInstance(){
        return rdaProp;
    }
    
    //Load Property
    private void load(Properties prop, String filename){
        System.out.println("* Load " + filename);

        try{
            prop.load(getClass().getResourceAsStream(filename));
        }catch(IOException e){
        }
    }

    //Get Value
    public String getValue(String key){
        return prop.getProperty(key);
    }
    
    public void setValue(String key, String value){
        prop.setProperty(key, value);
    }
    
    public void storePropeties(){
        try {
            
            String path = getClass().getResource(filename).getPath();
            prop.store(new FileOutputStream(path), null);
        } catch (IOException e) {
        }
    }
    
    private void setPropertyToMap(){
        //Server Parameter
        propMap.put(ServerConnectionManager.paramID.AMOUNT_SERVERS, Integer.valueOf(getValue("number.server")));
        propMap.put(ServerConnectionManager.paramID.AMOUNT_REGIONS, Integer.valueOf(getValue("number.region")));
        propMap.put(ServerConnectionManager.paramID.HOSTNAME_RULE, getValue("server.namerule"));
        propMap.put(ServerConnectionManager.paramID.SERVER_PORT, Integer.valueOf(getValue("server.port")));
        
        //Application Class
        propMap.put(ServerConnectionManager.paramID.APP_CLASS, getValue("app.class"));
        
        //AgentClient Parameter
        propMap.put(AgentConnection.paramID.POOL_SIZE, Integer.valueOf(getValue("pool.size")));
        
        //MessageQueue Parameter
        propMap.put(AgentMessageQueue.paramID.AGENT_WAIT, Long.valueOf(getValue("agent.wait")));
        propMap.put(AgentMessageQueue.paramID.QUEUE_WAIT, Long.valueOf(getValue("queue.wait")));
        propMap.put(AgentMessageQueue.paramID.QUEUE_LENGTH, Integer.valueOf(getValue("queue.length")));
        
        //Window Contoroller Parameter
        propMap.put(WindowController.paramID.WINDOW_SIZE, Integer.valueOf(getValue("window.size")));
        propMap.put(WindowController.paramID.WINDOW_TIME, Long.valueOf(getValue("window.time")));
        
        //Console Out
        displayProperty();
    }
    
    public Map getAllParameter(){
        return propMap;
    }
    
    public Object getParameter(Object id){
        return propMap.get(id);
    }
    
    public Map getWindowParameter(){
        Map map = new HashMap();
        map.put(WindowController.paramID.WINDOW_SIZE, (Integer)getParameter(WindowController.paramID.WINDOW_SIZE));
        map.put(WindowController.paramID.WINDOW_TIME, (Long)getParameter(WindowController.paramID.WINDOW_TIME));
        return map;
    }
    
    private void displayProperty(){
        System.out.println("****** -Get "+filename+" Property Value- ******");
        for(Object key: propMap.keySet())
            System.out.println("* RDA Parameter : "+key+" = "+propMap.get(key));
        System.out.println("****************************************************");
    }
    
    public static void main(String args[]){
        RDAProperty prop = RDAProperty.getInstance();
    }
}
