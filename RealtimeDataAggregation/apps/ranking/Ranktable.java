package apps.ranking;

import com.ibm.agent.exa.AgentException;
import com.ibm.agent.exa.impl.HPAEntity;
import com.ibm.agent.exa.TxID;

/**
 * Generated code for ranktable.
 *
 * <p>entity type="ranktable tablename="ranktable <br>
 * attribute name="RankID" type="STRING" primarykey="true" relationto="RankID" <br>
 * attribute name="UserID" type="STRING" primarykey="true" <br>
 * attribute name="Rank" type="LONG" <br>
 * attribute name="Data" type="LONG" <br>
 * attribute name="CurrentTime" type="LONG" <br>
**/
public class Ranktable extends HPAEntity {
    /**
    * Primary key size
    **/
    public static final int PKINDEXSIZE = 2;

    /**
    * Primary key index of RankID
    **/
    public static final int PKINDEX_RANKID = 0;

    /**
    * Primary key index of UserID
    **/
    public static final int PKINDEX_USERID = 1;

    /**
    * Column index of RankID
    **/
    public static final int RANKID = 0;

    /**
    * Column index of UserID
    **/
    public static final int USERID = 1;

    /**
    * Column index of Rank
    **/
    public static final int RANK = 2;

    /**
    * Column index of Data
    **/
    public static final int DATA = 3;

    /**
    * Column index of CurrentTime
    **/
    public static final int CURRENTTIME = 4;

    /**
     * This constructor is used by the runtime.
     * An application should not create an instance with this constructor
    **/
    public Ranktable() {
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
     * Get a value of UserID. 
     * The setter method of UserID is not generated because this attribute is a primarykey. 
     * @return UserID
     **/
    public final String getUserID(TxID tx) {
        // generated code
        return getString(tx,1);
    }

    /**
     * @return Rank
     **/
    public final long getRank(TxID tx) {
        // generated code
        return getLong(tx,2);
    }

    /**
     * Set a value to Rank. 
     * @param tx a transaction context
     * @param value a value to be set to Rank
     **/
    public final void  setRank(TxID tx, long value) throws AgentException {
        // generated code
        setLong(tx,2,value);
    }

    /**
     * @return Data
     **/
    public final long getData(TxID tx) {
        // generated code
        return getLong(tx,3);
    }

    /**
     * Set a value to Data. 
     * @param tx a transaction context
     * @param value a value to be set to Data
     **/
    public final void  setData(TxID tx, long value) throws AgentException {
        // generated code
        setLong(tx,3,value);
    }

    /**
     * @return CurrentTime
     **/
    public final long getCurrentTime(TxID tx) {
        // generated code
        return getLong(tx,4);
    }

    /**
     * Set a value to CurrentTime. 
     * @param tx a transaction context
     * @param value a value to be set to CurrentTime
     **/
    public final void  setCurrentTime(TxID tx, long value) throws AgentException {
        // generated code
        setLong(tx,4,value);
    }

}
