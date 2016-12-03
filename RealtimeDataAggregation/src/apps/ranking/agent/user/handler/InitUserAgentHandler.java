package apps.ranking.agent.user.handler;

import apps.ranking.Profile;
import apps.ranking.Useragent;
import apps.ranking.appuser.UserProfile;
import java.sql.Timestamp;

import rda.agent.message.InitMessage;

import com.ibm.agent.exa.Message;
import com.ibm.agent.exa.MessageHandler;
import com.ibm.agent.exa.TxID;
import java.util.Map;

/**
 *　INITメッセージのメッセージハンドラ．エージェントのデータの初期化を行う．
 */
public class InitUserAgentHandler extends MessageHandler {
    @Override
    public Object onMessage(Message msg) throws Exception {
        try {
            InitMessage initMsg = (InitMessage)msg;

            // マスターエンティティを取得
            Useragent agent = (Useragent)getEntity();

            // トランザクションIDを取得
            TxID tx = getTx();
                        
            // 登録日
            Long time = System.currentTimeMillis();
            Timestamp registerTime = new Timestamp(time);

            //UserAgent初期化
            //データのクリア
            agent.setData(tx, 0);
            //更新回数のクリア
            agent.setConnectionCount(tx, 0);
            
            agent.setMessageLatency(tx, 0);

            //Profile
            Map param = (Map) initMsg.msgdata;
            Profile profile = agent.createProfile(tx);
            profile.setName(tx, (String) param.get(UserProfile.profileID.NAME));
            profile.setAge(tx, (String) param.get(UserProfile.profileID.AGE));
            profile.setAddress(tx, (String) param.get(UserProfile.profileID.ADDRESS));
            profile.setSex(tx, (String) param.get(UserProfile.profileID.SEX));
            profile.setLastAccessTime(tx, registerTime);

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
