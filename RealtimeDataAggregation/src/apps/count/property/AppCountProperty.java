package apps.count.property;

import apps.count.appuser.UserProfile;
import apps.count.manager.AggregateAgentManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AppCountProperty {
    private static final String filename ="/agent.properties";
    private static AppCountProperty rdaProp = new AppCountProperty(filename);
    private Map propMap = new HashMap();
    private Properties prop = new Properties();
    
    private AppCountProperty(String filename) {
        // TODO 自動生成されたコンストラクター・スタブ
        load(prop, filename);
        setPropertyToMap();
    }
    
    public static AppCountProperty getInstance(){
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
        //User Profile Parameter (Define UserList in Benchmark Main Program)
        propMap.put(UserProfile.paramID.USER_MODE, Integer.valueOf(getValue("user.mode")));
        propMap.put(UserProfile.paramID.USER_SEED, Long.valueOf(getValue("random.seed")));
        
        //Agent Profile Parameter
        propMap.put(AggregateAgentManager.paramID.AMOUNT_AGENTS, Integer.valueOf(getValue("number.agent")));
        propMap.put(AggregateAgentManager.paramID.ID_RULE, getValue("agent.idrule"));
        
        displayProperty();
    }
    
    public Object getParameter(Object id){
        return propMap.get(id);
    }
    
    private void displayProperty(){
        System.out.println("****** -Get "+filename+" Property Value- ******");
        for(Object key: propMap.keySet())
            System.out.println("* Application Parameter : "+key+" = "+propMap.get(key));
        System.out.println("****************************************************");
    }
    
    public static void main(String args[]){
        AppCountProperty prop = AppCountProperty.getInstance();
    }
}
