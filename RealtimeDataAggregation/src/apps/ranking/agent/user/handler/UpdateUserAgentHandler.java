/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.agent.user.handler;

import apps.ranking.Useragent;
import bench.template.UserData;
import com.ibm.agent.exa.Message;
import com.ibm.agent.exa.MessageHandler;
import com.ibm.agent.exa.TxID;
import java.util.List;
import rda.agent.message.UpdateMessage;
import rda.extension.agent.comm.AgentIntaractionComm;
import rda.extension.agent.manager.AgentSystemExtension;

/**
 *
 * @author kaeru
 */
public class UpdateUserAgentHandler  extends MessageHandler{
    
    @Override
    public Object onMessage(Message msg) throws Exception {
        // TODO 自動生成されたメソッド・スタブ
        UpdateMessage updateMsg = (UpdateMessage) msg;
        
        // マスターエンティティを取得
        Useragent agent = (Useragent)getEntity();
        
        // トランザクションIDを取得
        TxID tx = getTx();
        long updateData = 0;
        for(UserData user : (List<UserData>)updateMsg.msgdata){
            updateData =  updateData + (Integer)user.data;
        }
        
        agent.setData(tx, agent.getData(tx)+updateData);
        
        //Agent Status
        //Connection
        agent.setConnectionCount(tx, agent.getConnectionCount(tx) + 1);
        //Message Latency
        agent.setMessageLatency(tx, updateMsg.latency());
        
        // Update LastAccessTime
        //Long time = System.currentTimeMillis();
        //Timestamp updateTime = new Timestamp(time);
        
        //Intaraction
        AgentIntaractionComm agcomm = AgentSystemExtension.getInstance().getAgentIntaraction();
        agcomm.connect(agent.getUserID(tx), new UserData(agent.getUserID(tx), updateData));
        
        return updateData;
    }
}
