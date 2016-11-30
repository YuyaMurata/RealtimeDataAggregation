package bench.property;

import bench.main.AgentBenchmark;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BenchProperty {
    private static final String filename ="/agent.properties";
    private static BenchProperty benchProp = new BenchProperty(filename);
    private Map propMap = new HashMap();
    private Properties prop = new Properties();
    
    private BenchProperty(String filename) {
        // TODO 自動生成されたコンストラクター・スタブ
        load(prop, filename);
        setPropertyToMap();
    }
    
    public static BenchProperty getInstance(){
        return benchProp;
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
    
    public Map getParameter(){
        return this.propMap;
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
        propMap.put(AgentBenchmark.paramID.TIME_RUN, Long.valueOf(getValue("time.run")));
        propMap.put(AgentBenchmark.paramID.TIME_PERIOD, Long.valueOf(getValue("time.period")));
        propMap.put(AgentBenchmark.paramID.DATA_VOLUME, Integer.valueOf(getValue("data.volume")));
        
        propMap.put(AgentBenchmark.paramID.AMOUNT_USER, Integer.valueOf(getValue("number.user")));
        propMap.put(AgentBenchmark.paramID.ID_RULE, getValue("user.idrule"));
        
        propMap.put(AgentBenchmark.paramID.AGENT_DEFAULT_VALUE, Integer.valueOf(getValue("agent.value")));
        
        propMap.put(AgentBenchmark.paramID.TYPE_SELECT, Integer.valueOf(getValue("data.type")));
        propMap.put(AgentBenchmark.paramID.DATA_MODE, Integer.valueOf(getValue("data.mode")));
        propMap.put(AgentBenchmark.paramID.DATA_SEED, Long.valueOf(getValue("random.seed")));
        
        //Console Out
        displayProperty();
    }
    
    public Object getParameter(Object id){
        return propMap.get(id);
    }
    
    private void displayProperty(){
        System.out.println("****** -Get "+filename+" Property Value- ******");
        for(Object key: propMap.keySet())
            System.out.println("* Benchmark Parameter : "+key+" = "+propMap.get(key));
        System.out.println("****************************************************");
    }
    
    public static void main(String args[]){
        BenchProperty bprop = BenchProperty.getInstance();
    }
}
