package apps.ranking;

import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.impl.HPAEntity;
import com.ibm.agent.exa.TxID;
import com.ibm.agent.exa.entity.EntitySet;
import com.ibm.agent.exa.entity.EntityKey;
import com.ibm.agent.exa.entity.Entity;
import java.util.Iterator;

/**
 * Generated code for rankagent.
 *
 * <p>entity type="rankagent tablename="rankagent <br>
 * attribute name="RankID" type="STRING" primarykey="true" <br>
 * attribute name="RankTable" type="ranktable" <br>
 * attribute name="TotalUsers" type="LONG" <br>
 * attribute name="ConnectionCount" type="LONG" <br>
 * attribute name="MessageLatency" type="LONG" <br>
**/
public class Rankagent extends HPAEntity {
    /**
    * Primary key size
    **/
    public static final int PKINDEXSIZE = 1;

    /**
    * Primary key index of RankID
    **/
    public static final int PKINDEX_RANKID = 0;

    /**
    * Column index of RankID
    **/
    public static final int RANKID = 0;

    /**
    * Column index of RankTable
    **/
    public static final int RANKTABLE = 1;

    /**
    * Column index of TotalUsers
    **/
    public static final int TOTALUSERS = 2;

    /**
    * Column index of ConnectionCount
    **/
    public static final int CONNECTIONCOUNT = 3;

    /**
    * Column index of MessageLatency
    **/
    public static final int MESSAGELATENCY = 4;

    /**
     * This constructor is used by the runtime.
     * An application should not create an instance with this constructor
    **/
    public Rankagent() {
        super();
    }

    /**
     * Get the version string
    **/
    public String getVersion() {
        return "rda1.0";
    }

    /**
     * Get a value of RankID. 
     * The setter method of RankID is not generated because this attribute is a primarykey. 
     * @return RankID
     **/
    public final String getRankID(TxID tx) {
        // generated code
        return getString(tx,0);
    }

    /**
     * Get a set of RankTable. 
     * Entity type of this entity set is ranktable.
     * The setter method of RankTable is not generated because this attribute is a EntitySet. 
     * @return an entity set containing Ranktable
     * @throws AgentException
     **/
    public final EntitySet getRankTable(TxID tx) throws AgentException {
        // generated code
        return getEntitySet(tx,1);
    }

    /**
     * Get a value of RankTable. 
     * @param tx a transaction context
     * @param UserID
     * @return Ranktable
     * @throws AgentException
     **/
    public final Ranktable getRankTable(TxID tx,String UserID) throws AgentException {
        // generated code
        EntitySet es = getEntitySet(tx,1);
        Entity parent = es.getParent();
        Object[] primaryKeys = new Object[]{parent.getObject(tx,0),UserID};
        EntityKey ek = new EntityKey("ranktable", primaryKeys);
        Ranktable entity = (Ranktable)es.getEntity(ek);
        return entity;
    }

    /**
     * Create a value of Ranktable. 
     * @param tx a transaction context
     * @param UserID
     * @return Ranktable
     **/
    public final Ranktable createRankTable(TxID tx,String UserID) throws AgentException {
        // generated code
        if (UserID.length() > 32) {
            throw new AgentException("UserID > maxlength(32)");
        }
        EntitySet es = getEntitySet(tx,1);
        Object[] primaryKeys = new Object[]{null,UserID};
        Ranktable entity = (Ranktable)es.createEntity(tx,primaryKeys);
        return entity;
    }

    /**
     * Get an iterator of Ranktable. 
     * @param tx a transaction context
     **/
    public final Iterator<Entity> getRankTableIterator(TxID tx) throws AgentException {
        // generated code
        EntitySet es = getEntitySet(tx,1);
        return es.iterator(tx);
    }

    /**
     * @return TotalUsers
     **/
    public final long getTotalUsers(TxID tx) {
        // generated code
        return getLong(tx,2);
    }

    /**
     * Set a value to TotalUsers. 
     * @param tx a transaction context
     * @param value a value to be set to TotalUsers
     **/
    public final void  setTotalUsers(TxID tx, long value) throws AgentException {
        // generated code
        setLong(tx,2,value);
    }

    /**
     * @return ConnectionCount
     **/
    public final long getConnectionCount(TxID tx) {
        // generated code
        return getLong(tx,3);
    }

    /**
     * Set a value to ConnectionCount. 
     * @param tx a transaction context
     * @param value a value to be set to ConnectionCount
     **/
    public final void  setConnectionCount(TxID tx, long value) throws AgentException {
        // generated code
        setLong(tx,3,value);
    }

    /**
     * @return MessageLatency
     **/
    public final long getMessageLatency(TxID tx) {
        // generated code
        return getLong(tx,4);
    }

    /**
     * Set a value to MessageLatency. 
     * @param tx a transaction context
     * @param value a value to be set to MessageLatency
     **/
    public final void  setMessageLatency(TxID tx, long value) throws AgentException {
        // generated code
        setLong(tx,4,value);
    }

}
