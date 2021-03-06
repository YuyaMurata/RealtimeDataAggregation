/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.agent.aggregate.handler;

import apps.count.Aggregateagent;
import bench.template.UserData;
import com.ibm.agent.exa.Message;
import com.ibm.agent.exa.MessageHandler;
import com.ibm.agent.exa.TxID;
import java.sql.Timestamp;
import java.util.List;
import rda.agent.message.UpdateMessage;

/**
 *
 * @author kaeru
 */
public class UpdateAggregateAgentHandler  extends MessageHandler{

    @Override
    public Object onMessage(Message msg) throws Exception {
        // TODO 自動生成されたメソッド・スタブ
        UpdateMessage updateMsg = (UpdateMessage) msg;
        
        // マスターエンティティを取得
        Aggregateagent agent = (Aggregateagent)getEntity();
        
        // トランザクションIDを取得
        TxID tx = getTx();
        long updateData = agent.getData(tx);
        for(UserData user : (List<UserData>)updateMsg.msgdata){
            updateData =  updateData + user.data;
        }
        
        agent.setData(tx, updateData);
        
        //Agent Status
        //Connection
        agent.setConnectionCount(tx, agent.getConnectionCount(tx) + 1);
        //Message Latency
        agent.setMessageLatency(tx, updateMsg.latency());
        
        // Update LastAccessTime
        Long time = System.currentTimeMillis();
        Timestamp updateTime = new Timestamp(time);
        
        return updateData;
    }
}
