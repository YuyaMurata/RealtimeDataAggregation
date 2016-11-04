package rda;

import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.impl.HPAEntity;
import com.ibm.agent.exa.TxID;

/**
 * Generated code for aggregateagent.
 *
 * <p>entity type="aggregateagent tablename="aggregateagent <br>
 * attribute name="AgentID" type="STRING" primarykey="true" <br>
 * attribute name="Data" type="LONG" <br>
 * attribute name="ConnectionCount" type="LONG" <br>
 * attribute name="MessageLatency" type="LONG" <br>
 * attribute name="MessageQueueLength" type="LONG" <br>
**/
public class Aggregateagent extends HPAEntity {
    /**
    * Primary key size
    **/
    public static final int PKINDEXSIZE = 1;

    /**
    * Primary key index of AgentID
    **/
    public static final int PKINDEX_AGENTID = 0;

    /**
    * Column index of AgentID
    **/
    public static final int AGENTID = 0;

    /**
    * Column index of Data
    **/
    public static final int DATA = 1;

    /**
    * Column index of ConnectionCount
    **/
    public static final int CONNECTIONCOUNT = 2;

    /**
    * Column index of MessageLatency
    **/
    public static final int MESSAGELATENCY = 3;

    /**
    * Column index of MessageQueueLength
    **/
    public static final int MESSAGEQUEUELENGTH = 4;

    /**
     * This constructor is used by the runtime.
     * An application should not create an instance with this constructor
    **/
    public Aggregateagent() {
        super();
    }

    /**
     * Get the version string
    **/
    public String getVersion() {
        return "rda1.0";
    }

    /**
     * Get a value of AgentID. 
     * The setter method of AgentID is not generated because this attribute is a primarykey. 
     * @return AgentID
     **/
    public final String getAgentID(TxID tx) {
        // generated code
        return getString(tx,0);
    }

    /**
     * @return Data
     **/
    public final long getData(TxID tx) {
        // generated code
        return getLong(tx,1);
    }

    /**
     * Set a value to Data. 
     * @param tx a transaction context
     * @param value a value to be set to Data
     **/
    public final void  setData(TxID tx, long value) throws AgentException {
        // generated code
        setLong(tx,1,value);
    }

    /**
     * @return ConnectionCount
     **/
    public final long getConnectionCount(TxID tx) {
        // generated code
        return getLong(tx,2);
    }

    /**
     * Set a value to ConnectionCount. 
     * @param tx a transaction context
     * @param value a value to be set to ConnectionCount
     **/
    public final void  setConnectionCount(TxID tx, long value) throws AgentException {
        // generated code
        setLong(tx,2,value);
    }

    /**
     * @return MessageLatency
     **/
    public final long getMessageLatency(TxID tx) {
        // generated code
        return getLong(tx,3);
    }

    /**
     * Set a value to MessageLatency. 
     * @param tx a transaction context
     * @param value a value to be set to MessageLatency
     **/
    public final void  setMessageLatency(TxID tx, long value) throws AgentException {
        // generated code
        setLong(tx,3,value);
    }

    /**
     * @return MessageQueueLength
     **/
    public final long getMessageQueueLength(TxID tx) {
        // generated code
        return getLong(tx,4);
    }

    /**
     * Set a value to MessageQueueLength. 
     * @param tx a transaction context
     * @param value a value to be set to MessageQueueLength
     **/
    public final void  setMessageQueueLength(TxID tx, long value) throws AgentException {
        // generated code
        setLong(tx,4,value);
    }

}
