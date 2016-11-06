package apps.count.agent.aggregate.handler;

import apps.count.Aggregateagent;
import java.sql.Timestamp;

import rda.agent.message.InitMessage;

import com.ibm.agent.exa.Message;
import com.ibm.agent.exa.MessageHandler;
import com.ibm.agent.exa.TxID;

/**
 *　INITメッセージのメッセージハンドラ．エージェントのデータの初期化を行う．
 */
public class InitAggregateAgentHandler extends MessageHandler {
    @Override
    public Object onMessage(Message msg) throws Exception {
        try {
            InitMessage initMsg = (InitMessage)msg;

            // マスターエンティティを取得
            Aggregateagent agent = (Aggregateagent)getEntity();

            // トランザクションIDを取得
            TxID tx = getTx();
            //Aggregatecondition cond = agent.createCondition(tx);

            // 集約条件をセット
            //cond.setConditions(tx, initMsg.msgdata.get(0));
                        
            // 登録日
            Long time = System.currentTimeMillis();
            Timestamp registerTime = new Timestamp(time);
            //cond.setLastAccessTime(tx, registerTime);

            //AggregateAgent初期化
            //データのクリア
            agent.setData(tx, 0);
            //更新回数のクリア
            agent.setConnectionCount(tx, 0);
            
            agent.setMessageLatency(tx, 0);

            // set Agent Log
            //Log log = agent.createLog(tx, "init");

            // 最終更新日
            //log.setLastAccessTime(tx, registerTime);
            //log.setCurrentTime(tx, time);

            //System.out.println("InitHandler of Agent:" + getAgentKey() + " was initialized");

            // 処理結果としてエージェントキーを含む文字列を戻す
            return "hello from agent:" + getAgentKey();
        } catch(Exception e) {
            throw e;
        }
    }
}
