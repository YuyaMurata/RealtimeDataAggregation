/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.ranking.agent.rank.handler;

import apps.ranking.Rankagent;
import apps.ranking.Ranktable;
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
public class UpdateRankAgentHandler  extends MessageHandler{

    @Override
    public Object onMessage(Message msg) throws Exception {
        // TODO 自動生成されたメソッド・スタブ
        UpdateMessage updateMsg = (UpdateMessage) msg;
        
        // マスターエンティティを取得
        Rankagent agent = (Rankagent)getEntity();
        
        // トランザクションIDを取得
        TxID tx = getTx();
        for(UserData user : (List<UserData>)updateMsg.msgdata){
            Ranktable table = agent.getRankTable(tx, user.id);
            if(table == null){
                table = agent.createRankTable(tx, user.id);
                table.setCurrentTime(tx, 0);
                table.setRank(tx, 1);
                
                long numUsers = agent.getTotalUsers(tx) + 1;
                agent.setTotalUsers(tx, numUsers);
            }
            
            if(table.getCurrentTime(tx) < user.time){
                table.setData(tx, (long) user.data);
                table.setCurrentTime(tx, user.time);
            }
        }
        
        //Ranking
        //Ranking Method Define
        
        //Agent Status
        // Connection
        agent.setConnectionCount(tx, agent.getConnectionCount(tx) + 1);
        
        // Message Latency
        agent.setMessageLatency(tx, updateMsg.latency());
        
        // Update LastAccessTime
        Long time = System.currentTimeMillis();
        Timestamp updateTime = new Timestamp(time);
        
        return "";
    }
}
