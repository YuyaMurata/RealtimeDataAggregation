/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.agent.user.handler;

import apps.ranking.Useragent;
import com.ibm.agent.exa.Message;
import com.ibm.agent.exa.MessageHandler;
import com.ibm.agent.exa.TxID;

/**
 *
 * @author kaeru
 */
public class ReadUserAgentHandler extends MessageHandler {

    @Override
    public Object onMessage(Message arg0) throws Exception {
        // マスターエンティティを取得
        Useragent agent = (Useragent) getEntity();

        // トランザクションIDを取得
        TxID tx = getTx();
        
        //System.out.println(agent.getAgentID(tx)+" - "+agent.getData(tx));
        
        return agent.getData(tx);
    }
}
