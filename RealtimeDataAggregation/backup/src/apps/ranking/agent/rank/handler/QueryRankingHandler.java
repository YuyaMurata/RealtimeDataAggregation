/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.agent.rank.handler;

import apps.ranking.Rankagent;
import apps.ranking.Ranktable;
import com.ibm.agent.exa.Message;
import com.ibm.agent.exa.MessageHandler;
import com.ibm.agent.exa.TxID;
import com.ibm.agent.exa.entity.Entity;
import java.util.Iterator;

/**
 *
 * @author kaeru
 */
public class QueryRankingHandler extends MessageHandler {

    @Override
    public Object onMessage(Message arg0) throws Exception {
        // マスターエンティティを取得
        Rankagent agent = (Rankagent) getEntity();

        // トランザクションIDを取得
        TxID tx = getTx();
        
        Iterator<Entity> it = agent.getRankTableIterator(tx);
        StringBuilder sb = new StringBuilder();
        while(it.hasNext()){
            Ranktable table = (Ranktable) it.next();
            sb.append(" | ");
            sb.append(table.getRank(tx));
            sb.append(" | ");
            sb.append(table.getUserID(tx));
            sb.append(" | ");
            sb.append(table.getData(tx));
            sb.append(" | \n");
        }
        
        String msg = agent.getRankID(tx)+" : "+agent.getTotalUsers(tx)+"\n"+sb.toString();
        
        return msg;
    }
    
}
