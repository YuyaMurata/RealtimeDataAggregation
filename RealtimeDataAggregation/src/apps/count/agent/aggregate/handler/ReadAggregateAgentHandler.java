/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.handler;

import apps.count.Aggregateagent;
import com.ibm.agent.exa.Message;
import com.ibm.agent.exa.MessageHandler;
import com.ibm.agent.exa.TxID;

/**
 *
 * @author kaeru
 */
public class ReadAggregateAgentHandler extends MessageHandler {

    @Override
    public Object onMessage(Message arg0) throws Exception {
        // マスターエンティティを取得
        Aggregateagent agent = (Aggregateagent) getEntity();

        // トランザクションIDを取得
        TxID tx = getTx();
        
        return agent.getData(tx);
    }
}
