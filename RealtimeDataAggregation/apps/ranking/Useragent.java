package apps.ranking;

import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.impl.HPAEntity;
import com.ibm.agent.exa.TxID;
import com.ibm.agent.exa.entity.EntitySet;
import com.ibm.agent.exa.entity.EntityKey;

/**
 * Generated code for useragent.
 *
 * <p>entity type="useragent tablename="useragent <br>
 * attribute name="UserID" type="STRING" primarykey="true" <br>
 * attribute name="Profile" type="profile" <br>
 * attribute name="Data" type="LONG" <br>
 * attribute name="CommunicationID" type="STRING" <br>
 * attribute name="ConnectionCount" type="LONG" <br>
 * attribute name="MessageLatency" type="LONG" <br>
**/
public class Useragent extends HPAEntity {
    /**
    * Primary key size
    **/
    public static final int PKINDEXSIZE = 1;

    /**
    * Primary key index of UserID
    **/
    public static final int PKINDEX_USERID = 0;

    /**
    * Column index of UserID
    **/
    public static final int USERID = 0;

    /**
    * Column index of Profile
    **/
    public static final int PROFILE = 1;

    /**
    * Column index of Data
    **/
    public static final int DATA = 2;

    /**
    * Column index of CommunicationID
    **/
    public static final int COMMUNICATIONID = 3;

    /**
    * Column index of ConnectionCount
    **/
    public static final int CONNECTIONCOUNT = 4;

    /**
    * Column index of MessageLatency
    **/
    public static final int MESSAGELATENCY = 5;

    /**
     * This constructor is used by the runtime.
     * An application should not create an instance with this constructor
    **/
    public Useragent() {
        super();
    }

    /**
     * Get the version string
    **/
    public String getVersion() {
        return "rda1.0";
    }

    /**
     * Get a value of UserID. 
     * The setter method of UserID is not generated because this attribute is a primarykey. 
     * @return UserID
     **/
    public final String getUserID(TxID tx) {
        // generated code
        return getString(tx,0);
    }

    /**
     * Get a set of Profile. 
     * Entity type of this entity set is profile.
     * A returned entity set has a single entity.
     * The setter method of Profile is not generated because this attribute is a EntitySet. 
     * @return an entity set containing Profile
     * @throws AgentException
     **/
    public final EntitySet getProfileSet(TxID tx) throws AgentException {
        // generated code
        return getEntitySet(tx,1);
    }

    /**
     * Get a value of Profile. 
     * @param tx a transaction context
     * @return Profile
     * @throws AgentException
     **/
    public final Profile getProfile(TxID tx) throws AgentException {
        // generated code
        EntitySet es = getEntitySet(tx,1);
        if (es == null) return null;
        return (Profile)es.getSingleEntity();
    }

    /**
     * Create a value of Profile. 
     * @param tx a transaction context
     * @return Profile
     **/
    public final Profile createProfile(TxID tx) throws AgentException {
        // generated code
        EntitySet es = getEntitySet(tx,1);
        Profile entity = (Profile)es.createEntity(tx,new Object[1]);
        return entity;
    }

    /**
     * @return Data
     **/
    public final long getData(TxID tx) {
        // generated code
        return getLong(tx,2);
    }

    /**
     * Set a value to Data. 
     * @param tx a transaction context
     * @param value a value to be set to Data
     **/
    public final void  setData(TxID tx, long value) throws AgentException {
        // generated code
        setLong(tx,2,value);
    }

    /**
     * @return CommunicationID
     **/
    public final String getCommunicationID(TxID tx) {
        // generated code
        return getString(tx,3);
    }

    /**
     * Set a value to CommunicationID. 
     * @param tx a transaction context
     * @param value a value to be set to CommunicationID
     **/
    public final void  setCommunicationID(TxID tx, String value) throws AgentException {
        // generated code
        if (value != null && value.length() > 32) {
            throw new AgentException("CommunicationID > maxlength(32)");
        }
        setString(tx,3,value);
    }

    /**
     * @return ConnectionCount
     **/
    public final long getConnectionCount(TxID tx) {
        // generated code
        return getLong(tx,4);
    }

    /**
     * Set a value to ConnectionCount. 
     * @param tx a transaction context
     * @param value a value to be set to ConnectionCount
     **/
    public final void  setConnectionCount(TxID tx, long value) throws AgentException {
        // generated code
        setLong(tx,4,value);
    }

    /**
     * @return MessageLatency
     **/
    public final long getMessageLatency(TxID tx) {
        // generated code
        return getLong(tx,5);
    }

    /**
     * Set a value to MessageLatency. 
     * @param tx a transaction context
     * @param value a value to be set to MessageLatency
     **/
    public final void  setMessageLatency(TxID tx, long value) throws AgentException {
        // generated code
        setLong(tx,5,value);
    }

}
